package com.gamaliel.ingcivilpro.data.local

import com.gamaliel.ingcivilpro.data.local.SeedData.caso
import com.gamaliel.ingcivilpro.data.local.SeedData.om
import com.gamaliel.ingcivilpro.data.local.SeedData.vf

/** Subsistema de Contratación de Bienes y Servicios (NB-SABS DS 0181, Arts. 3 a 106). */
object ContenidoContrataciones {

    val preguntas: List<PreguntaEntity> = listOf(
        // ---- Principios ----
        om(
            TemasExtra.PRINCIPIOS,
            "'Se presume el correcto y ético actuar de los servidores públicos y proponentes' corresponde al principio de:",
            listOf("Transparencia", "Buena Fe", "Responsabilidad", "Equidad"), 1,
            "Buena Fe (Art. 3 d): se presume el correcto y ético actuar de servidores públicos y proponentes.",
            "Art. 3 d)"
        ),
        om(
            TemasExtra.PRINCIPIOS,
            "'Los procesos deben realizarse oportunamente, en tiempos óptimos y con los menores costos posibles' es el principio de:",
            listOf("Economía", "Eficacia", "Eficiencia", "Solidaridad"), 2,
            "Eficiencia = oportunidad, tiempo óptimo y menor costo. Economía = celeridad y ahorro de recursos. Eficacia = alcanzar objetivos y resultados programados.",
            "Art. 3 e), f), g)"
        ),
        om(
            TemasExtra.PRINCIPIOS,
            "El principio que garantiza la más amplia concurrencia de proponentes mediante mecanismos de publicidad es:",
            listOf("Libre Participación", "Participación", "Control Social", "Equidad"), 0,
            "Libre Participación (Art. 3 i): libre participación y más amplia concurrencia, a través de publicidad, para obtener las mejores condiciones de precio y calidad.",
            "Art. 3 i)"
        ),
        om(
            TemasExtra.PRINCIPIOS,
            "¿Cuántos principios enumera el Art. 3 de las NB-SABS?",
            listOf("6", "9", "11", "14"), 2,
            "Once: Solidaridad, Participación, Control Social, Buena Fe, Economía, Eficacia, Eficiencia, Equidad, Libre Participación, Responsabilidad y Transparencia.",
            "Art. 3"
        ),
        vf(
            TemasExtra.PRINCIPIOS,
            "Según el principio de Transparencia, los actos, documentos e información de los procesos de contratación son públicos.",
            true,
            "VERDADERO (Art. 3 k).",
            "Art. 3 k)"
        ),

        // ---- Proceso de contratación ----
        om(
            TemasExtra.PROCESO,
            "En ANPE, para contrataciones mayores a Bs 50.000 hasta Bs 200.000, el plazo mínimo para presentar propuestas es:",
            listOf("2 días", "4 días", "8 días", "15 días"), 1,
            "ANPE: > 50.000 a 200.000 → mínimo 4 días; > 200.000 a 1.000.000 → mínimo 8 días (días hábiles).",
            "Art. 57 I a)"
        ),
        om(
            TemasExtra.PROCESO,
            "En ANPE, para contrataciones mayores a Bs 200.000 hasta Bs 1.000.000, el plazo mínimo para presentar propuestas es:",
            listOf("4 días", "8 días", "10 días", "15 días"), 1,
            "Mínimo ocho (8) días.",
            "Art. 57 I b)"
        ),
        om(
            TemasExtra.PROCESO,
            "En Licitación Pública Nacional, el plazo mínimo para presentar propuestas es:",
            listOf("8 días", "10 días", "15 días", "20 días"), 2,
            "LP Nacional: 15 días. LP Internacional: 20 días (obras internacionales: 15 días).",
            "Art. 61 I"
        ),
        om(
            TemasExtra.PROCESO,
            "La Convocatoria Pública Internacional se aplica para contrataciones mayores a:",
            listOf("Bs 1.000.000", "Bs 20.000.000", "Bs 70.000.000", "Bs 100.000.000"), 2,
            "Nacional: > Bs 1.000.000 hasta Bs 70.000.000. Internacional: > Bs 70.000.000 (o menores si el objeto requiere difusión internacional).",
            "Art. 60"
        ),
        om(
            TemasExtra.PROCESO,
            "El plazo de presentación de propuestas en Licitación Pública puede ampliarse excepcionalmente:",
            listOf(
                "Las veces que sea necesario",
                "Por única vez, hasta 10 días adicionales, mediante Resolución expresa, por fuerza mayor, caso fortuito o enmiendas al DBC",
                "Hasta 30 días calendario por decisión del RPC",
                "No puede ampliarse"
            ), 1,
            "Por única vez, máximo diez (10) días adicionales, mediante Resolución expresa, ajustando y publicando el cronograma en el SICOES.",
            "Art. 61 III"
        ),
        om(
            TemasExtra.PROCESO,
            "Para contrataciones mayores a Bs 50.000, los plazos se computan a partir de:",
            listOf(
                "La fecha de la Resolución de inicio",
                "El día hábil siguiente a la publicación de la convocatoria en el SICOES",
                "La aprobación del DBC",
                "La reunión de aclaración"
            ), 1,
            "Desde el día siguiente hábil de la publicación de la convocatoria en el SICOES.",
            "Art. 50"
        ),
        om(
            TemasExtra.PROCESO,
            "En ANPE, si sólo se recibió UNA propuesta:",
            listOf(
                "Se declara desierto",
                "Se amplía el plazo automáticamente",
                "Se procede a la evaluación igualmente",
                "Se convierte en Contratación Menor"
            ), 2,
            "Se procede a la evaluación aun cuando se hubiese recibido una sola cotización o propuesta.",
            "Art. 56"
        ),
        om(
            TemasExtra.PROCESO,
            "¿Cuál actividad existe en Licitación Pública pero NO en el listado del proceso ANPE?",
            listOf(
                "Apertura pública de propuestas",
                "Aprobación del DBC con enmiendas y concertación de mejores condiciones técnicas",
                "Notificación",
                "Recepción"
            ), 1,
            "En LP (Art. 62) se incluyen la Reunión de Aclaración obligatoria, la aprobación del DBC con enmiendas y la concertación de mejores condiciones técnicas.",
            "Arts. 58 y 62"
        ),
        om(
            TemasExtra.PROCESO,
            "La Resolución de Adjudicación o Declaratoria Desierta en LP debe contener mínimamente:",
            listOf(
                "Sólo el nombre del adjudicado",
                "Nómina de participantes y precios ofertados, resultados de la calificación y causales de rechazo/descalificación",
                "El contrato firmado",
                "Las garantías presentadas"
            ), 1,
            "Nómina de participantes y precios, resultados de la calificación y causales de rechazo y/o descalificación si existieran.",
            "Art. 62 i)"
        ),
        om(
            TemasExtra.PROCESO,
            "Las notificaciones de resoluciones impugnables se realizan en un plazo máximo de:",
            listOf("1 día", "2 días", "3 días", "5 días"), 1,
            "Dos (2) días desde la emisión, vía correo electrónico y/o fax y SICOES.",
            "Art. 51 I"
        ),
        om(
            TemasExtra.PROCESO,
            "Los métodos de selección y adjudicación previstos son:",
            listOf(
                "Precio más bajo y sorteo",
                "Calidad-Propuesta Técnica y Costo; Calidad; Presupuesto Fijo; Menor Costo; Precio Evaluado Más Bajo",
                "Sólo Precio Evaluado Más Bajo",
                "Calidad y Experiencia"
            ), 1,
            "Cinco métodos, según lo establezca el DBC.",
            "Art. 23"
        ),
        om(
            TemasExtra.PROCESO,
            "En obras con convocatoria internacional, la adjudicación debe realizarse por:",
            listOf("Ítems", "Lotes", "Tramos (salvo imposibilidad técnica)", "Paquetes"), 2,
            "Se adjudica por tramos, salvo que por las características técnicas no pueda ejecutarse así.",
            "Art. 24 I"
        ),

        // ---- Desierta / cancelación / excepción ----
        om(
            TemasExtra.EXCEPCION,
            "¿Cuál NO es causal de declaratoria desierta?",
            listOf(
                "No se recibió ninguna propuesta",
                "Todas las propuestas superaron el Precio Referencial",
                "Se recibió una sola propuesta que cumple el DBC",
                "Ninguna propuesta cumplió lo especificado en el DBC"
            ), 2,
            "Una sola propuesta válida se evalúa y puede adjudicarse. Causales: ninguna propuesta, todas sobre el PR, ninguna cumple el DBC, o el adjudicado desiste sin otras calificadas.",
            "Art. 27 I"
        ),
        om(
            TemasExtra.EXCEPCION,
            "El proceso de contratación puede cancelarse, anularse o suspenderse hasta antes de:",
            listOf(
                "La apertura de propuestas",
                "La adjudicación",
                "La suscripción del contrato u orden de compra/servicio",
                "La recepción definitiva"
            ), 2,
            "Hasta antes de la suscripción del contrato o emisión de la orden de compra/servicio, mediante Resolución expresa, técnica y legalmente motivada.",
            "Art. 28 I"
        ),
        caso(
            TemasExtra.EXCEPCION,
            "Después de publicar la convocatoria se detecta un error en el DBC publicado. ¿Qué corresponde?",
            listOf(
                "Cancelación del proceso",
                "Suspensión hasta la próxima gestión",
                "Anulación hasta el vicio más antiguo y nueva publicación de la convocatoria en el SICOES",
                "Continuar y corregir en el contrato"
            ), 2,
            "El error en el DBC publicado es causal de anulación (Art. 28 IV b); cuando la anulación es por error en el DBC, se debe publicar nuevamente la convocatoria.",
            "Art. 28 IV"
        ),
        om(
            TemasExtra.EXCEPCION,
            "La suspensión del proceso procede cuando:",
            listOf(
                "Se extinguió la necesidad",
                "Existe la necesidad, pero un hecho de fuerza mayor o caso fortuito impide continuar; puede reanudarse sólo en la gestión fiscal",
                "Hay error en el DBC",
                "Hay un único proponente"
            ), 1,
            "Suspensión: subsiste la necesidad pero un hecho de fuerza mayor/caso fortuito impide continuar. Cancelación: hecho irreversible, necesidad extinguida o cambios sustanciales.",
            "Art. 28 II y III"
        ),
        om(
            TemasExtra.EXCEPCION,
            "¿Cuál es causal de Contratación por Excepción?",
            listOf(
                "Precio referencial menor a Bs 50.000",
                "Existencia de un único proveedor cuyo bien no puede sustituirse (la marca por sí sola no es exclusividad)",
                "Urgencia administrativa por fin de gestión",
                "Preferencia por un proveedor conocido"
            ), 1,
            "Art. 65 a): único proveedor sin sustitutos. También: resolución de contrato, alimentos frescos, obras de arte, consultor único, etc.",
            "Art. 65"
        ),
        vf(
            TemasExtra.EXCEPCION,
            "Ante la resolución de un contrato de obra por las causales establecidas en el mismo, la entidad puede acudir a la Contratación por Excepción.",
            true,
            "VERDADERO (Art. 65 c). La interposición de recurso o proceso no impide la contratación.",
            "Art. 65 c)"
        ),

        // ---- Responsables ----
        om(
            TemasExtra.RESPONSABLES,
            "¿Quién NO puede integrar la Comisión de Calificación?",
            listOf(
                "Un representante de la Unidad Solicitante",
                "Un representante de la Unidad Administrativa",
                "El RPC, el RPA, la MAE o el abogado que asesora el proceso",
                "Un consultor individual de línea, excepcionalmente"
            ), 2,
            "No pueden ser Responsable de Evaluación ni Comisión de Calificación: MAE, RPC, RPA ni el representante de la Unidad Jurídica que asesora el proceso.",
            "Art. 38 I"
        ),
        om(
            TemasExtra.RESPONSABLES,
            "Las recomendaciones de la Comisión de Calificación se adoptan por:",
            listOf("Mayoría simple", "Consenso de sus integrantes", "Decisión del presidente", "Voto del RPC"), 1,
            "Por consenso; todos sus integrantes son responsables del proceso de evaluación.",
            "Art. 38 II"
        ),
        om(
            TemasExtra.RESPONSABLES,
            "¿Quién verifica que la contratación esté en el POA y el PAC, autoriza el inicio y aprueba el DBC en Licitación Pública?",
            listOf("La MAE", "El RPC", "La Unidad Solicitante", "La Comisión de Calificación"), 1,
            "El RPC (Art. 33): verifica POA/PAC y certificación presupuestaria, autoriza el inicio y aprueba el DBC.",
            "Art. 33"
        ),
        om(
            TemasExtra.RESPONSABLES,
            "¿Quién elabora las especificaciones técnicas y calcula el Precio Referencial?",
            listOf("La Unidad Administrativa", "La Unidad Solicitante", "El RPA", "La Unidad Jurídica"), 1,
            "La Unidad Solicitante.",
            "Arts. 16 y 35"
        ),

        // ---- Prohibiciones y excusas ----
        om(
            TemasExtra.PROHIBICIONES,
            "¿Cuál es una prohibición expresa a los servidores públicos que intervienen en el proceso?",
            listOf(
                "Absolver consultas escritas antes de la presentación de propuestas",
                "Fraccionar las contrataciones",
                "Publicar la convocatoria en el SICOES",
                "Realizar inspección previa"
            ), 1,
            "Art. 40 e): fraccionar contrataciones. Otras: exigir más requisitos que el DBC, iniciar sin presupuesto, obras con planos no actualizados, aceptar regalos, direccionar por marca, etc.",
            "Art. 40"
        ),
        vf(
            TemasExtra.PROHIBICIONES,
            "Está prohibido iniciar procesos de contratación de obras con planos o diseños no actualizados.",
            true,
            "VERDADERO (Art. 40 d).",
            "Art. 40 d)"
        ),
        om(
            TemasExtra.PROHIBICIONES,
            "Si es absolutamente necesario citar una marca comercial en el DBC, debe añadirse:",
            listOf("'o similar'", "'o su equivalente'", "'preferentemente'", "'marca referencial'"), 1,
            "Debe agregarse 'o su equivalente' para no direccionar el DBC.",
            "Art. 40 m)"
        ),
        om(
            TemasExtra.PROHIBICIONES,
            "¿Cuál NO es causal de excusa del Art. 41?",
            listOf(
                "Parentesco hasta 3er grado de consanguinidad con el representante del proponente",
                "Litigio pendiente con el proponente",
                "Haberle prestado servicios profesionales en el último año",
                "Haber sido compañero de universidad del proponente hace 10 años"
            ), 3,
            "Causales: interés personal/económico, parentesco (3° consanguinidad, 2° afinidad, adopción), litigio pendiente, relación de servicio en el último año.",
            "Art. 41 I"
        ),
        om(
            TemasExtra.PROHIBICIONES,
            "La omisión de excusa de un servidor público:",
            listOf(
                "Da lugar a recusación por los proponentes",
                "Anula automáticamente el proceso",
                "No da lugar a recusación, pero es causal de Responsabilidad por la Función Pública",
                "No tiene consecuencias"
            ), 2,
            "No da lugar a recusación; se considera causal de Responsabilidad por la Función Pública (Ley 1178).",
            "Art. 41 VI"
        ),
        om(
            TemasExtra.PROHIBICIONES,
            "Si el RPC está comprendido en una causal de excusa, la MAE debe ratificarlo o designar reemplazante en el plazo de:",
            listOf("1 día", "2 días", "3 días", "5 días"), 0,
            "Un (1) día.",
            "Art. 41 II"
        ),

        // ---- Impugnación ----
        om(
            Temas.IMPUGNACION,
            "El Recurso Administrativo de Impugnación se interpone ante el RPC/RPA en el plazo perentorio de:",
            listOf("2 días", "3 días", "5 días", "10 días"), 1,
            "Tres (3) días computables desde la publicación de la Resolución impugnable en el SICOES; fuera de plazo se devuelve.",
            "Art. 95 I"
        ),
        om(
            Temas.IMPUGNACION,
            "La garantía del recurrente contra la Resolución de Adjudicación equivale a:",
            listOf("1% del precio referencial", "1% del monto de la propuesta", "7% de la propuesta", "0,5% del PR"), 1,
            "Contra la aprobación del DBC: 1% del Precio Referencial. Contra adjudicación o desierta: 1% del monto de la propuesta. Vigencia: 30 días calendario.",
            "Art. 95 II y III"
        ),
        om(
            Temas.IMPUGNACION,
            "La MAE debe resolver el recurso en un plazo máximo de:",
            listOf("2 días", "3 días", "5 días", "10 días"), 2,
            "Cinco (5) días desde la recepción del recurso. Su resolución no admite recurso ulterior; se abre la vía judicial.",
            "Art. 97 II y III"
        ),
        om(
            Temas.IMPUGNACION,
            "Si la MAE no resuelve el recurso en plazo:",
            listOf(
                "Se confirma la resolución impugnada",
                "Opera el silencio administrativo positivo: se acepta el recurso y se revoca la resolución recurrida",
                "Se declara desierto el proceso",
                "Se amplía el plazo 5 días"
            ), 1,
            "Silencio administrativo positivo; además es causal de responsabilidad para la MAE.",
            "Art. 100"
        ),
        vf(
            Temas.IMPUGNACION,
            "La interposición del recurso de impugnación suspende el proceso de contratación hasta agotar la vía administrativa.",
            true,
            "VERDADERO. Si es por ítems/lotes, sólo se suspenden los impugnados.",
            "Art. 96"
        ),
        om(
            Temas.IMPUGNACION,
            "Las formas en que la MAE resuelve el recurso son:",
            listOf(
                "Aprobando o rechazando",
                "Confirmando, revocando o desestimando",
                "Anulando o ratificando",
                "Admitiendo o inadmitiendo"
            ), 1,
            "Confirmando la resolución impugnada, revocándola (anulando obrados hasta el vicio más antiguo) o desestimando el recurso.",
            "Art. 99"
        ),
        om(
            Temas.IMPUGNACION,
            "La garantía no ejecutada del recurrente se devuelve en un plazo máximo de:",
            listOf("5 días hábiles", "10 días calendario", "15 días", "30 días calendario"), 1,
            "Diez (10) días calendario.",
            "Art. 104"
        )
    )

    val lecciones: List<LeccionEntity> = listOf(
        LeccionEntity(
            modulo = Modulos.NORMATIVA, orden = 10,
            titulo = "Principios del SABS (Art. 3) — 11 principios",
            contenido = """
Regla mnemotécnica: 'SoPa Con BuEn EfE, Equi-Libre ResTo'.
a) Solidaridad: los recursos públicos deben favorecer a todos.
b) Participación: derecho a participar en los procesos.
c) Control Social: velar por la correcta ejecución de recursos públicos.
d) Buena Fe: se presume el actuar correcto y ético.
e) Economía: celeridad y ahorro de recursos.
f) Eficacia: alcanzar objetivos y resultados programados.
g) Eficiencia: oportunamente, en tiempo óptimo y al menor costo.
h) Equidad: participación en igualdad de condiciones.
i) Libre Participación: más amplia concurrencia mediante publicidad.
j) Responsabilidad: cumplir la normativa y asumir consecuencias.
k) Transparencia: actos, documentos e información son públicos.

Cuidado con las parejas que confunden: Economía (rapidez y ahorro) vs Eficiencia (tiempo óptimo y menor costo) vs Eficacia (lograr resultados); Participación vs Libre Participación.
            """.trimIndent(),
            fundamento = "Art. 3"
        ),
        LeccionEntity(
            modulo = Modulos.NORMATIVA, orden = 11,
            titulo = "Proceso ANPE paso a paso (Arts. 55-58)",
            contenido = """
Cuantía: > Bs 50.000 hasta Bs 1.000.000. Se evalúa aunque haya UNA sola propuesta (Art. 56).
Plazo mínimo para propuestas (Art. 57): 4 días (hasta Bs 200.000) / 8 días (hasta Bs 1.000.000).

Etapas (Art. 58):
a) Actividades previas: EETT/TDR, solicitud, DBC, autorización de inicio.
b) Publicación de la convocatoria (SICOES y Mesa de Partes).
c) Opcionales: consultas escritas, inspección previa, reunión informativa.
d) Apertura pública y lectura de precios.
e) Evaluación en acto continuo e Informe de Evaluación y Recomendación.
f) Aclaraciones (si se requieren).
g) Resolución de Adjudicación o Declaratoria Desierta.
h) Notificación.
i) Contrato u orden de compra/servicio.
j) Recepción.

Responsable: RPA (Art. 34). Recepción: Responsable o Comisión de Recepción.
Impugnación en ANPE: sólo si el monto es > Bs 200.000.
            """.trimIndent(),
            fundamento = "Arts. 55-58"
        ),
        LeccionEntity(
            modulo = Modulos.NORMATIVA, orden = 12,
            titulo = "Licitación Pública paso a paso (Arts. 59-62)",
            contenido = """
Cuantía: > Bs 1.000.000. Convocatoria Nacional hasta Bs 70.000.000; Internacional por encima (Art. 60).
Plazos mínimos (Art. 61): Nacional 15 días; Internacional 20 días (obras internacionales 15). Ampliación única de hasta 10 días por Resolución expresa.

Etapas (Art. 62):
a) Actividades previas.
b) Publicación de la convocatoria.
c) Consultas escritas, Reunión de Aclaración, Inspección previa.
d) Aprobación del DBC con enmiendas.
e) Notificación.
f) Apertura pública y lectura de precios.
g) Evaluación e Informe de Evaluación y Recomendación.
h) Aclaraciones.
i) Resolución de Adjudicación/Desierta (nómina y precios, calificación, causales de descalificación).
j) Notificación.
k) Concertación de mejores condiciones técnicas.
l) Suscripción de contrato.
m) Recepción.

Comparación de examen (Menor / ANPE / LP):
• Monto: ≤ 50.000 / 50.000-1.000.000 / > 1.000.000.
• Responsable: RPA / RPA / RPC.
• Menor: condiciones reglamentadas en el RE-SABS de cada entidad (calidad, economía, acciones ágiles; Art. 54).
• Documentación mínima: especificaciones técnicas/TDR, certificación presupuestaria, DBC (ANPE y LP), propuestas, informe de evaluación, resolución, contrato u orden.
            """.trimIndent(),
            fundamento = "Arts. 52-62"
        ),
        LeccionEntity(
            modulo = Modulos.NORMATIVA, orden = 13,
            titulo = "Desierta, cancelación, suspensión y anulación",
            contenido = """
DECLARATORIA DESIERTA (Art. 27):
a) No se recibió ninguna propuesta.
b) Todas las propuestas superan el Precio Referencial.
c) Ninguna cumple el DBC.
d) El adjudicado no presenta documentos o desiste y no hay otras calificadas.
→ Antes de reconvocar, Unidad Solicitante y Administrativa analizan causas y ajustan.

CANCELAR / SUSPENDER / ANULAR (Art. 28): hasta antes del contrato, con Resolución expresa motivada.
• CANCELAR: fuerza mayor/caso fortuito IRREVERSIBLE, necesidad extinguida, cambios sustanciales.
• SUSPENDER: la necesidad subsiste pero un hecho impide continuar; se reanuda en la misma gestión.
• ANULAR hasta el vicio más antiguo: incumplimiento normativo o error en el DBC publicado.

CONTRATACIÓN POR EXCEPCIÓN (Art. 65), entre otras: proveedor único, consultor único, resolución de contrato, semovientes, alimentos frescos, obras de arte.
            """.trimIndent(),
            fundamento = "Arts. 27, 28, 65"
        ),
        LeccionEntity(
            modulo = Modulos.NORMATIVA, orden = 14,
            titulo = "Prohibiciones (Art. 40) y excusas (Art. 41)",
            contenido = """
Prohibiciones clave a servidores públicos:
a) Exigir más requisitos que el DBC.
c) Iniciar sin presupuesto (salvo recurrentes).
d) Obras con planos o diseños no actualizados.
e) FRACCIONAR contrataciones.
f) Proyectos > 1 año sin financiamiento total.
h) Relaciones no escritas con proponentes (salvo actos públicos y consultas al encargado).
i) Aceptar regalos o beneficios.
m) Direccionar por marca (usar 'o su equivalente').

Excusas (Art. 41): RPC, RPA, comisiones, unidades jurídica y administrativa deben excusarse por:
a) Interés personal o económico con proponentes.
b) Parentesco hasta 3° consanguinidad, 2° afinidad o adopción.
c) Litigio pendiente.
d) Relación de servicio en el último año.
La MAE ratifica o reemplaza al RPC/RPA en 1 día. La omisión de excusa no da recusación, pero sí Responsabilidad por la Función Pública.
            """.trimIndent(),
            fundamento = "Arts. 40-41"
        ),
        LeccionEntity(
            modulo = Modulos.NORMATIVA, orden = 15,
            titulo = "Recurso de impugnación: números que se preguntan",
            contenido = """
• Procede (Art. 90): LP contra aprobación del DBC, adjudicación y desierta; ANPE > Bs 200.000 contra adjudicación y desierta.
• No procede contra actos preparatorios, informes, dictámenes ni inspecciones.
• Se presenta ante el RPC/RPA que emitió la resolución: 3 días (Art. 95).
• Garantía: 1% del PR (contra DBC) o 1% de la propuesta (contra adjudicación/desierta); vigencia 30 días calendario.
• Suspende el proceso (Art. 96).
• MAE comunica en 2 días y resuelve en 5 días (Art. 97); no admite recurso ulterior.
• Resolución: expresa, motivada y fundamentada, única (Art. 98).
• Formas: confirma, revoca o desestima (Art. 99).
• Sin resolución en plazo → silencio administrativo POSITIVO (Art. 100).
• Notificación de la resolución: 2 días (Art. 101).
• Devolución de garantía no ejecutada: 10 días calendario (Art. 104).
            """.trimIndent(),
            fundamento = "Arts. 90-104"
        )
    )
}
