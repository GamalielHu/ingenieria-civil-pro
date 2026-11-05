package com.gamaliel.ingcivilpro.domain

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

/**
 * Evaluador de expresiones aritméticas por descenso recursivo.
 * Soporta + - * / ^, paréntesis, variables y funciones:
 * sqrt, abs, ceil, floor, round, min, max, sin/cos/tan (radianes),
 * sind/cosd/tand (grados) y las constantes pi y e.
 */
class EvaluadorExpresiones(private val variables: Map<String, Double>) {

    private lateinit var texto: String
    private var pos = 0

    fun evaluar(expresion: String): Double {
        texto = expresion.replace(" ", "")
        pos = 0
        if (texto.isEmpty()) throw IllegalArgumentException("La expresión está vacía")
        val valor = sumaResta()
        if (pos < texto.length) {
            throw IllegalArgumentException("Carácter inesperado '${texto[pos]}' en la posición ${pos + 1}")
        }
        if (valor.isNaN() || valor.isInfinite()) {
            throw IllegalArgumentException("Resultado no válido (división entre cero o raíz negativa)")
        }
        return valor
    }

    private fun sumaResta(): Double {
        var valor = multDiv()
        while (pos < texto.length) {
            when (texto[pos]) {
                '+' -> { pos++; valor += multDiv() }
                '-' -> { pos++; valor -= multDiv() }
                else -> return valor
            }
        }
        return valor
    }

    private fun multDiv(): Double {
        var valor = potencia()
        while (pos < texto.length) {
            when (texto[pos]) {
                '*' -> { pos++; valor *= potencia() }
                '/' -> {
                    pos++
                    val divisor = potencia()
                    if (divisor == 0.0) throw IllegalArgumentException("División entre cero")
                    valor /= divisor
                }
                else -> return valor
            }
        }
        return valor
    }

    private fun potencia(): Double {
        val base = unario()
        if (pos < texto.length && texto[pos] == '^') {
            pos++
            return base.pow(potencia())
        }
        return base
    }

    private fun unario(): Double {
        if (pos < texto.length && texto[pos] == '-') { pos++; return -unario() }
        if (pos < texto.length && texto[pos] == '+') { pos++; return unario() }
        return primario()
    }

    private fun primario(): Double {
        if (pos >= texto.length) throw IllegalArgumentException("La expresión termina de forma incompleta")
        val c = texto[pos]
        return when {
            c == '(' -> {
                pos++
                val v = sumaResta()
                esperar(')')
                v
            }
            c.isDigit() || c == '.' -> numero()
            c.isLetter() || c == '_' -> identificador()
            else -> throw IllegalArgumentException("Carácter inesperado '$c' en la posición ${pos + 1}")
        }
    }

    private fun numero(): Double {
        val inicio = pos
        while (pos < texto.length && (texto[pos].isDigit() || texto[pos] == '.')) pos++
        return texto.substring(inicio, pos).toDoubleOrNull()
            ?: throw IllegalArgumentException("Número mal escrito: ${texto.substring(inicio, pos)}")
    }

    private fun identificador(): Double {
        val inicio = pos
        while (pos < texto.length && (texto[pos].isLetterOrDigit() || texto[pos] == '_')) pos++
        val nombre = texto.substring(inicio, pos)
        if (pos < texto.length && texto[pos] == '(') {
            pos++
            val args = mutableListOf(sumaResta())
            while (pos < texto.length && (texto[pos] == ',' || texto[pos] == ';')) {
                pos++
                args.add(sumaResta())
            }
            esperar(')')
            return funcion(nombre, args)
        }
        return when (nombre) {
            "pi", "PI" -> PI
            "e" -> if (variables.containsKey("e")) variables.getValue("e") else Math.E
            else -> variables[nombre]
                ?: throw IllegalArgumentException("Variable no definida: $nombre")
        }
    }

    private fun funcion(nombre: String, a: List<Double>): Double {
        fun requiere(n: Int) {
            if (a.size != n) throw IllegalArgumentException("$nombre() requiere $n argumento(s)")
        }
        return when (nombre.lowercase()) {
            "sqrt", "raiz" -> {
                requiere(1)
                if (a[0] < 0) throw IllegalArgumentException("Raíz de número negativo")
                sqrt(a[0])
            }
            "abs" -> { requiere(1); abs(a[0]) }
            "ceil" -> { requiere(1); ceil(a[0]) }
            "floor" -> { requiere(1); floor(a[0]) }
            "round" -> { requiere(1); round(a[0]) }
            "sin" -> { requiere(1); sin(a[0]) }
            "cos" -> { requiere(1); cos(a[0]) }
            "tan" -> { requiere(1); tan(a[0]) }
            "sind" -> { requiere(1); sin(Math.toRadians(a[0])) }
            "cosd" -> { requiere(1); cos(Math.toRadians(a[0])) }
            "tand" -> { requiere(1); tan(Math.toRadians(a[0])) }
            "min" -> a.min()
            "max" -> a.max()
            else -> throw IllegalArgumentException("Función desconocida: $nombre")
        }
    }

    private fun esperar(c: Char) {
        if (pos >= texto.length || texto[pos] != c) {
            throw IllegalArgumentException("Falta '$c' en la expresión")
        }
        pos++
    }

    companion object {
        private val PALABRAS_RESERVADAS = setOf(
            "pi", "PI", "sqrt", "raiz", "abs", "ceil", "floor", "round",
            "sin", "cos", "tan", "sind", "cosd", "tand", "min", "max"
        )

        /** Extrae los nombres de variables usados en una expresión (en orden de aparición). */
        fun detectarVariables(expresion: String): List<String> {
            val regex = Regex("[A-Za-z_][A-Za-z0-9_]*")
            val sinEspacios = expresion.replace(" ", "")
            return regex.findAll(sinEspacios)
                .filter { m ->
                    val fin = m.range.last + 1
                    val esFuncion = fin < sinEspacios.length && sinEspacios[fin] == '('
                    !esFuncion && m.value !in PALABRAS_RESERVADAS
                }
                .map { it.value }
                .distinct()
                .toList()
        }
    }
}
