package com.gamaliel.ingcivilpro.domain

import kotlin.math.ceil

/** Actividad de cronograma (método de la ruta crítica, CPM). */
data class Actividad(
    val id: Int,
    val nombre: String,
    val unidad: String,
    val volumen: Double,
    val precioUnitario: Double,
    val diasBase: Double,
    val cuadrillas: Int,
    val predecesoras: List<Int>
) {
    val parcial: Double get() = volumen * precioUnitario
    val duracion: Int get() = ceil(diasBase / cuadrillas.coerceAtLeast(1)).toInt().coerceAtLeast(0)
}

data class ResultadoActividad(
    val actividad: Actividad,
    val inicioTemprano: Int,
    val finTemprano: Int,
    val inicioTardio: Int,
    val finTardio: Int
) {
    val holgura: Int get() = inicioTardio - inicioTemprano
    val esCritica: Boolean get() = holgura == 0
}

data class ResultadoCpm(
    val actividades: List<ResultadoActividad>,
    val duracionTotal: Int,
    val rutaCritica: List<Int>,
    val montoTotal: Double
)

object RutaCritica {

    /** Calcula pasada adelante/atrás. Lanza IllegalArgumentException si hay ciclos o predecesoras inexistentes. */
    fun calcular(actividades: List<Actividad>): ResultadoCpm {
        if (actividades.isEmpty()) return ResultadoCpm(emptyList(), 0, emptyList(), 0.0)
        val porId = actividades.associateBy { it.id }
        actividades.forEach { a ->
            a.predecesoras.forEach { p ->
                if (p !in porId) throw IllegalArgumentException("La actividad ${a.id} tiene la predecesora $p que no existe")
                if (p == a.id) throw IllegalArgumentException("La actividad ${a.id} no puede precederse a sí misma")
            }
        }
        val orden = ordenTopologico(actividades)

        val inicioTemprano = mutableMapOf<Int, Int>()
        val finTemprano = mutableMapOf<Int, Int>()
        for (id in orden) {
            val a = porId.getValue(id)
            val es = a.predecesoras.maxOfOrNull { finTemprano.getValue(it) } ?: 0
            inicioTemprano[id] = es
            finTemprano[id] = es + a.duracion
        }
        val duracionTotal = finTemprano.values.max()

        val sucesoras = actividades.associate { a -> a.id to actividades.filter { a.id in it.predecesoras }.map { it.id } }
        val finTardio = mutableMapOf<Int, Int>()
        val inicioTardio = mutableMapOf<Int, Int>()
        for (id in orden.reversed()) {
            val a = porId.getValue(id)
            val lf = sucesoras.getValue(id).minOfOrNull { inicioTardio.getValue(it) } ?: duracionTotal
            finTardio[id] = lf
            inicioTardio[id] = lf - a.duracion
        }

        val resultados = orden.map { id ->
            ResultadoActividad(
                actividad = porId.getValue(id),
                inicioTemprano = inicioTemprano.getValue(id),
                finTemprano = finTemprano.getValue(id),
                inicioTardio = inicioTardio.getValue(id),
                finTardio = finTardio.getValue(id)
            )
        }
        return ResultadoCpm(
            actividades = resultados,
            duracionTotal = duracionTotal,
            rutaCritica = resultados.filter { it.esCritica }.map { it.actividad.id },
            montoTotal = actividades.sumOf { it.parcial }
        )
    }

    private fun ordenTopologico(actividades: List<Actividad>): List<Int> {
        val pendientes = actividades.associate { it.id to it.predecesoras.toMutableSet() }.toMutableMap()
        val resultado = mutableListOf<Int>()
        while (pendientes.isNotEmpty()) {
            val listas = pendientes.filter { (_, preds) -> preds.all { it in resultado } }.keys.sorted()
            if (listas.isEmpty()) {
                throw IllegalArgumentException("Hay un ciclo entre las predecesoras de las actividades ${pendientes.keys.sorted()}")
            }
            listas.forEach { resultado.add(it); pendientes.remove(it) }
        }
        return resultado
    }
}
