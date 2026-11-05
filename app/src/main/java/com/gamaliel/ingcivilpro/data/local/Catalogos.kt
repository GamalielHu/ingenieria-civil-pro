package com.gamaliel.ingcivilpro.data.local

object Temas {
    const val MODIFICACIONES = "Modificaciones al contrato (Art. 89)"
    const val GARANTIAS = "Garantías y anticipo"
    const val MODALIDADES = "Modalidades, cuantías y plazos"
    const val CONTRATO = "Contrato y protocolización"
    const val IMPEDIMENTOS = "Impedimentos y conflicto de intereses"
    const val RECEPCION = "Recepción y supervisión"
    const val LIBRO = "Libro de Órdenes"
    const val COMPUTOS = "Cómputos y rendimientos"
    const val PRECIOS = "Precios unitarios e impuestos"
    const val CRONOGRAMA = "Cronograma y ruta crítica"
    const val IMPUGNACION = "Impugnación"
    const val SABS = "Sistema SABS"
}

object TiposPregunta {
    const val OPCION_MULTIPLE = "OPCION_MULTIPLE"
    const val VERDADERO_FALSO = "VERDADERO_FALSO"
    const val CASO = "CASO_ESTUDIO"
}

object Modulos {
    const val NORMATIVA = "NORMATIVA"
    const val LIBRO = "LIBRO"
    const val COMPUTOS = "COMPUTOS"
}

object Emisores {
    const val SUPERVISOR = "SUPERVISOR"
    const val CONTRATISTA = "CONTRATISTA"
    val todos = listOf(SUPERVISOR, CONTRATISTA)
    fun etiqueta(e: String) = when (e) {
        SUPERVISOR -> "Supervisor"
        CONTRATISTA -> "Contratista (Superintendente)"
        else -> e
    }
}

object TiposAsiento {
    const val ORDEN = "ORDEN"
    const val SOLICITUD = "SOLICITUD"
    const val OBSERVACION = "OBSERVACION"
    const val ORDEN_CAMBIO = "ORDEN_CAMBIO"
    const val AMPLIACION_PLAZO = "AMPLIACION_PLAZO"
    const val PARALIZACION = "PARALIZACION"
    const val APROBACION = "APROBACION"

    val todos = listOf(ORDEN, SOLICITUD, OBSERVACION, ORDEN_CAMBIO, AMPLIACION_PLAZO, PARALIZACION, APROBACION)

    /** Tipos que sólo puede emitir el Supervisor. */
    val exclusivosSupervisor = setOf(ORDEN, OBSERVACION, ORDEN_CAMBIO, APROBACION)

    /** Tipos que imponen una obligación con plazo de cumplimiento. */
    val requierenPlazo = setOf(ORDEN, OBSERVACION, ORDEN_CAMBIO, PARALIZACION)

    fun etiqueta(t: String) = when (t) {
        ORDEN -> "Orden / Instrucción"
        SOLICITUD -> "Solicitud / Comunicación"
        OBSERVACION -> "Observación de calidad"
        ORDEN_CAMBIO -> "Orden de Cambio"
        AMPLIACION_PLAZO -> "Solicitud de ampliación de plazo"
        PARALIZACION -> "Paralización / Reanudación"
        APROBACION -> "Aprobación (planilla / certificado)"
        else -> t
    }
}

object Modalidades {
    const val LICITACION = "LICITACION_PUBLICA"
    const val ANPE = "ANPE"
    fun etiqueta(m: String) = if (m == LICITACION) "Licitación Pública" else "ANPE"
}

object TemasExtra {
    const val PRINCIPIOS = "Principios del SABS (Art. 3)"
    const val PROCESO = "Proceso de contratación (ANPE / LP)"
    const val RESPONSABLES = "Responsables y comisiones"
    const val PROHIBICIONES = "Prohibiciones y excusas"
    const val EXCEPCION = "Excepción, desierta y cancelación"
    const val ETICA = "Ética del servidor público (Anexo 1)"
}
