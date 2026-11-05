package com.gamaliel.ingcivilpro.data

import com.gamaliel.ingcivilpro.data.local.AsientoEntity
import com.gamaliel.ingcivilpro.data.local.EngineeringDatabase
import com.gamaliel.ingcivilpro.data.local.FormulaComputoEntity
import com.gamaliel.ingcivilpro.data.local.IntentoEntity
import kotlinx.coroutines.flow.Flow

/** Fuente única de datos para el ViewModel. */
class EngineeringRepository(private val db: EngineeringDatabase) {

    private val normativa = db.normativaDao()
    private val libro = db.libroOrdenesDao()
    private val computos = db.computosDao()

    val preguntas = normativa.observarPreguntas()
    val intentos = normativa.observarIntentos()
    val lecciones = normativa.observarLecciones()
    val plantillas = libro.observarPlantillas()
    val asientos: Flow<List<AsientoEntity>> = libro.observarAsientos()
    val formulas = computos.observarFormulas()

    suspend fun sembrar() = db.sembrarSiVacia()

    suspend fun registrarIntento(preguntaId: Long, correcta: Boolean) =
        normativa.insertarIntento(IntentoEntity(preguntaId = preguntaId, correcta = correcta, respondidaEn = System.currentTimeMillis()))

    suspend fun reiniciarEstadisticas() = normativa.borrarIntentos()

    suspend fun guardarAsiento(asiento: AsientoEntity) = libro.insertarAsiento(asiento)

    suspend fun vaciarLibro() = libro.vaciarLibro()

    suspend fun guardarFormula(formula: FormulaComputoEntity): Long =
        if (formula.id == 0L) computos.insertarFormula(formula) else {
            computos.actualizarFormula(formula); formula.id
        }

    suspend fun borrarFormula(id: Long) = computos.borrarFormulaPersonalizada(id)
}
