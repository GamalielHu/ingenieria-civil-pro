package com.gamaliel.ingcivilpro

import com.gamaliel.ingcivilpro.data.local.SeedData
import com.gamaliel.ingcivilpro.data.local.TiposAsiento
import com.gamaliel.ingcivilpro.domain.EvaluadorExpresiones
import com.gamaliel.ingcivilpro.domain.RutaCritica
import com.gamaliel.ingcivilpro.ui.viewmodel.AsientoForm
import com.gamaliel.ingcivilpro.ui.viewmodel.EngineeringViewModel
import com.gamaliel.ingcivilpro.ui.viewmodel.GeneradorInconsistencias
import com.gamaliel.ingcivilpro.ui.viewmodel.SemillaCronograma
import com.gamaliel.ingcivilpro.ui.viewmodel.Severidad
import com.gamaliel.ingcivilpro.ui.viewmodel.ValidadorLibroOrdenes
import java.time.LocalDate
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DominioTest {

    private fun eval(expr: String, vars: Map<String, Double> = emptyMap()) = EvaluadorExpresiones(vars).evaluar(expr)

    @Test
    fun evaluador_operaciones_basicas() {
        assertEquals(14.0, eval("2+3*4"), 1e-9)
        assertEquals(20.0, eval("(2+3)*4"), 1e-9)
        assertEquals(512.0, eval("2^3^2"), 1e-9)
        assertEquals(-4.0, eval("-2*2"), 1e-9)
        assertEquals(1.0, eval("sind(90)"), 1e-9)
    }

    @Test
    fun formulas_de_examen() {
        // Muro en T del examen: 15,675 m3
        assertEquals(15.675, eval("L*(t*H+B*e)", mapOf("L" to 5.0, "t" to 0.5, "H" to 4.27, "B" to 2.0, "e" to 0.5)), 1e-9)
        // IVA compensado 14% -> 16,28%
        assertEquals(16.28, eval("100*i/(100-i)", mapOf("i" to 14.0)), 0.005)
        // Inverso 16,96% -> 14,5%
        assertEquals(14.5, eval("100*c/(100+c)", mapOf("c" to 16.96)), 0.005)
        // Madera de encofrado -> 134,13 pie2/m3
        assertEquals(134.13, eval("(Atab+Alist)*10.76/V", mapOf("Atab" to 5.0, "Alist" to 0.6096, "V" to 0.45)), 0.05)
    }

    @Test
    fun todas_las_formulas_semilla_evaluan() {
        SeedData.formulas.forEach { f ->
            val vars = f.variables.zip(f.valoresPorDefecto).toMap()
            assertEquals("variables de ${f.nombre}", f.variables.toSet(), EvaluadorExpresiones.detectarVariables(f.expresion).toSet())
            val r = EvaluadorExpresiones(vars).evaluar(f.expresion)
            assertTrue("${f.nombre} = $r", r.isFinite())
        }
    }

    @Test
    fun preguntas_semilla_consistentes() {
        assertTrue(SeedData.preguntas.size >= 80)
        SeedData.preguntas.forEach { p ->
            assertTrue("Índice correcto fuera de rango: ${p.enunciado}", p.indiceCorrecto in p.opciones.indices)
            assertTrue(p.explicacion.isNotBlank() && p.fundamento.isNotBlank())
        }
    }

    @Test
    fun cronograma_examen() {
        val r = RutaCritica.calcular(SemillaCronograma.actividades)
        assertEquals(417437.26, r.montoTotal, 0.05)
        assertEquals(1 + 1 + 10 + 2 + 33 + 107 + 45 + 1, r.duracionTotal)
        assertTrue(8 in r.rutaCritica && 6 !in r.rutaCritica)
    }

    @Test
    fun plantillas_modelo_no_tienen_omisiones_criticas() {
        val hoy = LocalDate.now()
        SeedData.plantillasAsiento.forEach { p ->
            val form = AsientoForm(
                numero = "1", fecha = hoy.format(EngineeringViewModel.FORMATO_FECHA), hora = "09:00",
                emisor = p.emisor, nombreEmisor = p.nombreEmisor, destinatario = p.destinatario, tipo = p.tipoAsiento,
                itemReferencia = p.itemReferencia, instruccion = p.instruccion, plazoDias = p.plazoDias?.toString() ?: "",
                rubricaEmisor = true, firmaRecepcion = true,
                montoContrato = p.montoContrato?.toString() ?: "", montoVariacion = p.montoVariacion?.toString() ?: "",
                informeTecnico = p.tipoAsiento == TiposAsiento.ORDEN_CAMBIO,
                informeLegal = p.tipoAsiento == TiposAsiento.ORDEN_CAMBIO,
                aprobadaPorFiscal = p.tipoAsiento == TiposAsiento.ORDEN_CAMBIO
            )
            val criticas = ValidadorLibroOrdenes.validar(form, null, hoy).filter { it.severidad == Severidad.CRITICA }
            assertTrue("${p.titulo}: $criticas", criticas.isEmpty())

            repeat(5) { semilla ->
                val (corrupto, problemas) = GeneradorInconsistencias.corromper(form, Random(semilla))
                val c = ValidadorLibroOrdenes.validar(corrupto, null, hoy).count { it.severidad == Severidad.CRITICA }
                assertTrue("${p.titulo} con $problemas no detectado", problemas.isNotEmpty() && c > 0)
            }
        }
    }

    @Test
    fun orden_de_cambio_fuera_de_tope() {
        val hoy = LocalDate.now()
        val form = AsientoForm(
            numero = "1", fecha = hoy.format(EngineeringViewModel.FORMATO_FECHA), hora = "10:00",
            nombreEmisor = "Ing. Supervisor", destinatario = "Superintendente", tipo = TiposAsiento.ORDEN_CAMBIO,
            itemReferencia = "Ítem 4", instruccion = "Se emite Orden de Cambio para ejecutar volúmenes adicionales del ítem 4 conforme informe técnico; Supervisión verificará cómputos.",
            plazoDias = "10", rubricaEmisor = true, firmaRecepcion = true,
            montoContrato = "2000000", montoVariacion = "50000", acumuladoOC = "60000",
            informeTecnico = true, informeLegal = true, aprobadaPorFiscal = true
        )
        val h = ValidadorLibroOrdenes.validar(form, null, hoy)
        assertTrue(h.any { it.severidad == Severidad.CRITICA && it.campo == "Tope OC" })
    }
}
