package com.gamaliel.ingcivilpro.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gamaliel.ingcivilpro.data.EngineeringRepository
import com.gamaliel.ingcivilpro.data.local.AsientoEntity
import com.gamaliel.ingcivilpro.data.local.Emisores
import com.gamaliel.ingcivilpro.data.local.EngineeringDatabase
import com.gamaliel.ingcivilpro.data.local.FormulaComputoEntity
import com.gamaliel.ingcivilpro.data.local.IntentoEntity
import com.gamaliel.ingcivilpro.data.local.LeccionEntity
import com.gamaliel.ingcivilpro.data.local.Modalidades
import com.gamaliel.ingcivilpro.data.local.PlantillaAsientoEntity
import com.gamaliel.ingcivilpro.data.local.PreguntaEntity
import com.gamaliel.ingcivilpro.data.local.TiposAsiento
import com.gamaliel.ingcivilpro.domain.Actividad
import com.gamaliel.ingcivilpro.domain.EvaluadorExpresiones
import com.gamaliel.ingcivilpro.domain.ResultadoCpm
import com.gamaliel.ingcivilpro.domain.RutaCritica
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle
import kotlin.random.Random
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ===========================================================================
// ESTADOS DE UI
// ===========================================================================

data class EstadisticaTema(val tema: String, val respondidas: Int, val aciertos: Int) {
    val porcentaje: Int get() = if (respondidas == 0) 0 else aciertos * 100 / respondidas
}

data class QuizUiState(
    val cargando: Boolean = true,
    val temas: List<String> = emptyList(),
    val temaSeleccionado: String? = null,
    val soloFalladas: Boolean = false,
    val cola: List<PreguntaEntity> = emptyList(),
    val indice: Int = 0,
    val seleccion: Int? = null,
    val respondida: Boolean = false,
    val aciertosSesion: Int = 0,
    val respondidasSesion: Int = 0,
    val estadisticas: List<EstadisticaTema> = emptyList(),
    val totalPreguntas: Int = 0
) {
    val actual: PreguntaEntity? get() = cola.getOrNull(indice)
    val esCorrecta: Boolean get() = respondida && seleccion == actual?.indiceCorrecto
    val terminada: Boolean get() = !cargando && cola.isNotEmpty() && indice >= cola.size
}

enum class Severidad { CRITICA, ADVERTENCIA, INFO }

data class Hallazgo(
    val severidad: Severidad,
    val campo: String,
    val mensaje: String,
    val fundamento: String
)

data class AsientoForm(
    val numero: String = "",
    val fecha: String = "",
    val hora: String = "",
    val emisor: String = Emisores.SUPERVISOR,
    val nombreEmisor: String = "",
    val destinatario: String = "",
    val tipo: String = TiposAsiento.ORDEN,
    val itemReferencia: String = "",
    val instruccion: String = "",
    val plazoDias: String = "",
    val rubricaEmisor: Boolean = false,
    val firmaRecepcion: Boolean = false,
    // Orden de Cambio
    val montoContrato: String = "",
    val montoVariacion: String = "",
    val acumuladoOC: String = "0",
    val acumuladoCM: String = "0",
    val modalidad: String = Modalidades.LICITACION,
    val creaNuevosItems: Boolean = false,
    val incrementaPreciosUnitarios: Boolean = false,
    val informeTecnico: Boolean = false,
    val informeLegal: Boolean = false,
    val aprobadaPorFiscal: Boolean = false
)

data class Ejercicio(
    val titulo: String,
    val escenario: String,
    val problemasOcultos: List<String>,
    val mostrarPistas: Boolean = false,
    val resuelto: Boolean = false
)

data class LibroUiState(
    val form: AsientoForm = AsientoForm(),
    val hallazgos: List<Hallazgo> = emptyList(),
    val validado: Boolean = false,
    val ejercicio: Ejercicio? = null,
    val asientos: List<AsientoEntity> = emptyList(),
    val plantillas: List<PlantillaAsientoEntity> = emptyList(),
    val mensaje: String? = null,
    val ejerciciosResueltos: Int = 0
) {
    val criticas: Int get() = hallazgos.count { it.severidad == Severidad.CRITICA }
    val advertencias: Int get() = hallazgos.count { it.severidad == Severidad.ADVERTENCIA }
    val puedeRegistrar: Boolean get() = validado && criticas == 0
}

data class CpmUiState(
    val actividades: List<Actividad> = SemillaCronograma.actividades,
    val plazoReferencial: String = "30",
    val precioReferencial: String = "378426.61",
    val resultado: ResultadoCpm? = null,
    val error: String? = null
)

data class ComputosUiState(
    val formulas: List<FormulaComputoEntity> = emptyList(),
    val categorias: List<String> = emptyList(),
    val categoriaSeleccionada: String? = null,
    val formulaSeleccionada: FormulaComputoEntity? = null,
    val expresion: String = "",
    val valores: Map<String, String> = emptyMap(),
    val resultado: Double? = null,
    val error: String? = null,
    val mensaje: String? = null,
    val cpm: CpmUiState = CpmUiState()
) {
    val variablesActuales: List<String>
        get() = EvaluadorExpresiones.detectarVariables(expresion)
}

// ===========================================================================
// VIEWMODEL
// ===========================================================================

class EngineeringViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = EngineeringRepository(EngineeringDatabase.obtener(application))

    private val _quiz = MutableStateFlow(QuizUiState())
    val quiz: StateFlow<QuizUiState> = _quiz.asStateFlow()

    private val _libro = MutableStateFlow(LibroUiState())
    val libro: StateFlow<LibroUiState> = _libro.asStateFlow()

    private val _computos = MutableStateFlow(ComputosUiState())
    val computos: StateFlow<ComputosUiState> = _computos.asStateFlow()

    val lecciones: StateFlow<List<LeccionEntity>> =
        repo.lecciones.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private var todasLasPreguntas: List<PreguntaEntity> = emptyList()
    private var intentos: List<IntentoEntity> = emptyList()

    init {
        viewModelScope.launch {
            repo.sembrar()
            combine(repo.preguntas, repo.intentos) { p, i -> p to i }.collect { (p, i) ->
                val primeraCarga = todasLasPreguntas.isEmpty() && p.isNotEmpty()
                todasLasPreguntas = p
                intentos = i
                _quiz.update {
                    it.copy(
                        cargando = false,
                        temas = p.map { q -> q.tema }.distinct(),
                        estadisticas = calcularEstadisticas(p, i),
                        totalPreguntas = p.size
                    )
                }
                if (primeraCarga) reiniciarQuiz()
            }
        }
        viewModelScope.launch {
            repo.asientos.collect { lista ->
                _libro.update { st ->
                    val f = st.form
                    val nuevoForm = if (f.numero.isBlank()) f.copy(numero = (lista.size + 1).toString()) else f
                    st.copy(asientos = lista, form = nuevoForm)
                }
            }
        }
        viewModelScope.launch {
            repo.plantillas.collect { p -> _libro.update { it.copy(plantillas = p) } }
        }
        viewModelScope.launch {
            repo.formulas.collect { lista ->
                _computos.update { st ->
                    val seleccion = st.formulaSeleccionada?.let { sel -> lista.find { it.id == sel.id } }
                    st.copy(formulas = lista, categorias = lista.map { it.categoria }.distinct(), formulaSeleccionada = seleccion ?: st.formulaSeleccionada)
                }
            }
        }
        calcularCpm()
    }

    // -----------------------------------------------------------------------
    // NORMATIVA / QUIZ
    // -----------------------------------------------------------------------

    private fun calcularEstadisticas(p: List<PreguntaEntity>, i: List<IntentoEntity>): List<EstadisticaTema> {
        val temaDe = p.associate { it.id to it.tema }
        return i.groupBy { temaDe[it.preguntaId] ?: "?" }
            .map { (tema, lista) -> EstadisticaTema(tema, lista.size, lista.count { it.correcta }) }
            .sortedBy { it.porcentaje }
    }

    fun seleccionarTema(tema: String?) {
        _quiz.update { it.copy(temaSeleccionado = tema) }
        reiniciarQuiz()
    }

    fun alternarSoloFalladas(valor: Boolean) {
        _quiz.update { it.copy(soloFalladas = valor) }
        reiniciarQuiz()
    }

    fun reiniciarQuiz() {
        val st = _quiz.value
        var base = todasLasPreguntas.filter { st.temaSeleccionado == null || it.tema == st.temaSeleccionado }
        if (st.soloFalladas) {
            // Una pregunta se considera fallada si su último intento fue incorrecto.
            val ultimo = intentos.groupBy { it.preguntaId }.mapValues { (_, l) -> l.maxBy { it.respondidaEn }.correcta }
            base = base.filter { ultimo[it.id] == false }
        }
        _quiz.update {
            it.copy(cola = base.shuffled(), indice = 0, seleccion = null, respondida = false, aciertosSesion = 0, respondidasSesion = 0)
        }
    }

    fun elegirOpcion(indice: Int) {
        if (_quiz.value.respondida) return
        _quiz.update { it.copy(seleccion = indice) }
    }

    /** Valida instantáneamente la respuesta y registra el intento en Room. */
    fun responder() {
        val st = _quiz.value
        val pregunta = st.actual ?: return
        val sel = st.seleccion ?: return
        if (st.respondida) return
        val correcta = sel == pregunta.indiceCorrecto
        _quiz.update {
            it.copy(
                respondida = true,
                aciertosSesion = it.aciertosSesion + if (correcta) 1 else 0,
                respondidasSesion = it.respondidasSesion + 1
            )
        }
        viewModelScope.launch { repo.registrarIntento(pregunta.id, correcta) }
    }

    fun siguientePregunta() {
        _quiz.update { it.copy(indice = it.indice + 1, seleccion = null, respondida = false) }
    }

    fun reiniciarEstadisticas() {
        viewModelScope.launch { repo.reiniciarEstadisticas() }
    }

    // -----------------------------------------------------------------------
    // LIBRO DE ÓRDENES
    // -----------------------------------------------------------------------

    fun actualizarForm(transformar: (AsientoForm) -> AsientoForm) {
        _libro.update { it.copy(form = transformar(it.form), validado = false, mensaje = null) }
    }

    fun validarAsiento() {
        val st = _libro.value
        val hallazgos = ValidadorLibroOrdenes.validar(st.form, st.asientos.maxByOrNull { it.numero }, LocalDate.now())
        val criticas = hallazgos.count { it.severidad == Severidad.CRITICA }
        val ejercicio = st.ejercicio
        val resueltoAhora = ejercicio != null && !ejercicio.resuelto && criticas == 0
        _libro.update {
            it.copy(
                hallazgos = hallazgos,
                validado = true,
                ejercicio = if (resueltoAhora) ejercicio?.copy(resuelto = true) else it.ejercicio,
                ejerciciosResueltos = it.ejerciciosResueltos + if (resueltoAhora) 1 else 0,
                mensaje = when {
                    resueltoAhora -> "¡Ejercicio resuelto! El asiento ya no tiene omisiones críticas."
                    criticas == 0 -> "Asiento conforme: puede registrarse en el libro."
                    else -> "Se encontraron $criticas omisiones críticas. Corrígelas y vuelve a validar."
                }
            )
        }
    }

    fun registrarAsiento() {
        val st = _libro.value
        if (!st.puedeRegistrar) {
            _libro.update { it.copy(mensaje = "Valida el asiento y corrige las omisiones críticas antes de registrarlo.") }
            return
        }
        val f = st.form
        val entidad = AsientoEntity(
            numero = f.numero.trim().toInt(),
            fecha = f.fecha.trim(),
            hora = f.hora.trim(),
            emisor = f.emisor,
            nombreEmisor = f.nombreEmisor.trim(),
            destinatario = f.destinatario.trim(),
            tipoAsiento = f.tipo,
            itemReferencia = f.itemReferencia.trim(),
            instruccion = f.instruccion.trim(),
            plazoDias = f.plazoDias.trim().toIntOrNull(),
            rubricaEmisor = f.rubricaEmisor,
            firmaRecepcion = f.firmaRecepcion,
            advertencias = st.advertencias,
            creadoEn = System.currentTimeMillis()
        )
        viewModelScope.launch {
            repo.guardarAsiento(entidad)
            _libro.update {
                it.copy(
                    form = AsientoForm(numero = (entidad.numero + 1).toString(), fecha = entidad.fecha),
                    hallazgos = emptyList(), validado = false, ejercicio = null,
                    mensaje = "Asiento N° ${entidad.numero} registrado en el Libro de Órdenes."
                )
            }
        }
    }

    fun nuevoAsientoEnBlanco() {
        val siguiente = (_libro.value.asientos.maxOfOrNull { it.numero } ?: 0) + 1
        _libro.update {
            it.copy(
                form = AsientoForm(numero = siguiente.toString(), fecha = LocalDate.now().format(FORMATO_FECHA), hora = LocalTime.now().format(FORMATO_HORA)),
                hallazgos = emptyList(), validado = false, ejercicio = null, mensaje = null
            )
        }
    }

    /** Carga una plantilla correcta como modelo de estudio. */
    fun cargarModelo(plantilla: PlantillaAsientoEntity) {
        val form = formDesdePlantilla(plantilla)
        _libro.update { it.copy(form = form, hallazgos = emptyList(), validado = false, ejercicio = null, mensaje = "Modelo cargado: ${plantilla.titulo}. Valídalo para ver por qué es correcto.") }
    }

    /** Genera un ejercicio: toma un modelo correcto e introduce inconsistencias que el usuario debe resolver. */
    fun generarEjercicio(semilla: Long = System.nanoTime()) {
        val plantillas = _libro.value.plantillas
        if (plantillas.isEmpty()) return
        val random = Random(semilla)
        val plantilla = plantillas[random.nextInt(plantillas.size)]
        val (corrupto, problemas) = GeneradorInconsistencias.corromper(formDesdePlantilla(plantilla), random)
        _libro.update {
            it.copy(
                form = corrupto, hallazgos = emptyList(), validado = false,
                ejercicio = Ejercicio(plantilla.titulo, plantilla.escenario, problemas),
                mensaje = "Ejercicio generado con ${problemas.size} inconsistencias. Corrígelas y valida."
            )
        }
    }

    fun alternarPistas() {
        _libro.update { st -> st.copy(ejercicio = st.ejercicio?.let { it.copy(mostrarPistas = !it.mostrarPistas) }) }
    }

    fun vaciarLibro() {
        viewModelScope.launch {
            repo.vaciarLibro()
            _libro.update { it.copy(form = AsientoForm(numero = "1"), hallazgos = emptyList(), validado = false, mensaje = "Libro de Órdenes vaciado.") }
        }
    }

    private fun formDesdePlantilla(p: PlantillaAsientoEntity): AsientoForm {
        val asientos = _libro.value.asientos
        val siguiente = (asientos.maxOfOrNull { it.numero } ?: 0) + 1
        val ultimaFecha = asientos.maxByOrNull { it.numero }?.fecha?.let { parsearFecha(it) }
        val hoy = LocalDate.now()
        val fecha = if (ultimaFecha != null && ultimaFecha.isAfter(hoy)) ultimaFecha else hoy
        return AsientoForm(
            numero = siguiente.toString(),
            fecha = fecha.format(FORMATO_FECHA),
            hora = "09:00",
            emisor = p.emisor,
            nombreEmisor = p.nombreEmisor,
            destinatario = p.destinatario,
            tipo = p.tipoAsiento,
            itemReferencia = p.itemReferencia,
            instruccion = p.instruccion,
            plazoDias = p.plazoDias?.toString() ?: "",
            rubricaEmisor = true,
            firmaRecepcion = true,
            montoContrato = p.montoContrato?.let { formatoNumero(it) } ?: "",
            montoVariacion = p.montoVariacion?.let { formatoNumero(it) } ?: "",
            acumuladoOC = p.acumuladoOrdenesCambio?.let { formatoNumero(it) } ?: "0",
            acumuladoCM = p.acumuladoContratosModificatorios?.let { formatoNumero(it) } ?: "0",
            modalidad = p.modalidad ?: Modalidades.LICITACION,
            informeTecnico = p.tipoAsiento == TiposAsiento.ORDEN_CAMBIO,
            informeLegal = p.tipoAsiento == TiposAsiento.ORDEN_CAMBIO,
            aprobadaPorFiscal = p.tipoAsiento == TiposAsiento.ORDEN_CAMBIO
        )
    }

    // -----------------------------------------------------------------------
    // CÓMPUTOS MÉTRICOS
    // -----------------------------------------------------------------------

    fun seleccionarCategoria(categoria: String?) {
        _computos.update { it.copy(categoriaSeleccionada = categoria) }
    }

    fun seleccionarFormula(formula: FormulaComputoEntity) {
        val valores = formula.variables.mapIndexed { i, v -> v to formatoNumero(formula.valoresPorDefecto.getOrElse(i) { 0.0 }) }.toMap()
        _computos.update { it.copy(formulaSeleccionada = formula, expresion = formula.expresion, valores = valores, resultado = null, error = null, mensaje = null) }
        calcularFormula()
    }

    fun cerrarFormula() {
        _computos.update { it.copy(formulaSeleccionada = null, resultado = null, error = null, mensaje = null) }
    }

    fun nuevaFormula() {
        val nueva = FormulaComputoEntity(
            nombre = "Nueva fórmula", categoria = "Personalizadas", expresion = "a*b*c",
            variables = listOf("a", "b", "c"), valoresPorDefecto = listOf(1.0, 1.0, 1.0),
            descripcionVariables = listOf("", "", ""), unidad = "m³", descripcion = "Fórmula definida por el usuario.", esPersonalizada = true
        )
        seleccionarFormula(nueva)
    }

    fun editarExpresion(expresion: String) {
        _computos.update { st ->
            val vars = EvaluadorExpresiones.detectarVariables(expresion)
            val valores = vars.associateWith { v -> st.valores[v] ?: "1" }
            st.copy(expresion = expresion, valores = valores)
        }
        calcularFormula()
    }

    fun editarValor(variable: String, valor: String) {
        _computos.update { it.copy(valores = it.valores + (variable to valor)) }
        calcularFormula()
    }

    fun calcularFormula() {
        val st = _computos.value
        if (st.formulaSeleccionada == null) return
        val numeros = mutableMapOf<String, Double>()
        for (v in st.variablesActuales) {
            val n = st.valores[v]?.replace(',', '.')?.trim()?.toDoubleOrNull()
            if (n == null) {
                _computos.update { it.copy(resultado = null, error = "Ingresa un valor numérico para '$v'") }
                return
            }
            numeros[v] = n
        }
        try {
            val r = EvaluadorExpresiones(numeros).evaluar(st.expresion)
            _computos.update { it.copy(resultado = r, error = null) }
        } catch (e: IllegalArgumentException) {
            _computos.update { it.copy(resultado = null, error = e.message) }
        }
    }

    /** Guarda la fórmula editada. Las fórmulas de fábrica se guardan como copia personalizada. */
    fun guardarFormula(nombre: String, unidad: String) {
        val st = _computos.value
        val base = st.formulaSeleccionada ?: return
        if (st.error != null || st.resultado == null) {
            _computos.update { it.copy(mensaje = "Corrige la expresión antes de guardar.") }
            return
        }
        val vars = st.variablesActuales
        val descripciones = vars.map { v ->
            val i = base.variables.indexOf(v)
            if (i >= 0) base.descripcionVariables.getOrElse(i) { "" } else ""
        }
        val formula = base.copy(
            id = if (base.esPersonalizada) base.id else 0L,
            nombre = nombre.ifBlank { base.nombre } + if (!base.esPersonalizada && nombre == base.nombre) " (personalizada)" else "",
            categoria = if (base.esPersonalizada) base.categoria else "Personalizadas",
            expresion = st.expresion,
            variables = vars,
            valoresPorDefecto = vars.map { v -> st.valores[v]?.replace(',', '.')?.toDoubleOrNull() ?: 0.0 },
            descripcionVariables = descripciones,
            unidad = unidad.ifBlank { base.unidad },
            esPersonalizada = true
        )
        viewModelScope.launch {
            val id = repo.guardarFormula(formula)
            _computos.update { it.copy(formulaSeleccionada = formula.copy(id = id), mensaje = "Fórmula guardada en 'Personalizadas'.") }
        }
    }

    fun borrarFormula() {
        val f = _computos.value.formulaSeleccionada ?: return
        if (!f.esPersonalizada || f.id == 0L) return
        viewModelScope.launch {
            repo.borrarFormula(f.id)
            cerrarFormula()
        }
    }

    // ---- Cronograma / ruta crítica ----

    fun cambiarCuadrillas(id: Int, delta: Int) = editarActividad(id) { it.copy(cuadrillas = (it.cuadrillas + delta).coerceIn(1, 50)) }

    fun cambiarDiasBase(id: Int, texto: String) {
        val d = texto.replace(',', '.').toDoubleOrNull() ?: return
        editarActividad(id) { it.copy(diasBase = d.coerceAtLeast(0.0)) }
    }

    fun cambiarPredecesoras(id: Int, texto: String) {
        val preds = texto.split(',', ';', ' ').mapNotNull { it.trim().toIntOrNull() }.distinct()
        editarActividad(id) { it.copy(predecesoras = preds) }
    }

    fun agregarActividad(nombre: String, unidad: String, volumen: String, unitario: String, dias: String, predecesoras: String) {
        val acts = _computos.value.cpm.actividades
        val nueva = Actividad(
            id = (acts.maxOfOrNull { it.id } ?: 0) + 1,
            nombre = nombre.ifBlank { "Actividad" },
            unidad = unidad.ifBlank { "GLB" },
            volumen = volumen.replace(',', '.').toDoubleOrNull() ?: 0.0,
            precioUnitario = unitario.replace(',', '.').toDoubleOrNull() ?: 0.0,
            diasBase = dias.replace(',', '.').toDoubleOrNull() ?: 1.0,
            cuadrillas = 1,
            predecesoras = predecesoras.split(',', ';', ' ').mapNotNull { it.trim().toIntOrNull() }
        )
        _computos.update { it.copy(cpm = it.cpm.copy(actividades = acts + nueva)) }
        calcularCpm()
    }

    fun eliminarActividad(id: Int) {
        _computos.update { st ->
            val restantes = st.cpm.actividades.filter { it.id != id }.map { a -> a.copy(predecesoras = a.predecesoras - id) }
            st.copy(cpm = st.cpm.copy(actividades = restantes))
        }
        calcularCpm()
    }

    fun restaurarCronogramaExamen() {
        _computos.update { it.copy(cpm = CpmUiState()) }
        calcularCpm()
    }

    fun cambiarReferenciales(plazo: String, precio: String) {
        _computos.update { it.copy(cpm = it.cpm.copy(plazoReferencial = plazo, precioReferencial = precio)) }
    }

    private fun editarActividad(id: Int, cambio: (Actividad) -> Actividad) {
        _computos.update { st -> st.copy(cpm = st.cpm.copy(actividades = st.cpm.actividades.map { if (it.id == id) cambio(it) else it })) }
        calcularCpm()
    }

    fun calcularCpm() {
        val cpm = _computos.value.cpm
        try {
            val r = RutaCritica.calcular(cpm.actividades)
            _computos.update { it.copy(cpm = it.cpm.copy(resultado = r, error = null)) }
        } catch (e: IllegalArgumentException) {
            _computos.update { it.copy(cpm = it.cpm.copy(resultado = null, error = e.message)) }
        }
    }

    companion object {
        val FORMATO_FECHA: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT)
        val FORMATO_HORA: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        fun parsearFecha(texto: String): LocalDate? = try {
            LocalDate.parse(texto.trim(), FORMATO_FECHA)
        } catch (e: Exception) {
            null
        }

        fun formatoNumero(n: Double): String =
            if (n == Math.floor(n) && kotlin.math.abs(n) < 1e15) n.toLong().toString() else n.toString()
    }
}

// ===========================================================================
// VALIDACIÓN NORMATIVA DEL LIBRO DE ÓRDENES
// ===========================================================================

object ValidadorLibroOrdenes {

    private const val F_DBC = "DBC Obras – cláusula Libro de Órdenes"

    val expresionesAmbiguas = listOf(
        "lo antes posible", "a la brevedad", "según criterio", "segun criterio", "más o menos", "mas o menos",
        "aproximadamente", "etc", "ver tema", "si es posible", "cuando pueda", "en lo posible", "algún", "cuanto antes"
    )

    val verbosDeAccion = listOf(
        "instruy", "ejecut", "realiz", "retir", "reempla", "demol", "repar", "corrig", "present", "coloc", "recoloc",
        "vaci", "excav", "suspend", "paraliz", "reanud", "verific", "instal", "complet", "rellen", "compact", "limpi",
        "ensay", "entreg", "solicit", "inform", "aprueb", "aprob", "observ", "emit", "ajust", "refuerz", "reforz", "mantener", "continu"
    )

    private val indiciosCasoFortuito = listOf("senamhi", "pluviom", "registro", "informe", "fotograf", "cronograma", "caso fortuito", "fuerza mayor")

    fun validar(f: AsientoForm, ultimo: AsientoEntity?, hoy: LocalDate): List<Hallazgo> {
        val h = mutableListOf<Hallazgo>()
        fun critica(campo: String, msg: String, fund: String = F_DBC) = h.add(Hallazgo(Severidad.CRITICA, campo, msg, fund))
        fun advertencia(campo: String, msg: String, fund: String = F_DBC) = h.add(Hallazgo(Severidad.ADVERTENCIA, campo, msg, fund))
        fun info(campo: String, msg: String, fund: String = F_DBC) = h.add(Hallazgo(Severidad.INFO, campo, msg, fund))

        // 1. Número correlativo
        val numero = f.numero.trim().toIntOrNull()
        val esperado = (ultimo?.numero ?: 0) + 1
        when {
            f.numero.isBlank() -> critica("N° de asiento", "Falta el número correlativo. Las hojas del libro son numeradas y no pueden omitirse.")
            numero == null || numero <= 0 -> critica("N° de asiento", "El número de asiento debe ser un entero positivo.")
            numero != esperado -> critica("N° de asiento", "Correlatividad rota: el último asiento registrado es el N° ${esperado - 1}; corresponde el N° $esperado. Saltar o repetir números invalida la trazabilidad del libro.")
        }

        // 2. Fecha y hora
        val fecha = EngineeringViewModel.parsearFecha(f.fecha)
        if (f.fecha.isBlank()) {
            critica("Fecha", "Falta la FECHA. Sin fecha no puede computarse el plazo de cumplimiento ni acreditarse la oportunidad del asiento.")
        } else if (fecha == null) {
            critica("Fecha", "Fecha inválida. Use el formato dd/mm/aaaa (ej. 05/11/2026).")
        } else if (fecha.isAfter(hoy)) {
            critica("Fecha", "La fecha (${f.fecha}) es posterior a hoy. No se pueden registrar asientos con fecha adelantada.")
        } else if (ultimo != null) {
            val fechaUltimo = EngineeringViewModel.parsearFecha(ultimo.fecha)
            if (fechaUltimo != null && fecha.isBefore(fechaUltimo)) {
                critica("Fecha", "La fecha es anterior a la del asiento N° ${ultimo.numero} (${ultimo.fecha}). El libro debe llevarse en orden cronológico.")
            }
        }
        if (f.hora.isBlank()) {
            advertencia("Hora", "Registre la hora: en órdenes con plazos cortos o paralizaciones define el inicio del cómputo.")
        } else if (!Regex("^([01]?\\d|2[0-3]):[0-5]\\d$").matches(f.hora.trim())) {
            advertencia("Hora", "Hora con formato inválido. Use HH:mm (ej. 09:30).")
        }

        // 3. Emisor y destinatario
        if (f.nombreEmisor.isBlank()) critica("Emisor", "Falta el nombre y cargo de quien emite el asiento. Debe identificarse al responsable.")
        if (f.destinatario.isBlank()) {
            critica("Destinatario", "Falta el destinatario del asiento.")
        } else {
            val dest = f.destinatario.lowercase()
            if (f.emisor == Emisores.SUPERVISOR && dest.contains("supervis")) critica("Destinatario", "El Supervisor no puede dirigirse el asiento a sí mismo; el destinatario es el Contratista/Superintendente.")
            if (f.emisor == Emisores.CONTRATISTA && (dest.contains("superintendente") || dest.contains("contratista"))) critica("Destinatario", "El Contratista no puede dirigirse el asiento a sí mismo; el destinatario es el Supervisor.")
        }

        // 4. Competencia según tipo de asiento
        if (f.emisor == Emisores.CONTRATISTA && f.tipo in TiposAsiento.exclusivosSupervisor) {
            critica(
                "Tipo de asiento",
                "El Contratista NO emite '${TiposAsiento.etiqueta(f.tipo)}'. Las órdenes, observaciones, aprobaciones y Órdenes de Cambio las emite el Supervisor; el Contratista registra solicitudes y comunicaciones."
            )
        }
        if (f.emisor == Emisores.SUPERVISOR && f.tipo == TiposAsiento.AMPLIACION_PLAZO) {
            advertencia("Tipo de asiento", "La ampliación de plazo la SOLICITA el Contratista; el Supervisor la evalúa y, si procede, la formaliza mediante Orden de Cambio o Contrato Modificatorio.", "Art. 89 NB-SABS")
        }

        // 5. Ítem / frente de obra
        if (f.itemReferencia.isBlank()) {
            if (f.tipo in setOf(TiposAsiento.ORDEN, TiposAsiento.OBSERVACION, TiposAsiento.ORDEN_CAMBIO, TiposAsiento.PARALIZACION)) {
                critica("Ítem / frente", "Indique el ítem del presupuesto o el frente de obra (eje, nivel, progresiva) al que se refiere la instrucción.")
            } else {
                advertencia("Ítem / frente", "Conviene indicar el ítem o frente de obra para la trazabilidad en planillas.")
            }
        }

        // 6. Instrucción clara
        val texto = f.instruccion.trim()
        val textoMin = texto.lowercase()
        if (texto.isBlank()) {
            critica("Instrucción", "El asiento no tiene contenido. Toda orden o comunicación debe redactarse por escrito en el libro.")
        } else {
            if (texto.length < 60) critica("Instrucción", "Texto demasiado escueto (${texto.length} caracteres). Una instrucción clara indica QUÉ, DÓNDE, CÓMO, PLAZO y VERIFICACIÓN.")
            expresionesAmbiguas.filter { exp ->
                if (exp == "etc") Regex("\\betc\\b").containsMatchIn(textoMin) else textoMin.contains(exp)
            }.forEach { exp ->
                advertencia("Instrucción", "Expresión ambigua '$exp': genera controversias sobre el alcance o el plazo. Sustitúyala por un dato concreto.")
            }
            if (f.tipo in setOf(TiposAsiento.ORDEN, TiposAsiento.OBSERVACION, TiposAsiento.PARALIZACION, TiposAsiento.ORDEN_CAMBIO) &&
                verbosDeAccion.none { textoMin.contains(it) }
            ) {
                critica("Instrucción", "No se identifica una acción concreta (ej.: ejecutar, retirar, reemplazar, suspender, presentar). La instrucción no es ejecutable.")
            }
            if (f.tipo in setOf(TiposAsiento.ORDEN, TiposAsiento.OBSERVACION) &&
                listOf("plano", "especificaci", "norma", "según", "conforme", "de acuerdo").none { textoMin.contains(it) }
            ) {
                advertencia("Instrucción", "No se cita plano, especificación técnica o norma de referencia (el CÓMO).")
            }
            if (f.tipo in setOf(TiposAsiento.ORDEN, TiposAsiento.OBSERVACION, TiposAsiento.ORDEN_CAMBIO) &&
                listOf("verific", "supervisión", "supervision", "control", "ensay", "inspecc").none { textoMin.contains(it) }
            ) {
                info("Instrucción", "Sugerencia: indique cómo y cuándo Supervisión verificará el cumplimiento.")
            }
        }

        // 7. Plazo
        val plazo = f.plazoDias.trim().toIntOrNull()
        if (f.tipo in TiposAsiento.requierenPlazo) {
            when {
                f.plazoDias.isBlank() -> critica("Plazo", "Falta el PLAZO de cumplimiento. Una orden sin plazo no es exigible ni permite aplicar multas por incumplimiento.")
                plazo == null || plazo <= 0 -> critica("Plazo", "El plazo debe ser un número entero de días mayor a cero.")
                plazo != null && plazo > 180 -> advertencia("Plazo", "Plazo inusualmente largo ($plazo días). Verifique que no exceda el plazo contractual remanente.")
            }
        } else if (f.tipo == TiposAsiento.SOLICITUD && f.plazoDias.isBlank()) {
            info("Plazo", "Puede indicar el plazo en que requiere respuesta del Supervisor para dejar constancia de la urgencia.")
        }

        // 8. Rúbricas
        if (!f.rubricaEmisor) critica("Rúbrica", "Falta la RÚBRICA (firma) del emisor. Un asiento sin firma carece de validez.")
        if (!f.firmaRecepcion) {
            if (f.tipo in TiposAsiento.requierenPlazo || f.tipo == TiposAsiento.APROBACION) {
                critica("Firma de recepción", "Falta la firma de recepción del destinatario: sin constancia de notificación no corre el plazo de cumplimiento.")
            } else {
                advertencia("Firma de recepción", "Obtenga la firma de recepción del Supervisor como constancia de presentación de la solicitud.")
            }
        }

        // 9. Reglas específicas por tipo
        when (f.tipo) {
            TiposAsiento.ORDEN_CAMBIO -> validarOrdenDeCambio(f, h)
            TiposAsiento.AMPLIACION_PLAZO -> {
                if (indiciosCasoFortuito.none { textoMin.contains(it) }) {
                    critica("Respaldo técnico", "La solicitud de ampliación no menciona respaldo: registros oficiales (SENAMHI), informe técnico de días perdidos en la ruta crítica, cronograma reprogramado o fotografías.", "Art. 89 I NB-SABS")
                }
                if (!Regex("\\d+\\s*d[ií]as").containsMatchIn(textoMin)) {
                    critica("Instrucción", "Indique cuántos días de ampliación se solicitan.")
                }
            }
            TiposAsiento.PARALIZACION -> {
                if (listOf("seguridad", "fuerza mayor", "caso fortuito", "riesgo", "calidad", "reanud").none { textoMin.contains(it) }) {
                    critica("Causa", "Toda paralización debe expresar su causa (seguridad, calidad, fuerza mayor o caso fortuito) y las condiciones para reanudar.")
                }
            }
        }
        return h.sortedBy { it.severidad.ordinal }
    }

    private fun validarOrdenDeCambio(f: AsientoForm, h: MutableList<Hallazgo>) {
        val art = "Art. 89 NB-SABS"
        val monto = f.montoContrato.replace(',', '.').toDoubleOrNull()
        val variacion = f.montoVariacion.replace(',', '.').toDoubleOrNull()
        val accOC = f.acumuladoOC.replace(',', '.').toDoubleOrNull() ?: 0.0
        val accCM = f.acumuladoCM.replace(',', '.').toDoubleOrNull() ?: 0.0

        if (monto == null || monto <= 0) {
            h.add(Hallazgo(Severidad.CRITICA, "Monto del contrato", "Indique el monto del contrato principal para verificar los topes.", art)); return
        }
        if (variacion == null) {
            h.add(Hallazgo(Severidad.CRITICA, "Monto de la OC", "Indique el monto (positivo o negativo) que modifica la Orden de Cambio.", art)); return
        }
        if (f.creaNuevosItems) h.add(Hallazgo(Severidad.CRITICA, "Nuevos ítems", "La Orden de Cambio NO puede crear ítems nuevos. Para ítems nuevos corresponde Contrato Modificatorio con precios unitarios negociados.", "Art. 89 II a) y d)"))
        if (f.incrementaPreciosUnitarios) h.add(Hallazgo(Severidad.CRITICA, "Precios unitarios", "Ninguna modificación puede incrementar los precios unitarios del contrato.", "Art. 89 II a) y d)"))

        val pctOC = (accOC + variacion) / monto * 100
        val pctTotal = (accOC + accCM + variacion) / monto * 100
        if (pctOC > 5.0) {
            h.add(Hallazgo(Severidad.CRITICA, "Tope OC", "Las Órdenes de Cambio acumuladas suman ${"%.2f".format(pctOC)}% del contrato; el máximo es 5%. Evalúe Contrato Modificatorio.", "Art. 89 II d)"))
        } else {
            h.add(Hallazgo(Severidad.INFO, "Tope OC", "Órdenes de Cambio acumuladas: ${"%.2f".format(pctOC)}% (máx. 5%).", "Art. 89 II d)"))
        }
        val tope = if (f.modalidad == Modalidades.LICITACION) 15.0 else 10.0
        if (pctTotal > tope) {
            h.add(Hallazgo(Severidad.CRITICA, "Tope conjunto", "OC + CM suman ${"%.2f".format(pctTotal)}%; en ${Modalidades.etiqueta(f.modalidad)} el máximo es ${tope.toInt()}%.", "Art. 89 II d)"))
        } else {
            h.add(Hallazgo(Severidad.INFO, "Tope conjunto", "OC + CM: ${"%.2f".format(pctTotal)}% (máx. ${tope.toInt()}% en ${Modalidades.etiqueta(f.modalidad)}).", "Art. 89 II d)"))
        }
        if (accCM / monto * 100 > 10.0) h.add(Hallazgo(Severidad.CRITICA, "Contratos modificatorios", "Los Contratos Modificatorios acumulados superan el 10% del contrato principal.", "Art. 89 II a)"))
        if (!f.informeTecnico) h.add(Hallazgo(Severidad.CRITICA, "Informe técnico", "Toda modificación debe sustentarse en INFORME TÉCNICO que establezca la viabilidad técnica y de financiamiento.", "Art. 89 I"))
        if (!f.informeLegal) h.add(Hallazgo(Severidad.CRITICA, "Informe legal", "Toda modificación debe sustentarse en INFORME LEGAL.", "Art. 89 I"))
        if (!f.aprobadaPorFiscal) h.add(Hallazgo(Severidad.CRITICA, "Aprobación", "La Orden de Cambio debe ser aprobada por la instancia responsable del seguimiento de la obra en la entidad (Fiscal).", "Art. 89 II d)"))
    }
}

// ===========================================================================
// GENERADOR DE INCONSISTENCIAS PARA EJERCICIOS
// ===========================================================================

object GeneradorInconsistencias {

    private data class Corrupcion(val descripcion: String, val aplicable: (AsientoForm) -> Boolean, val aplicar: (AsientoForm) -> AsientoForm)

    private val corrupciones = listOf(
        Corrupcion("Se borró la fecha del asiento", { true }) { it.copy(fecha = "") },
        Corrupcion("La fecha está adelantada (fecha futura)", { true }) {
            it.copy(fecha = LocalDate.now().plusDays(7).format(EngineeringViewModel.FORMATO_FECHA))
        },
        Corrupcion("Falta la rúbrica del emisor", { true }) { it.copy(rubricaEmisor = false) },
        Corrupcion("Falta la firma de recepción", { it.tipo in TiposAsiento.requierenPlazo }) { it.copy(firmaRecepcion = false) },
        Corrupcion("Se omitió el plazo de cumplimiento", { it.tipo in TiposAsiento.requierenPlazo }) { it.copy(plazoDias = "") },
        Corrupcion("La instrucción contiene expresiones ambiguas y quedó incompleta", { true }) {
            it.copy(instruccion = it.instruccion.substringBefore(",").take(30) + " lo antes posible, etc.")
        },
        Corrupcion("El número de asiento no es correlativo", { true }) {
            it.copy(numero = ((it.numero.toIntOrNull() ?: 1) + 3).toString())
        },
        Corrupcion("Se omitió el ítem o frente de obra", { it.tipo in setOf(TiposAsiento.ORDEN, TiposAsiento.OBSERVACION, TiposAsiento.ORDEN_CAMBIO, TiposAsiento.PARALIZACION) }) { it.copy(itemReferencia = "") },
        Corrupcion("El asiento figura emitido por el Contratista siendo una orden", { it.tipo in TiposAsiento.exclusivosSupervisor }) {
            it.copy(emisor = Emisores.CONTRATISTA, nombreEmisor = "Ing. Superintendente", destinatario = "Supervisor de obra")
        },
        Corrupcion("No se identificó a quien firma", { true }) { it.copy(nombreEmisor = "") },
        Corrupcion("La Orden de Cambio crea un ítem nuevo", { it.tipo == TiposAsiento.ORDEN_CAMBIO }) { it.copy(creaNuevosItems = true) },
        Corrupcion("La Orden de Cambio supera el 5% del contrato", { it.tipo == TiposAsiento.ORDEN_CAMBIO }) {
            val monto = it.montoContrato.toDoubleOrNull() ?: 2_000_000.0
            it.copy(montoVariacion = EngineeringViewModel.formatoNumero(monto * 0.07))
        },
        Corrupcion("La Orden de Cambio no tiene informe legal", { it.tipo == TiposAsiento.ORDEN_CAMBIO }) { it.copy(informeLegal = false) },
        Corrupcion("La Orden de Cambio incrementa precios unitarios", { it.tipo == TiposAsiento.ORDEN_CAMBIO }) { it.copy(incrementaPreciosUnitarios = true) },
        Corrupcion("La solicitud de ampliación no tiene respaldo técnico", { it.tipo == TiposAsiento.AMPLIACION_PLAZO }) {
            it.copy(instruccion = "Se solicita ampliación de plazo de 30 días calendario debido a las lluvias que afectaron la obra durante el mes.")
        }
    )

    /** Aplica entre 2 y 4 inconsistencias compatibles con el tipo de asiento. */
    fun corromper(form: AsientoForm, random: Random): Pair<AsientoForm, List<String>> {
        val aplicables = corrupciones.filter { it.aplicable(form) }.shuffled(random)
        // Evita combinar dos corrupciones de fecha.
        val elegidas = mutableListOf<Corrupcion>()
        for (c in aplicables) {
            if (elegidas.size >= 2 + random.nextInt(3)) break
            if (c.descripcion.contains("fecha") && elegidas.any { it.descripcion.contains("fecha") }) continue
            elegidas += c
        }
        var resultado = form
        elegidas.forEach { resultado = it.aplicar(resultado) }
        return resultado to elegidas.map { it.descripcion }
    }
}

// ===========================================================================
// CRONOGRAMA DEL EXAMEN (precargado)
// ===========================================================================

object SemillaCronograma {
    val actividades: List<Actividad> = listOf(
        Actividad(1, "Instalación de faenas (urbano)", "GLB", 1.0, 2225.66, 1.0, 1, emptyList()),
        Actividad(2, "Replanteo – estructuras y edificaciones", "M2", 139.40, 13.15, 1.0, 1, listOf(1)),
        Actividad(3, "Extracción y retiro de árboles", "PZA", 3.0, 737.21, 1.0, 1, listOf(1)),
        Actividad(4, "Excavación suelo duro c/retroexc. + agotamiento", "M3", 489.17, 42.299998, 10.0, 1, listOf(2, 3)),
        Actividad(5, "Excavación en roca c/martillo hidráulico", "M3", 209.64, 58.060008, 2.0, 1, listOf(4)),
        Actividad(6, "Carga y transporte de material de excavación", "M3", 288.07, 27.189989, 2.0, 1, listOf(4)),
        Actividad(7, "Fundaciones de H°C° 50% P.D. H-21", "M3", 125.46, 665.769967, 33.0, 1, listOf(5)),
        Actividad(8, "Elevaciones de H°C° 50% P.D. H-21 + barbacanas", "M3", 247.52, 1013.219982, 107.0, 1, listOf(7)),
        Actividad(9, "Relleno y compactado c/equipo manual", "M3", 571.47, 61.360001, 45.0, 1, listOf(8)),
        Actividad(10, "Limpieza general", "GLB", 1.0, 1085.54, 1.0, 1, listOf(6, 9))
    )
}
