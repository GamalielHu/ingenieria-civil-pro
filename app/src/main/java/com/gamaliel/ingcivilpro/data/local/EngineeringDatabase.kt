package com.gamaliel.ingcivilpro.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// ---------------------------------------------------------------------------
// ENTIDADES
// ---------------------------------------------------------------------------

/** Pregunta o caso de estudio de Normativa (NB-SABS DS 0181, DBC, cómputos). */
@Entity(tableName = "preguntas")
data class PreguntaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tema: String,
    val tipo: String,
    val enunciado: String,
    val opciones: List<String>,
    val indiceCorrecto: Int,
    val explicacion: String,
    val fundamento: String,
    val origen: String
)

/** Registro de cada respuesta del usuario para estadísticas y repaso de fallos. */
@Entity(tableName = "intentos")
data class IntentoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val preguntaId: Long,
    val correcta: Boolean,
    val respondidaEn: Long
)

/** Lección de estudio (teoría guiada) de cualquiera de los módulos. */
@Entity(tableName = "lecciones")
data class LeccionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val modulo: String,
    val orden: Int,
    val titulo: String,
    val contenido: String,
    val fundamento: String
)

/** Plantilla (modelo correcto) de asiento del Libro de Órdenes. */
@Entity(tableName = "plantillas_asiento")
data class PlantillaAsientoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val titulo: String,
    val escenario: String,
    val emisor: String,
    val nombreEmisor: String,
    val destinatario: String,
    val tipoAsiento: String,
    val itemReferencia: String,
    val instruccion: String,
    val plazoDias: Int?,
    val fundamento: String,
    val montoContrato: Double? = null,
    val montoVariacion: Double? = null,
    val acumuladoOrdenesCambio: Double? = null,
    val acumuladoContratosModificatorios: Double? = null,
    val modalidad: String? = null
)

/** Asiento registrado por el usuario en su Libro de Órdenes (persistente). */
@Entity(tableName = "asientos")
data class AsientoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val numero: Int,
    val fecha: String,
    val hora: String,
    val emisor: String,
    val nombreEmisor: String,
    val destinatario: String,
    val tipoAsiento: String,
    val itemReferencia: String,
    val instruccion: String,
    val plazoDias: Int?,
    val rubricaEmisor: Boolean,
    val firmaRecepcion: Boolean,
    val advertencias: Int,
    val creadoEn: Long
)

/** Fórmula paramétrica editable de cómputos métricos. */
@Entity(tableName = "formulas")
data class FormulaComputoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val categoria: String,
    val expresion: String,
    val variables: List<String>,
    val valoresPorDefecto: List<Double>,
    val descripcionVariables: List<String>,
    val unidad: String,
    val descripcion: String,
    val esPersonalizada: Boolean = false
)

// ---------------------------------------------------------------------------
// CONVERSORES
// ---------------------------------------------------------------------------

class Conversores {
    @TypeConverter
    fun desdeListaTexto(lista: List<String>): String = lista.joinToString(SEPARADOR)

    @TypeConverter
    fun aListaTexto(valor: String): List<String> =
        if (valor.isEmpty()) emptyList() else valor.split(SEPARADOR)

    @TypeConverter
    fun desdeListaNumeros(lista: List<Double>): String = lista.joinToString(SEPARADOR)

    @TypeConverter
    fun aListaNumeros(valor: String): List<Double> =
        if (valor.isEmpty()) emptyList() else valor.split(SEPARADOR).map { it.toDouble() }

    private companion object {
        const val SEPARADOR = "\u001F"
    }
}

// ---------------------------------------------------------------------------
// DAOs
// ---------------------------------------------------------------------------

@Dao
interface NormativaDao {
    @Query("SELECT * FROM preguntas ORDER BY tema, id")
    fun observarPreguntas(): Flow<List<PreguntaEntity>>

    @Query("SELECT COUNT(*) FROM preguntas")
    suspend fun contarPreguntas(): Int

    @Insert
    suspend fun insertarPreguntas(preguntas: List<PreguntaEntity>)

    @Insert
    suspend fun insertarIntento(intento: IntentoEntity)

    @Query("SELECT * FROM intentos ORDER BY respondidaEn DESC")
    fun observarIntentos(): Flow<List<IntentoEntity>>

    @Query("DELETE FROM intentos")
    suspend fun borrarIntentos()

    @Query("SELECT * FROM lecciones ORDER BY modulo, orden")
    fun observarLecciones(): Flow<List<LeccionEntity>>

    @Insert
    suspend fun insertarLecciones(lecciones: List<LeccionEntity>)
}

@Dao
interface LibroOrdenesDao {
    @Query("SELECT * FROM plantillas_asiento ORDER BY id")
    fun observarPlantillas(): Flow<List<PlantillaAsientoEntity>>

    @Query("SELECT COUNT(*) FROM plantillas_asiento")
    suspend fun contarPlantillas(): Int

    @Insert
    suspend fun insertarPlantillas(plantillas: List<PlantillaAsientoEntity>)

    @Query("SELECT * FROM asientos ORDER BY numero")
    fun observarAsientos(): Flow<List<AsientoEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertarAsiento(asiento: AsientoEntity)

    @Query("DELETE FROM asientos")
    suspend fun vaciarLibro()
}

@Dao
interface ComputosDao {
    @Query("SELECT * FROM formulas ORDER BY categoria, nombre")
    fun observarFormulas(): Flow<List<FormulaComputoEntity>>

    @Query("SELECT COUNT(*) FROM formulas")
    suspend fun contarFormulas(): Int

    @Insert
    suspend fun insertarFormulas(formulas: List<FormulaComputoEntity>)

    @Insert
    suspend fun insertarFormula(formula: FormulaComputoEntity): Long

    @Update
    suspend fun actualizarFormula(formula: FormulaComputoEntity)

    @Query("DELETE FROM formulas WHERE id = :id AND esPersonalizada = 1")
    suspend fun borrarFormulaPersonalizada(id: Long)
}

// ---------------------------------------------------------------------------
// BASE DE DATOS
// ---------------------------------------------------------------------------

@Database(
    entities = [
        PreguntaEntity::class,
        IntentoEntity::class,
        LeccionEntity::class,
        PlantillaAsientoEntity::class,
        AsientoEntity::class,
        FormulaComputoEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Conversores::class)
abstract class EngineeringDatabase : RoomDatabase() {
    abstract fun normativaDao(): NormativaDao
    abstract fun libroOrdenesDao(): LibroOrdenesDao
    abstract fun computosDao(): ComputosDao

    /** Carga el contenido semilla la primera vez que se abre la base. */
    suspend fun sembrarSiVacia() {
        val normativa = normativaDao()
        if (normativa.contarPreguntas() == 0) {
            normativa.insertarPreguntas(SeedData.preguntas)
            normativa.insertarLecciones(SeedData.lecciones)
        }
        val libro = libroOrdenesDao()
        if (libro.contarPlantillas() == 0) libro.insertarPlantillas(SeedData.plantillasAsiento)
        val computos = computosDao()
        if (computos.contarFormulas() == 0) computos.insertarFormulas(SeedData.formulas)
    }

    companion object {
        @Volatile
        private var instancia: EngineeringDatabase? = null

        fun obtener(context: Context): EngineeringDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    EngineeringDatabase::class.java,
                    "ingenieria_civil_pro.db"
                ).fallbackToDestructiveMigration().build().also { instancia = it }
            }
    }
}
