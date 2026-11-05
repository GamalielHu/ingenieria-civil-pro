package com.gamaliel.ingcivilpro.data.local

import com.gamaliel.ingcivilpro.data.local.SeedData.caso
import com.gamaliel.ingcivilpro.data.local.SeedData.om
import com.gamaliel.ingcivilpro.data.local.SeedData.vf

/** Anexo 1 del DS 0181: Imperativos para la conducta ética del servidor público en contrataciones. */
object ContenidoEtica {

    private const val ANEXO = "Anexo 1 – Imperativos Éticos"
    private const val T = TemasExtra.ETICA

    val preguntas: List<PreguntaEntity> = listOf(
        om(
            T, "¿Cuáles son los seis valores fundamentales de los Imperativos Éticos?",
            listOf(
                "Responsabilidad, Transparencia, Integridad, Justicia, Verdad y Respeto a las personas",
                "Solidaridad, Participación, Control Social, Buena Fe, Economía y Eficacia",
                "Honestidad, Lealtad, Puntualidad, Disciplina, Orden y Respeto",
                "Legalidad, Publicidad, Igualdad, Eficiencia, Economía y Transparencia"
            ), 0,
            "Art. 7: Responsabilidad, Transparencia, Integridad, Justicia, Verdad y Respeto a las personas. No confundir con los 11 principios del Art. 3 de las NB-SABS.",
            "$ANEXO, Art. 7", "Anexo 1 DS 0181"
        ),
        om(
            T, "'La constante disposición de no vulnerar, por ningún motivo, los valores y principios éticos' define el valor:",
            listOf("Responsabilidad", "Integridad", "Justicia", "Verdad"), 1,
            "Integridad (Art. 7 c).", "$ANEXO, Art. 7 c)", "Anexo 1 DS 0181"
        ),
        om(
            T, "'La constante voluntad de dar a cada cual lo que le es debido' es:",
            listOf("Justicia", "Respeto", "Integridad", "Transparencia"), 0,
            "Justicia (Art. 7 d).", "$ANEXO, Art. 7 d)", "Anexo 1 DS 0181"
        ),
        om(
            T, "'La conformidad de lo que uno piensa o comunica con la realidad de los hechos' es:",
            listOf("Transparencia", "Verdad", "Integridad", "Responsabilidad"), 1,
            "Verdad (Art. 7 e).", "$ANEXO, Art. 7 e)", "Anexo 1 DS 0181"
        ),
        om(
            T, "'Disposición de actuar con claridad, sin esconder lo que debe ser conocido' define:",
            listOf("Verdad", "Transparencia", "Justicia", "Respeto a las personas"), 1,
            "Transparencia (Art. 7 b).", "$ANEXO, Art. 7 b)", "Anexo 1 DS 0181"
        ),
        om(
            T, "'Actuar conforme a las normas y funciones del cargo, representar las inadecuadas y responder por las consecuencias de los propios actos' es:",
            listOf("Responsabilidad", "Integridad", "Justicia", "Verdad"), 0,
            "Responsabilidad (Art. 7 a).", "$ANEXO, Art. 7 a)", "Anexo 1 DS 0181"
        ),
        om(
            T, "La desaprobación de pares, superiores, inferiores y ciudadanos que afecta negativamente la reputación personal se denomina:",
            listOf("Sanción administrativa", "Sanción moral", "Responsabilidad civil", "Norma ética"), 1,
            "Sanción moral (Art. 5 d).", "$ANEXO, Art. 5 d)", "Anexo 1 DS 0181"
        ),
        om(
            T, "La diferencia entre 'labor directa' y 'labor indirecta' es:",
            listOf(
                "Directa: servidores con contratación como ocupación única o principal y permanente; indirecta: quienes asumen esas actividades de forma temporal o puntual",
                "Directa: personal de planta; indirecta: consultores",
                "Directa: MAE; indirecta: RPC",
                "No existe tal distinción"
            ), 0,
            "Art. 5 e) y f).", "$ANEXO, Art. 5 e) f)", "Anexo 1 DS 0181"
        ),
        om(
            T, "¿Qué instancia es responsable de la implantación, difusión y capacitación de los Imperativos?",
            listOf(
                "La Contraloría General del Estado",
                "La Dirección General de Normas de Gestión Pública (DGNGP) del Ministerio de Economía y Finanzas Públicas",
                "La MAE de cada entidad",
                "El Ministerio de Justicia"
            ), 1,
            "La DGNGP del MEFP (Arts. 5 h, 15 y 16).", "$ANEXO, Arts. 15-16", "Anexo 1 DS 0181"
        ),
        om(
            T, "¿Cuál NO es función para la promoción de la ética institucional (Art. 17)?",
            listOf("Premiación anual", "Recepción y derivación de denuncias", "Atención de consultas de servidores", "Sancionar penalmente a los infractores"), 3,
            "Funciones: capacitación, comunicación, actualización de contenidos, consultas, premiación anual, promoción de la conducta moral, recepción y derivación de denuncias. Las faltas se tratan en las instancias administrativas y legales que correspondan (Art. 4).",
            "$ANEXO, Arts. 4 y 17", "Anexo 1 DS 0181"
        ),
        vf(
            T, "Los Imperativos Éticos son de aplicación obligatoria para todos los servidores públicos que intervienen directa o indirectamente en contrataciones.",
            true, "VERDADERO (Art. 3 del Anexo 1, entidades de los Arts. 3 y 4 de la Ley 1178).",
            "$ANEXO, Art. 3", "Anexo 1 DS 0181"
        ),
        caso(
            T, "Un proponente invita a almorzar al presidente de la Comisión de Calificación 'para explicarle mejor su propuesta'. ¿Qué debe hacer el servidor?",
            listOf(
                "Aceptar, siempre que no hablen del precio",
                "Rechazar con cortesía y firmeza el intento de influir y reportarlo por escrito a su superior",
                "Aceptar y luego excusarse del proceso",
                "Ignorarlo sin informar"
            ), 1,
            "Integridad, Art. 11 f): rechazar con cortesía y firmeza cualquier intento de influir y reportarlo por escrito. Además, Art. 40 h) e i) de las NB-SABS prohíbe relaciones no escritas con proponentes y aceptar beneficios.",
            "$ANEXO, Art. 11 f); NB-SABS Art. 40", "Anexo 1 DS 0181"
        ),
        caso(
            T, "Un evaluador descubre que su primo es socio de una empresa proponente. Según los principios del valor Integridad debe:",
            listOf(
                "Evaluar con mayor rigor a esa empresa",
                "Solicitar su excusa por conflicto de intereses",
                "Pedir al primo que retire la propuesta",
                "No decir nada si no lo favorece"
            ), 1,
            "Art. 11 c) del Anexo: solicitar excusa ante conflicto de intereses. En las NB-SABS: Art. 41 I b) (parentesco hasta 3° de consanguinidad).",
            "$ANEXO, Art. 11 c); NB-SABS Art. 41", "Anexo 1 DS 0181"
        ),
        om(
            T, "'Emplear la información disponible únicamente con el fin específico para el cual fue obtenida' es un principio del valor:",
            listOf("Verdad", "Transparencia", "Justicia", "Respeto"), 1,
            "Transparencia, Art. 10 a).", "$ANEXO, Art. 10 a)", "Anexo 1 DS 0181"
        ),
        om(
            T, "'Dar el mismo trato a todos los concurrentes a los procesos de contratación' es un principio del valor:",
            listOf("Justicia", "Integridad", "Respeto a las personas", "Responsabilidad"), 0,
            "Justicia, Art. 12 f).", "$ANEXO, Art. 12 f)", "Anexo 1 DS 0181"
        ),
        om(
            T, "'En pliegos, convocatorias e informes establecer cláusulas claras que todos puedan comprender' corresponde al valor:",
            listOf("Verdad", "Transparencia", "Justicia", "Responsabilidad"), 0,
            "Verdad, Art. 13 d). También Art. 13 e): proteger la información confidencial.", "$ANEXO, Art. 13 d)", "Anexo 1 DS 0181"
        ),
        om(
            T, "'Evitar participar en rumores que puedan dañar la imagen de las personas' corresponde al valor:",
            listOf("Integridad", "Respeto a las personas", "Verdad", "Justicia"), 1,
            "Respeto a las personas, Art. 14 d).", "$ANEXO, Art. 14 d)", "Anexo 1 DS 0181"
        ),
        om(
            T, "Ante situaciones de duda sobre la acción a tomar, según el valor Responsabilidad el servidor:",
            listOf(
                "Decide según su criterio personal",
                "Consulta con quien corresponda antes de decidir",
                "Posterga la decisión indefinidamente",
                "Delega al proponente"
            ), 1,
            "Art. 9 c).", "$ANEXO, Art. 9 c)", "Anexo 1 DS 0181"
        )
    )

    val lecciones: List<LeccionEntity> = listOf(
        LeccionEntity(
            modulo = Modulos.NORMATIVA, orden = 20,
            titulo = "Ética: definiciones y alcance (Anexo 1, Arts. 1-6)",
            contenido = """
• Objetivo: promover y regular la conducta ética de los servidores públicos en contrataciones (Art. 2).
• Ámbito: obligatorio para todos los servidores que intervienen directa o indirectamente en contrataciones (Art. 3).
• Las faltas se tratan en las instancias administrativas y legales que correspondan (Art. 4).

Definiciones (Art. 5):
a) Valor ético: cualidad humana objetiva y positiva; permanente.
b) Principio ético: manera de ser y actuar conforme a valores.
c) Norma ética: regla de conducta para asegurar valores y principios.
d) Sanción moral: desaprobación de pares, superiores, inferiores y ciudadanos.
e) Labor directa: contratación como ocupación única o principal, permanente.
f) Labor indirecta: actividades de contratación temporales o puntuales.
g) Reserva: mantener confidencial la identidad (denunciante).
h) Promoción de la ética: a cargo de la DGNGP del MEFP.
            """.trimIndent(),
            fundamento = "Anexo 1, Arts. 1-6"
        ),
        LeccionEntity(
            modulo = Modulos.NORMATIVA, orden = 21,
            titulo = "Los 6 valores éticos (Art. 7) y sus principios",
            contenido = """
Mnemotecnia: 'RE-TRA-IN-JUS-VER-RES' (Responsabilidad, Transparencia, Integridad, Justicia, Verdad, Respeto).

1. RESPONSABILIDAD (Art. 9): aplicar leyes; en duda, consultar; dar cuenta a superiores y sociedad; responder por dependientes; buscar el máximo beneficio del bien común.
2. TRANSPARENCIA (Art. 10): usar la información sólo para su fin; no retener ni ocultar lo que debe conocerse; decisiones con conocimiento de todas las partes.
3. INTEGRIDAD (Art. 11): independencia de criterio; EXCUSARSE ante conflicto de intereses; RECHAZAR con cortesía y firmeza intentos de influencia y REPORTARLOS POR ESCRITO; denunciar; resguardar la vida social para reducir riesgo de soborno.
4. JUSTICIA (Art. 12): aplicación imparcial; objetividad; equidad; mismo trato a todos los concurrentes.
5. VERDAD (Art. 13): información fidedigna y oportuna; lenguaje claro; cláusulas claras en pliegos e informes; proteger información confidencial.
6. RESPETO A LAS PERSONAS (Art. 14): trato igualitario sin discriminación; no participar en rumores; respetar la libertad de opinión; evitar el acoso; intervenir ante discriminación.

Responsabilidad de aplicación (Arts. 15-17): DGNGP – capacitación, consultas, premiación anual, recepción y derivación de denuncias.

En un caso práctico: identifica la conducta → asócialo al valor → cita el artículo del Anexo y, si corresponde, la prohibición del Art. 40 o la excusa del Art. 41 de las NB-SABS.
            """.trimIndent(),
            fundamento = "Anexo 1, Arts. 7-17"
        )
    )
}
