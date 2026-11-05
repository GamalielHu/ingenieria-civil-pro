package com.gamaliel.ingcivilpro.data.local

/**
 * Contenido semilla. Fuente normativa: NB-SABS, Decreto Supremo N° 0181 (texto compilado 2023,
 * con modificaciones de los DS 0843, 0956, 1497, 1783, 2728, 3548 y 4453).
 * Las preguntas con origen "Examen" reproducen reactivos de exámenes pasados de Dirección/Gerencia de Obras.
 */
object SeedData {

    private const val NBSABS = "NB-SABS DS 0181"
    private const val EXAMEN = "Examen pasado CIV-248"
    private const val DBC = "DBC Obras (modelo de contrato)"

    internal fun om(
        tema: String, enunciado: String, opciones: List<String>, correcta: Int,
        explicacion: String, fundamento: String, origen: String = NBSABS,
        tipo: String = TiposPregunta.OPCION_MULTIPLE
    ) = PreguntaEntity(
        tema = tema, tipo = tipo, enunciado = enunciado, opciones = opciones,
        indiceCorrecto = correcta, explicacion = explicacion, fundamento = fundamento, origen = origen
    )

    internal fun vf(
        tema: String, enunciado: String, verdadero: Boolean, explicacion: String,
        fundamento: String, origen: String = NBSABS
    ) = om(
        tema, enunciado, listOf("Verdadero", "Falso"), if (verdadero) 0 else 1,
        explicacion, fundamento, origen, TiposPregunta.VERDADERO_FALSO
    )

    internal fun caso(
        tema: String, enunciado: String, opciones: List<String>, correcta: Int,
        explicacion: String, fundamento: String, origen: String = NBSABS
    ) = om(tema, enunciado, opciones, correcta, explicacion, fundamento, origen, TiposPregunta.CASO)

    // -----------------------------------------------------------------------
    // PREGUNTAS
    // -----------------------------------------------------------------------
    val preguntas: List<PreguntaEntity> by lazy { preguntasBase + ContenidoContrataciones.preguntas + ContenidoEtica.preguntas }

    private val preguntasBase: List<PreguntaEntity> = listOf(
        // ---- Art. 89 ----
        om(
            Temas.MODIFICACIONES,
            "¿Qué instrumento se usa ÚNICAMENTE en obras cuando la modificación implica cambiar el precio o el plazo del contrato, sin incrementar precios unitarios ni crear nuevos ítems?",
            listOf("Contrato Modificatorio", "Orden de Cambio", "Orden de Trabajo", "Adenda notariada"), 1,
            "La Orden de Cambio se utiliza sólo en contratación de obras y modifica precio o plazo sin subir precios unitarios ni crear ítems nuevos. Si se necesitan ítems nuevos corresponde Contrato Modificatorio.",
            "Art. 89 II d)"
        ),
        om(
            Temas.MODIFICACIONES,
            "La Orden de Cambio no podrá exceder el ___ del monto del contrato principal.",
            listOf("5%", "10%", "15%", "20%"), 0,
            "Tope de la Orden de Cambio: cinco por ciento (5%) del monto del contrato principal.",
            "Art. 89 II d)"
        ),
        om(
            Temas.MODIFICACIONES,
            "Uno o varios Contratos Modificatorios, sumados, no deberán exceder el ___ del monto del contrato principal.",
            listOf("5%", "10%", "15%", "25%"), 1,
            "Los contratos modificatorios sumados no deben exceder el diez por ciento (10%) del contrato principal.",
            "Art. 89 II a)"
        ),
        om(
            Temas.MODIFICACIONES,
            "En Licitación Pública, cuando se emiten Órdenes de Cambio y además Contratos Modificatorios, sumados no deben exceder el:",
            listOf("10%", "12%", "15%", "20%"), 2,
            "Licitación Pública: OC + CM ≤ 15% del monto del contrato principal. En ANPE el tope conjunto es 10%.",
            "Art. 89 II d)"
        ),
        vf(
            Temas.MODIFICACIONES,
            "En obras ANPE, sumados el o los contratos modificatorios y órdenes de cambio no deberán exceder el quince por ciento (15%) del monto del contrato principal.",
            false,
            "FALSO. En ANPE el tope conjunto (OC + CM) es diez por ciento (10%). El 15% corresponde a Licitación Pública.",
            "Art. 89 II d)", EXAMEN
        ),
        om(
            Temas.MODIFICACIONES,
            "¿Quién aprueba la Orden de Cambio?",
            listOf(
                "La MAE mediante resolución expresa",
                "El Supervisor de obra por sí solo",
                "La instancia responsable del seguimiento de la obra en la entidad",
                "El RPC"
            ), 2,
            "La Orden de Cambio debe ser aprobada por la instancia responsable del seguimiento de la obra en la entidad (en la práctica, la Fiscalización). El Contrato Modificatorio, en cambio, lo suscribe la MAE o la autoridad que firmó el contrato principal.",
            "Art. 89 II d)"
        ),
        om(
            Temas.MODIFICACIONES,
            "¿Quién suscribe el Contrato Modificatorio?",
            listOf(
                "El Supervisor y el Superintendente",
                "La MAE o la autoridad que suscribió el contrato principal",
                "El Fiscal de obra",
                "La Unidad Jurídica"
            ), 1,
            "El Contrato Modificatorio será suscrito por la MAE o por la autoridad que suscribió el contrato principal.",
            "Art. 89 II a)"
        ),
        om(
            Temas.MODIFICACIONES,
            "Si para cumplir el objeto del contrato se necesitan nuevos ítems (volúmenes o cantidades no previstas), sus precios unitarios:",
            listOf(
                "Se fijan con el precio referencial vigente",
                "Deben ser negociados, mediante Contrato Modificatorio",
                "Los define el Supervisor con Orden de Cambio",
                "No pueden crearse nuevos ítems bajo ninguna figura"
            ), 1,
            "La creación de ítems nuevos sólo es posible con Contrato Modificatorio, y sus precios unitarios deben ser negociados. La Orden de Cambio prohíbe expresamente crear ítems nuevos.",
            "Art. 89 II a) y d)"
        ),
        om(
            Temas.MODIFICACIONES,
            "Toda modificación al contrato debe estar sustentada por:",
            listOf(
                "Sólo el visto bueno del Supervisor",
                "Informe técnico y legal que establezca la viabilidad técnica y de financiamiento",
                "Una nota del contratista con su justificación",
                "Únicamente certificación presupuestaria"
            ), 1,
            "Las modificaciones deben estar destinadas al cumplimiento del objeto y sustentadas por informe técnico y legal (viabilidad técnica y de financiamiento). En proyectos de inversión se aplican además las normas del SNIP.",
            "Art. 89 I"
        ),
        caso(
            Temas.MODIFICACIONES,
            "Obra por Licitación Pública de Bs 2.000.000. Ya se aprobó una Orden de Cambio por Bs 60.000. El Supervisor propone otra Orden de Cambio por Bs 50.000 por mayores volúmenes de un ítem existente, con el mismo precio unitario. ¿Qué corresponde?",
            listOf(
                "Procede: cada Orden de Cambio se evalúa por separado",
                "No procede como Orden de Cambio porque el acumulado (Bs 110.000 = 5,5%) supera el 5%; debe tramitarse Contrato Modificatorio con informe técnico y legal",
                "Procede si el Supervisor lo firma en el Libro de Órdenes",
                "Debe convocarse una nueva licitación"
            ), 1,
            "Acumulado de OC = 60.000 + 50.000 = 110.000 → 110.000 / 2.000.000 = 5,5% > 5%. Excede el tope de la OC. La alternativa es Contrato Modificatorio (≤10%), siempre que OC + CM no superen el 15% en LP.",
            "Art. 89 II a) y d)"
        ),
        caso(
            Temas.MODIFICACIONES,
            "Obra ANPE de Bs 800.000. Existe un Contrato Modificatorio de Bs 50.000 y ahora se pide una Orden de Cambio de Bs 40.000. ¿Es viable?",
            listOf(
                "Sí, la OC es exactamente 5%",
                "Sí, porque el CM es menor al 10%",
                "No, porque OC + CM = 11,25% y en ANPE el tope conjunto es 10%",
                "No, porque en ANPE no existen Órdenes de Cambio"
            ), 2,
            "OC = 40.000/800.000 = 5% (en el límite), CM = 6,25%. Sumados: 90.000/800.000 = 11,25% > 10% (tope conjunto ANPE). No es viable.",
            "Art. 89 II d)"
        ),
        om(
            Temas.MODIFICACIONES,
            "Indique los tres instrumentos para modificar las obras según el DBC de obras:",
            listOf(
                "Orden de Trabajo, Orden de Cambio y Contrato Modificatorio",
                "Adenda, Resolución y Contrato Modificatorio",
                "Orden de Proceder, Orden de Cambio y Acta",
                "Planilla, Certificado y Orden de Cambio"
            ), 0,
            "El DBC de obras prevé: Orden de Trabajo (ajustes sin variar monto ni plazo), Orden de Cambio (Art. 89 II d) y Contrato Modificatorio (Art. 89 II a).",
            "$DBC; Art. 89", EXAMEN
        ),
        vf(
            Temas.MODIFICACIONES,
            "En una obra ANPE, el plazo podrá modificarse solo con una Orden de Trabajo.",
            false,
            "FALSO. La Orden de Trabajo no modifica monto ni plazo. El plazo se modifica con Orden de Cambio (precio o plazo, ≤5%) o con Contrato Modificatorio (alcance, monto y/o plazo).",
            "Art. 89 II a) y d)", EXAMEN
        ),
        om(
            Temas.MODIFICACIONES,
            "El Contrato Modificatorio para Consultores Individuales de Línea podrá realizarse hasta un máximo de:",
            listOf("Una vez", "Dos veces", "Tres veces", "Sin límite"), 1,
            "Hasta dos (2) veces, sin que el plazo de cada modificación exceda el del contrato principal, previa evaluación del cumplimiento de los TDR.",
            "Art. 89 II b)"
        ),
        om(
            Temas.MODIFICACIONES,
            "El Contrato Modificatorio para Servicios Generales Recurrentes procede:",
            listOf("Una sola vez", "Dos veces", "Hasta llegar al 10%", "Cada gestión"), 0,
            "Una (1) sola vez, sin exceder el plazo establecido en el contrato principal.",
            "Art. 89 II c)"
        ),
        om(
            Temas.MODIFICACIONES,
            "La cesión o subrogación de contratos procede por:",
            listOf(
                "Solicitud del contratista por falta de liquidez",
                "Fuerza mayor, caso fortuito o necesidad pública, previa aprobación de la MAE",
                "Decisión del Supervisor",
                "No procede nunca"
            ), 1,
            "Procede por fuerza mayor, caso fortuito o necesidad pública, previa aprobación de la MAE de la entidad contratante.",
            "Art. 89 III"
        ),

        // ---- Garantías y anticipo ----
        om(
            Temas.GARANTIAS,
            "La Garantía de Cumplimiento de Contrato equivale al:",
            listOf("1%", "3,5%", "7%", "10%"), 2,
            "Siete por ciento (7%) del monto del contrato. En contrataciones hasta Bs 1.000.000 con pagos parciales puede sustituirse por retención del 7% de cada pago.",
            "Art. 21 I b)"
        ),
        vf(
            Temas.GARANTIAS,
            "En obras ANPE, las Micro y Pequeñas Empresas presentarán una Garantía de Cumplimiento de Contrato equivalente al 3,5% del valor del contrato o se hará una retención del 3,5% de cada pago cuando haya pagos parciales.",
            true,
            "VERDADERO. En contrataciones hasta Bs 1.000.000, MyPEs, Asociaciones de Pequeños Productores y OECAs presentan garantía del 3,5% o retención del 3,5%.",
            "Art. 21 I b)", EXAMEN
        ),
        om(
            Temas.GARANTIAS,
            "La Garantía de Seriedad de Propuesta (bienes, obras, servicios generales) equivale al:",
            listOf("0,5% del precio referencial", "1% del precio referencial", "7% de la propuesta", "1% de la propuesta"), 1,
            "1% del precio referencial (0,5% en consultoría). Su vigencia excede en 30 días calendario la validez de la propuesta.",
            "Art. 21 I a)"
        ),
        vf(
            Temas.GARANTIAS,
            "La Garantía de Correcta Inversión de Anticipo es por el cien por ciento (100%) del monto entregado.",
            true,
            "VERDADERO. Equivale al 100% del anticipo, con vigencia mínima de 90 días calendario desde la entrega, renovable mientras no se deduzca el total.",
            "Art. 21 I e)", EXAMEN
        ),
        om(
            Temas.GARANTIAS,
            "El anticipo no deberá exceder el ___ del monto total del contrato (obras).",
            listOf("10%", "20%", "30%", "50%"), 1,
            "Máximo 20%, previa solicitud del contratista y aceptación del contratante. El 50% aplica sólo a adquisición de inmuebles en construcción.",
            "Art. 19 I y III"
        ),
        om(
            Temas.GARANTIAS,
            "En obras, la deducción del anticipo se inicia:",
            listOf(
                "Al firmar el contrato",
                "Con el primer certificado de avance de obra",
                "En la recepción provisional",
                "Al final, en la planilla de liquidación"
            ), 1,
            "Para obras, los pagos y la deducción del anticipo se inician con el primer certificado de avance de obra.",
            "Art. 19 II"
        ),
        caso(
            Temas.GARANTIAS,
            "Precio referencial de una obra: Bs 1.500.000. La propuesta adjudicada es de Bs 1.200.000. ¿Debe presentar Garantía Adicional?",
            listOf(
                "No, porque la propuesta es menor al referencial",
                "Sí, por Bs 75.000 (85% del PR = 1.275.000 menos 1.200.000)",
                "Sí, por el 7% de la propuesta",
                "Sí, por Bs 300.000"
            ), 1,
            "Si la propuesta está por debajo del 85% del PR se exige garantía adicional igual a la diferencia: 0,85 × 1.500.000 = 1.275.000 → 1.275.000 − 1.200.000 = Bs 75.000.",
            "Art. 21 I c)"
        ),
        om(
            Temas.GARANTIAS,
            "La vigencia mínima de la Garantía de Correcta Inversión de Anticipo es:",
            listOf("30 días calendario", "60 días hábiles", "90 días calendario", "Hasta la recepción definitiva"), 2,
            "Vigencia mínima de 90 días calendario desde la entrega del anticipo, renovable mientras no se deduzca el total.",
            "Art. 21 I e)"
        ),

        // ---- Modalidades, cuantías y plazos ----
        om(
            Temas.MODALIDADES,
            "La modalidad ANPE se aplica para montos:",
            listOf(
                "Hasta Bs 50.000",
                "Mayores a Bs 50.000 hasta Bs 1.000.000",
                "Mayores a Bs 1.000.000",
                "Sin límite de monto"
            ), 1,
            "Contratación Menor: Bs 1 a 50.000. ANPE: > 50.000 hasta 1.000.000. Licitación Pública: > 1.000.000.",
            "Art. 13"
        ),
        om(
            Temas.MODALIDADES,
            "¿Qué modalidades no tienen límite de monto?",
            listOf(
                "Contratación Menor y ANPE",
                "Excepción, Desastres y/o Emergencias, y Contratación Directa",
                "Sólo Licitación Pública",
                "ANPE con cotizaciones"
            ), 1,
            "Contratación por Excepción, por Desastres y/o Emergencias y Contratación Directa de Bienes y Servicios no tienen límite de monto.",
            "Art. 13"
        ),
        om(
            Temas.MODALIDADES,
            "Salvo indicación expresa de días calendario, los plazos de las NB-SABS se computan en:",
            listOf("Días calendario", "Días hábiles administrativos (lunes a viernes)", "Días hábiles incluido sábado", "Horas"), 1,
            "Todos los plazos son días hábiles administrativos (lunes a viernes, sin feriados), excepto los señalados como calendario. Si vencen en sábado, domingo o feriado se trasladan al siguiente día hábil.",
            "Art. 15"
        ),
        om(
            Temas.MODALIDADES,
            "Para convocar una obra, el Precio Referencial no debe tener una antigüedad mayor a:",
            listOf("2 meses", "4 meses", "6 meses", "1 año"), 1,
            "Para obras, el Precio Referencial no debe tener una antigüedad mayor a cuatro (4) meses. Es público y debe figurar en el DBC.",
            "Art. 16 IV"
        ),
        om(
            Temas.MODALIDADES,
            "El cronograma de plazos del proceso de contratación:",
            listOf(
                "Es referencial y puede modificarse libremente",
                "Forma parte del DBC y sus plazos son de cumplimiento obligatorio",
                "Lo elabora el proponente",
                "Sólo es exigido en Licitación Pública internacional"
            ), 1,
            "Es el instrumento de planificación de plazos de cada proceso, forma parte del DBC y sus plazos son de cumplimiento obligatorio.",
            "Art. 47"
        ),
        om(
            Temas.SABS,
            "Los tres subsistemas del SABS son:",
            listOf(
                "Contratación, Manejo y Disposición",
                "Planificación, Ejecución y Control",
                "Inversión, Preinversión y Operación",
                "Licitación, ANPE y Menor"
            ), 0,
            "El SABS regula la contratación de bienes y servicios, el manejo y la disposición de bienes de las entidades públicas.",
            "Art. 1", EXAMEN
        ),
        om(
            Temas.SABS,
            "Según las NB-SABS, el Precio Referencial lo calcula:",
            listOf("El proponente", "La Unidad Solicitante, incluyendo tributos, transporte, instalación y otros costos", "El Órgano Rector", "La Comisión de Calificación"), 1,
            "La Unidad Solicitante calcula el Precio Referencial incluyendo todos los tributos, transporte, instalación, inspecciones y cualquier concepto que incida en el costo total, con información respaldatoria.",
            "Art. 16 I"
        ),
        vf(
            Temas.SABS,
            "La entidad adquiere materiales de construcción principalmente cuando construye obras por administración directa.",
            true,
            "La clave del examen lo calificó VERDADERO. Además, el Art. 82 permite que la entidad adquiera materiales para proporcionarlos al contratista cuando convoque el servicio de construcción con provisión total o parcial de materiales.",
            "Art. 82", EXAMEN
        ),

        // ---- Contrato ----
        om(
            Temas.CONTRATO,
            "Debe protocolizarse ante Notaría de Gobierno el contrato cuyo monto sea:",
            listOf("Mayor a Bs 200.000", "Igual o superior a Bs 1.000.000", "Superior a Bs 50.000", "Cualquier monto"), 1,
            "Contratos ≥ Bs 1.000.000 (o los que por ley requieran escritura pública). La documentación se entrega en un plazo no mayor a 10 días; el costo lo asume el contratista. La falta de protocolización no afecta la validez.",
            "Art. 88 II"
        ),
        om(
            Temas.CONTRATO,
            "Los contratos que suscriben las entidades públicas para la provisión de bienes y servicios son de naturaleza:",
            listOf("Civil", "Comercial", "Administrativa", "Laboral"), 2,
            "Son de naturaleza administrativa.",
            "Art. 85"
        ),
        om(
            Temas.CONTRATO,
            "¿Cuál NO es un documento integrante del contrato según el Art. 86?",
            listOf("DBC", "Propuesta adjudicada", "Garantías", "Informe de la Comisión de Calificación de propuestas no adjudicadas"), 3,
            "Integran el contrato: DBC, propuesta adjudicada, resolución de adjudicación, poder del representante, garantías, póliza (seguros), contrato de asociación accidental y otros específicos.",
            "Art. 86"
        ),
        om(
            Temas.CONTRATO,
            "Respecto a la subcontratación:",
            listOf(
                "Está prohibida",
                "Se permite si el contrato lo define; el contratista mantiene todas sus obligaciones frente a la entidad",
                "La entidad asume responsabilidad con el subcontratista",
                "Sólo se permite a empresas extranjeras"
            ), 1,
            "Procede cuando la naturaleza del objeto lo permita y esté definido en el contrato. El contratista mantiene la totalidad de sus obligaciones; la entidad no asume obligación con el subcontratado.",
            "Art. 87 Bis"
        ),

        // ---- Impedimentos ----
        om(
            Temas.IMPEDIMENTOS,
            "Están impedidos de participar quienes tengan vínculo de parentesco con la MAE hasta el:",
            listOf(
                "Segundo grado de consanguinidad y primero de afinidad",
                "Tercer grado de consanguinidad y segundo de afinidad",
                "Cuarto grado de consanguinidad",
                "Primer grado únicamente"
            ), 1,
            "Representantes legales, accionistas o socios controladores con vínculo matrimonial o parentesco con la MAE hasta el tercer grado de consanguinidad y segundo de afinidad.",
            "Art. 43 f)"
        ),
        om(
            Temas.IMPEDIMENTOS,
            "Los ex servidores públicos de la entidad convocante están impedidos si ejercieron funciones hasta ___ antes de la publicación de la convocatoria.",
            listOf("6 meses", "1 año", "2 años", "3 años"), 1,
            "Un (1) año antes de la publicación de la convocatoria, incluidas las empresas controladas por ellos.",
            "Art. 43 g)"
        ),
        caso(
            Temas.IMPEDIMENTOS,
            "La MAE observa al proponente adjudicado porque trabajó como técnico de estudios en el mismo municipio hace 15 meses y es hermano del exalcalde de la gestión anterior. Asumiendo el rol del RPC, ¿se adjudica?",
            listOf(
                "No se adjudica: el parentesco con el exalcalde lo impide",
                "Sí se adjudica: dejó la entidad hace más de 1 año (Art. 43 g) y el impedimento por parentesco es con la MAE en funciones (Art. 43 f), no con un exalcalde",
                "No se adjudica porque la MAE lo pidió",
                "Se declara desierto el proceso"
            ), 1,
            "Art. 43 g) impide sólo a ex servidores que ejercieron funciones hasta 1 año antes; 15 meses supera ese plazo. Art. 43 f) se refiere al parentesco con la MAE actual. No existe causal de impedimento: el RPC adjudica.",
            "Art. 43 f) y g)", EXAMEN
        ),
        om(
            Temas.IMPEDIMENTOS,
            "Un contratista al que se le resolvió el contrato por causas atribuibles a él no podrá participar durante:",
            listOf("1 año", "2 años", "3 años", "5 años"), 2,
            "Tres (3) años desde la fecha de resolución. Si incumplió una orden de compra u orden de servicio: 1 año.",
            "Art. 43 j)"
        ),
        om(
            Temas.IMPEDIMENTOS,
            "Quien asesoró a la entidad en un proceso de contratación:",
            listOf(
                "Puede participar si renuncia a la asesoría",
                "No puede participar en ese mismo proceso bajo ninguna circunstancia",
                "Puede participar asociado",
                "Puede participar sólo en ANPE"
            ), 1,
            "Conflicto de intereses: quien asesore a la entidad en un proceso no puede participar en el mismo bajo ninguna razón o circunstancia.",
            "Art. 44 I"
        ),

        // ---- Recepción y supervisión ----
        om(
            Temas.RECEPCION,
            "La recepción de obras se realiza en:",
            listOf(
                "Una sola etapa con acta final",
                "Dos etapas: provisional y definitiva, con sus actas",
                "Tres etapas: parcial, provisional y definitiva",
                "Mediante Informe de Conformidad único"
            ), 1,
            "La recepción de obras se realiza en dos etapas: provisional y definitiva, emitiéndose las actas respectivas.",
            "Art. 39 III"
        ),
        vf(
            Temas.RECEPCION,
            "El SUPERVISOR deberá establecer de forma racional, en función al tipo de obra, el plazo máximo para la Recepción Definitiva, que no podrá exceder de ciento ochenta (180) días calendario.",
            true,
            "VERDADERO según el modelo de DBC/contrato de obras: la recepción definitiva no puede exceder 180 días calendario desde la provisional.",
            DBC, EXAMEN
        ),
        vf(
            Temas.RECEPCION,
            "Supervisor: es la empresa consultora o profesional independiente que ha sido o será contratada por el Contratante para que realice un servicio de fiscalización de alguna obra civil específica.",
            false,
            "FALSO. El Supervisor presta un servicio de SUPERVISIÓN técnica. La fiscalización la ejerce el Fiscal de Obra, servidor público designado por la entidad para el seguimiento.",
            DBC, EXAMEN
        ),
        om(
            Temas.RECEPCION,
            "¿Quién designa al Responsable de Recepción (ANPE) o a la Comisión de Recepción (LP)?",
            listOf("El Supervisor", "La MAE (puede delegar en RPC, RPA o autoridad de la Unidad Solicitante)", "El contratista", "La Unidad Jurídica"), 1,
            "Lo designa la MAE, pudiendo delegar esta función.",
            "Art. 32 d)"
        ),
        om(
            Temas.RECEPCION,
            "El Acta de Recepción firmada por la Comisión de Recepción:",
            listOf(
                "Libera al Supervisor de toda responsabilidad",
                "No exime las responsabilidades del proveedor ni del supervisor",
                "Reemplaza a la garantía de cumplimiento",
                "Sólo se emite en ANPE"
            ), 1,
            "El acta o informe de conformidad no exime las responsabilidades del proveedor ni del supervisor respecto de la entrega.",
            "Art. 39 II b)"
        ),

        // ---- Libro de órdenes ----
        om(
            Temas.LIBRO,
            "En el Libro de Órdenes, ¿quién imparte órdenes e instrucciones al contratista?",
            listOf("El Fiscal de obra", "El Supervisor", "El residente del contratista", "La MAE"), 1,
            "El Supervisor imparte órdenes; el Contratista (Superintendente) registra comunicaciones, solicitudes y reclamos, y firma la recepción de las órdenes.",
            DBC
        ),
        caso(
            Temas.LIBRO,
            "Durante la excavación de un tanque enterrado aparece napa freática a 1,60 m y la maquinaria no puede continuar. El contratista solicita instrucciones y pregunta cómo se pagarán los mayores costos. ¿Respuesta correcta del Supervisor?",
            listOf(
                "Ordenar seguir excavando y pagar con nuevos precios unitarios mediante Orden de Cambio",
                "Instruir por escrito el agotamiento (bombeo) y el procedimiento; si son mayores volúmenes de ítems existentes, Orden de Cambio (≤5%) a precios del contrato; si se requieren ítems nuevos (ej. agotamiento no previsto), Contrato Modificatorio con precios negociados e informe técnico-legal",
                "Paralizar la obra hasta la próxima gestión",
                "Aprobar el incremento de precios unitarios por ser un hecho imprevisto"
            ), 1,
            "Ninguna figura del Art. 89 permite incrementar precios unitarios. Volúmenes adicionales de ítems existentes → OC; ítems nuevos → CM con precios negociados. Toda modificación requiere informe técnico y legal.",
            "Art. 89 I y II", EXAMEN
        ),
        caso(
            Temas.LIBRO,
            "La empresa solicita ampliación de plazo de 30 días argumentando que las lluvias superaron los registros históricos de los últimos 10 años. ¿Qué documentación técnica debe presentar?",
            listOf(
                "Sólo una nota simple al Supervisor",
                "Registros pluviométricos oficiales (SENAMHI) comparados con la serie histórica, asientos del Libro de Órdenes de los días afectados, informe técnico con días perdidos y cronograma reprogramado, fotografías",
                "Una declaración jurada notariada",
                "Ninguna, el caso fortuito es automático"
            ), 1,
            "La ampliación debe probar el caso fortuito/fuerza mayor y el impacto real en la ruta crítica. El Supervisor evalúa y, si procede, se tramita Orden de Cambio o Contrato Modificatorio sustentado en informe técnico y legal.",
            "Art. 89 I; $DBC", EXAMEN
        ),
        om(
            Temas.LIBRO,
            "¿Cuál de estas redacciones de orden es la CORRECTA?",
            listOf(
                "Mejorar el encofrado lo antes posible.",
                "Se instruye retirar y reemplazar el encofrado de la losa del eje B-C entre ejes 2-3 por presentar deformación mayor a 5 mm, conforme especificación técnica ítem 12; plazo 2 días; Supervisión verificará antes del vaciado.",
                "Corregir lo observado según criterio del contratista.",
                "Ver tema del encofrado, etc."
            ), 1,
            "Una orden correcta dice QUÉ hacer, DÓNDE, CÓMO (referencia a plano/especificación), PLAZO y cómo se VERIFICARÁ. Evitar expresiones ambiguas como 'lo antes posible', 'según criterio' o 'etc.'.",
            DBC
        ),

        // ---- Cómputos ----
        om(
            Temas.COMPUTOS,
            "La fórmula del volumen de un tronco de pirámide (zapata) con áreas A1 (base) y A2 (cara superior) y altura h es:",
            listOf("V = h·(A1 + A2)/2", "V = h/3·(A1 + A2 + √(A1·A2))", "V = h·√(A1·A2)", "V = h/6·(A1 + A2)"), 1,
            "Tronco de pirámide: V = h/3·(A1 + A2 + √(A1·A2)). La media de áreas h(A1+A2)/2 sobreestima el volumen.",
            "Cómputos métricos", EXAMEN
        ),
        caso(
            Temas.COMPUTOS,
            "Un sobrecimiento de 100 m de perímetro tiene sección 0,40 × 0,80 m. Se usaron 64 bolsas de cemento. ¿Rendimiento del cemento por m³?",
            listOf("1 bolsa = 50 kg/m³", "2 bolsas = 100 kg/m³", "3 bolsas = 150 kg/m³", "64 kg/m³"), 1,
            "V = 100 × 0,40 × 0,80 = 32 m³. 64 bolsas / 32 m³ = 2 bolsas/m³ = 2 × 50 kg = 100 kg/m³.",
            "Cómputos y rendimientos", EXAMEN
        ),
        caso(
            Temas.COMPUTOS,
            "Para el mismo sobrecimiento de 32 m³ se usaron 18 jornales. ¿Rendimiento de mano de obra en horas por m³ (jornada de 8 h)?",
            listOf("2,25 h/m³", "4,5 h/m³", "8 h/m³", "0,56 h/m³"), 1,
            "18 / 32 = 0,5625 jornales/m³ × 8 h = 4,5 h/m³.",
            "Cómputos y rendimientos", EXAMEN
        ),
        om(
            Temas.COMPUTOS,
            "1 m² equivale aproximadamente a:",
            listOf("3,28 pie²", "10,76 pie²", "12 pie²", "9 pie²"), 1,
            "1 m = 3,2808 pie → 1 m² = 10,764 pie².",
            "Conversión de unidades"
        ),
        caso(
            Temas.COMPUTOS,
            "Encofrado de sobrecimiento H°C° (0,20 × 0,45 × 5,0 m): tablones 2 caras de 0,50 × 5,0 m y 10 listones 2\"×2\" de 0,60 m. ¿Rendimiento de madera en pie² por m³?",
            listOf("60,36 pie²/m³", "134,13 pie²/m³", "53,80 pie²/m³", "26,83 pie²/m³"), 1,
            "V = 0,45 m³. Tablones: 2×0,50×5 = 5 m² = 53,80 pie². Listones: 6 m × 0,1016 m = 0,6096 m² = 6,56 pie². Total 60,36 pie² / 0,45 m³ = 134,13 pie²/m³ (un solo uso).",
            "Cómputos y rendimientos", EXAMEN
        ),
        caso(
            Temas.COMPUTOS,
            "Muro de contención (sección constante): pantalla 0,50 × 4,27 m y zapata 2,00 × 0,50 m, longitud 5 m. H°A° = 4000 Bs/m³. ¿Costo?",
            listOf("Bs 42.700", "Bs 62.700", "Bs 58.500", "Bs 85.400"), 1,
            "Área = 0,50×4,27 + 2,00×0,50 = 2,135 + 1,00 = 3,135 m². V = 3,135 × 5 = 15,675 m³. Costo = 15,675 × 4000 = Bs 62.700.",
            "Cómputos métricos", EXAMEN
        ),

        // ---- Precios / impuestos ----
        om(
            Temas.PRECIOS,
            "Si el IVA es 14%, el IVA compensado es:",
            listOf("14,00%", "15,96%", "16,28%", "17,65%"), 2,
            "IVA compensado = i / (1 − i) = 0,14 / 0,86 = 16,28%. Se compensa porque el impuesto se aplica sobre el precio que ya lo incluye.",
            "Análisis de precios unitarios", EXAMEN
        ),
        om(
            Temas.PRECIOS,
            "Si el IT es 4%, el IT compensado es:",
            listOf("4,00%", "4,17%", "4,35%", "5,26%"), 1,
            "IT compensado = 0,04 / 0,96 = 4,17%.",
            "Análisis de precios unitarios", EXAMEN
        ),
        caso(
            Temas.PRECIOS,
            "Si el IVA compensado es 16,96%, ¿cuál es el IVA nominal?",
            listOf("13%", "14,5%", "15%", "16,96%"), 1,
            "Despejando: i = c / (1 + c) = 0,1696 / 1,1696 = 14,5%.",
            "Análisis de precios unitarios", EXAMEN
        ),
        om(
            Temas.PRECIOS,
            "En el análisis de precio unitario, las herramientas menores se calculan normalmente como porcentaje de:",
            listOf("Los materiales", "La mano de obra total", "Los gastos generales", "La utilidad"), 1,
            "Herramientas menores = % del total de mano de obra (incluidas cargas sociales e IVA de la MO).",
            "Análisis de precios unitarios"
        ),

        // ---- Cronograma ----
        om(
            Temas.CRONOGRAMA,
            "En el método de la ruta crítica, las actividades críticas son las que tienen:",
            listOf("Mayor costo", "Holgura total igual a cero", "Mayor número de cuadrillas", "Mayor volumen"), 1,
            "Holgura total = Inicio tardío − Inicio temprano. Si es 0, cualquier retraso en la actividad retrasa toda la obra.",
            "Programación de obras"
        ),
        caso(
            Temas.CRONOGRAMA,
            "Una propuesta suma Bs 417.437,27 y el precio referencial es Bs 378.426,61. ¿Qué sucede?",
            listOf(
                "Se adjudica porque es la de mejor calidad",
                "La propuesta excede el precio referencial; debe ajustarse antes de presentarla porque superarlo es causal de descalificación según el DBC",
                "Se negocia el precio después de adjudicar",
                "No importa, el referencial es sólo orientativo"
            ), 1,
            "El precio referencial es público (Art. 16 II). Una propuesta económica que lo supera es descalificada según el DBC; hay que reprogramar cuadrillas/rendimientos y ajustar precios.",
            "Art. 16; $DBC", EXAMEN
        ),
        om(
            Temas.CRONOGRAMA,
            "Si una actividad de 107 días se ejecuta con 4 cuadrillas iguales, su duración aproximada es:",
            listOf("107 días", "27 días", "26 días", "54 días"), 1,
            "Duración = días base / cuadrillas = 107 / 4 = 26,75 → se redondea hacia arriba: 27 días.",
            "Programación de obras", EXAMEN
        ),

        // ---- Impugnación ----
        om(
            Temas.IMPUGNACION,
            "En Licitación Pública procede el Recurso Administrativo de Impugnación contra:",
            listOf(
                "Informes de evaluación",
                "Resolución que aprueba el DBC, Resolución de Adjudicación y Resolución de Declaratoria Desierta",
                "Cualquier acto de mero trámite",
                "Sólo la Resolución de Adjudicación"
            ), 1,
            "LP: Resolución que aprueba el DBC, de Adjudicación y de Declaratoria Desierta. ANPE > Bs 200.000: Adjudicación y Declaratoria Desierta. No procede contra actos preparatorios, informes ni dictámenes.",
            "Art. 90"
        )
    )

    // -----------------------------------------------------------------------
    // LECCIONES
    // -----------------------------------------------------------------------
    val lecciones: List<LeccionEntity> by lazy { leccionesBase + ContenidoContrataciones.lecciones + ContenidoEtica.lecciones }

    private val leccionesBase: List<LeccionEntity> = listOf(
        LeccionEntity(
            modulo = Modulos.NORMATIVA, orden = 1,
            titulo = "Mapa general del SABS",
            contenido = """
El Sistema de Administración de Bienes y Servicios (SABS) tiene tres subsistemas:
1. Contratación de bienes y servicios.
2. Manejo de bienes.
3. Disposición de bienes.

Actores clave en contratación:
• MAE: responsable de todos los procesos, designa RPC/RPA, suscribe contratos (Art. 32).
• RPC (Licitación Pública) y RPA (ANPE): conducen el proceso y adjudican (Arts. 33-34).
• Unidad Solicitante: elabora especificaciones técnicas y calcula el Precio Referencial (Arts. 16, 35).
• Comisión de Calificación / Responsable de Evaluación (Art. 38).
• Comisión / Responsable de Recepción (Art. 39).

Plazos: siempre días hábiles administrativos (lunes a viernes) salvo que diga 'calendario' (Art. 15).
            """.trimIndent(),
            fundamento = "Arts. 1, 15, 32-39"
        ),
        LeccionEntity(
            modulo = Modulos.NORMATIVA, orden = 2,
            titulo = "Modalidades y cuantías (Art. 13)",
            contenido = """
• Contratación Menor: de Bs 1 hasta Bs 50.000.
• ANPE (Apoyo Nacional a la Producción y Empleo): más de Bs 50.000 hasta Bs 1.000.000.
• Licitación Pública: más de Bs 1.000.000.
• Excepción, Desastres y/o Emergencias, Contratación Directa: sin límite de monto.

Truco para recordar: 50 mil – 1 millón. Por debajo, Menor; entre ambos, ANPE; por encima, Licitación.

Otros números que se preguntan:
• Publicación en SICOES: contrataciones mayores a Bs 20.000 (Art. 49).
• Mesa de Partes: convocatorias mayores a Bs 50.000 (Art. 48).
• Impugnación en ANPE: sólo para montos mayores a Bs 200.000 (Art. 90).
• Precio Referencial de obras: antigüedad máxima 4 meses (Art. 16 IV).
            """.trimIndent(),
            fundamento = "Arts. 13, 16, 48, 49, 90"
        ),
        LeccionEntity(
            modulo = Modulos.NORMATIVA, orden = 3,
            titulo = "Modificaciones al contrato (Art. 89) — el tema estrella",
            contenido = """
Regla base (89 I): toda modificación debe servir al objeto del contrato y estar sustentada por INFORME TÉCNICO y LEGAL (viabilidad técnica y de financiamiento). En proyectos de inversión, cumplir el SNIP.

Instrumentos (89 II):
a) CONTRATO MODIFICATORIO
   • Afecta alcance, monto y/o plazo.
   • NO incrementa precios unitarios.
   • Uno o varios, sumados ≤ 10% del contrato principal.
   • Ítems nuevos: precios unitarios NEGOCIADOS.
   • Lo firma la MAE o quien firmó el contrato principal.

b) CM Consultor Individual de Línea: amplía plazo, máximo 2 veces.
c) CM Servicios Generales Recurrentes: amplía plazo, 1 sola vez.

d) ORDEN DE CAMBIO (sólo OBRAS)
   • Modifica precio o plazo.
   • NO incrementa precios unitarios NI crea ítems nuevos.
   • ≤ 5% del contrato principal.
   • La aprueba la instancia responsable del seguimiento de la obra.

Topes conjuntos OC + CM:
   • Licitación Pública: ≤ 15%.
   • ANPE: ≤ 10%.

En el DBC de obras existe además la ORDEN DE TRABAJO: ajustes o redistribución de cantidades sin cambiar monto ni plazo.

Árbol de decisión:
¿Cambia monto o plazo? NO → Orden de Trabajo.
SÍ → ¿Hay ítems nuevos? SÍ → Contrato Modificatorio.
NO → ¿Acumulado OC ≤ 5% y total OC+CM dentro del tope? SÍ → Orden de Cambio; NO → Contrato Modificatorio (si cabe en 10%).
¿Se piden precios unitarios más altos? → NUNCA procede.
            """.trimIndent(),
            fundamento = "Art. 89"
        ),
        LeccionEntity(
            modulo = Modulos.NORMATIVA, orden = 4,
            titulo = "Garantías y anticipo",
            contenido = """
Tipos (Art. 20): Boleta de Garantía, Garantía a Primer Requerimiento, Póliza de Seguro de Caución a Primer Requerimiento. Deben ser renovables, irrevocables y de ejecución inmediata. El proponente elige el tipo.

Según el objeto (Art. 21):
• Seriedad de Propuesta: 1% del precio referencial (0,5% consultoría). Vigencia = validez de propuesta + 30 días calendario. En ANPE sólo si PR > Bs 200.000.
• Cumplimiento de Contrato: 7% del monto. Hasta Bs 1.000.000 con pagos parciales → retención del 7%. MyPEs/OECAs hasta Bs 1.000.000 → 3,5%. Vigente hasta la recepción definitiva.
• Garantía Adicional (obras): si la propuesta < 85% del PR → diferencia entre 85% PR y la propuesta.
• Funcionamiento de Maquinaria: hasta 1,5%.
• Correcta Inversión de Anticipo: 100% del anticipo, vigencia mínima 90 días calendario.

Anticipo (Art. 19): máximo 20% del contrato (50% en inmuebles en construcción). En obras se descuenta desde el primer certificado de avance.

Ejemplo: PR = 1.500.000; propuesta = 1.200.000 → 85% PR = 1.275.000 → garantía adicional = 75.000.
            """.trimIndent(),
            fundamento = "Arts. 19-21"
        ),
        LeccionEntity(
            modulo = Modulos.NORMATIVA, orden = 5,
            titulo = "Impedimentos y conflicto de intereses",
            contenido = """
Art. 43 – impedidos para participar, entre otros:
a) Deudas con el Estado (pliegos de cargo ejecutoriados).
f) Parentesco o matrimonio con la MAE hasta 3er grado de consanguinidad y 2do de afinidad.
g) Ex servidores de la entidad hasta 1 año antes de la convocatoria.
h) Servidores en funciones de la entidad.
i) Adjudicados que desistieron de firmar: 1 año.
j) Contrato resuelto por su culpa: 3 años (orden de compra/servicio incumplida: 1 año).

Art. 44 – quien asesora a la entidad en un proceso no puede participar en él; quien ejecuta la obra no puede ser su consultor y viceversa.

Cómo responder un caso: (1) identifica qué inciso podría aplicar; (2) compara el plazo o grado de parentesco con lo que dice la norma; (3) concluye si se adjudica o no y CITA el inciso.
            """.trimIndent(),
            fundamento = "Arts. 43-44"
        ),
        LeccionEntity(
            modulo = Modulos.NORMATIVA, orden = 6,
            titulo = "Contrato, recepción e impugnación",
            contenido = """
• Naturaleza administrativa (Art. 85).
• Documentos integrantes: DBC, propuesta adjudicada, resolución de adjudicación, poder, garantías, póliza, asociación accidental, otros (Art. 86).
• Cláusulas mínimas: antecedentes, partes, legislación, documentos, objeto y causa, garantías, precio, vigencia, obligaciones, multas, entrega y recepción, terminación, controversias, consentimiento (Art. 87).
• Protocolización: contratos ≥ Bs 1.000.000 ante Notaría de Gobierno; documentos en 10 días; costo del contratista (Art. 88).
• Recepción de obras: provisional y definitiva con actas (Art. 39 III). El DBC limita la definitiva a 180 días calendario.
• Impugnación (Art. 90): LP contra aprobación del DBC, adjudicación y declaratoria desierta; ANPE > Bs 200.000 contra adjudicación y declaratoria desierta.
            """.trimIndent(),
            fundamento = "Arts. 39, 85-90"
        ),

        // ---- Libro de Órdenes ----
        LeccionEntity(
            modulo = Modulos.LIBRO, orden = 1,
            titulo = "¿Qué es el Libro de Órdenes?",
            contenido = """
Es el documento oficial de comunicación entre el SUPERVISOR y el CONTRATISTA (a través de su Superintendente/Residente) durante la ejecución de la obra.

Características (según el modelo de contrato del DBC de obras):
• Se abre con la Orden de Proceder y permanece en obra.
• Hojas numeradas correlativamente, en original y copias (entidad, supervisor, contratista).
• No se arrancan hojas ni se borra: un error se anula con una línea y se escribe un nuevo asiento.
• Se cierra con la recepción definitiva.

Quién escribe qué:
• SUPERVISOR: órdenes, instrucciones, observaciones de calidad, aprobaciones, Órdenes de Cambio.
• CONTRATISTA: comunicaciones, solicitudes, consultas, reclamos y solicitudes de ampliación de plazo. NO emite órdenes.
• FISCAL de obra (entidad): toma conocimiento y aprueba las Órdenes de Cambio (Art. 89 II d).

Importante: la NB-SABS no fija el formato del libro; lo regulan el DBC y el contrato. Verifica siempre las cláusulas de tu contrato.
            """.trimIndent(),
            fundamento = DBC
        ),
        LeccionEntity(
            modulo = Modulos.LIBRO, orden = 2,
            titulo = "Cómo llenar un asiento, campo por campo",
            contenido = """
1. N° de asiento: correlativo al anterior. Nunca saltar números.
2. Fecha (dd/mm/aaaa) y hora: obligatorias. La fecha no puede ser anterior al último asiento ni futura. Desde ella corren los plazos.
3. De: Supervisor o Contratista, con nombre y cargo de quien firma.
4. A: destinatario (distinto del emisor).
5. Tipo de asiento: orden, solicitud, observación, Orden de Cambio, ampliación de plazo, paralización, aprobación.
6. Ítem / frente de obra: el ítem del presupuesto o el lugar exacto (eje, nivel, progresiva).
7. Texto: instrucción clara (ver lección siguiente).
8. Plazo de cumplimiento en días: obligatorio en órdenes, observaciones, Órdenes de Cambio y paralizaciones.
9. Rúbrica (firma) del emisor: sin firma el asiento no tiene validez.
10. Firma de recepción del destinatario: constancia de que fue notificado; desde allí corre el plazo.

Ejemplo modelo:
Asiento N° 15 – 12/03/2026 – 09:30
De: Ing. Supervisor  A: Superintendente
Tipo: Orden  Ítem: 14 Columnas de H°A°
'Se instruye retirar y recolocar los estribos de la columna C-4 (eje B-3), actualmente a 25 cm, a una separación de 15 cm conforme plano E-02, antes del vaciado. Supervisión verificará antes de autorizar el hormigonado.'
Plazo: 2 días.  Firma Supervisor ✓  Recibido Superintendente ✓
            """.trimIndent(),
            fundamento = DBC
        ),
        LeccionEntity(
            modulo = Modulos.LIBRO, orden = 3,
            titulo = "Redacción clara: la fórmula QUÉ–DÓNDE–CÓMO–PLAZO–VERIFICACIÓN",
            contenido = """
Una orden válida responde a cinco preguntas:
• QUÉ hacer (verbo de acción: ejecutar, retirar, reemplazar, demoler, colocar, vaciar, presentar, suspender…).
• DÓNDE (ítem, eje, nivel, progresiva).
• CÓMO (plano, especificación técnica, norma de referencia).
• PLAZO (días).
• VERIFICACIÓN (quién y cuándo controla).

Expresiones prohibidas porque generan controversia:
'lo antes posible', 'a la brevedad', 'según criterio', 'más o menos', 'aproximadamente', 'etc.', 'ver tema', 'si es posible', 'cuando pueda'.

Mal: 'Mejorar el encofrado lo antes posible.'
Bien: 'Se instruye reemplazar los tableros deformados del encofrado de la losa entre ejes B-C/2-3 (deformación > 5 mm), conforme especificación técnica ítem 12, en un plazo de 2 días. Supervisión verificará antes del vaciado.'
            """.trimIndent(),
            fundamento = DBC
        ),
        LeccionEntity(
            modulo = Modulos.LIBRO, orden = 4,
            titulo = "Del Libro de Órdenes a la Orden de Cambio",
            contenido = """
Cuando un asiento implica cambiar monto o plazo, la Orden de Cambio debe cumplir:
✓ Sólo obras.
✓ Sin nuevos ítems y sin subir precios unitarios.
✓ Acumulado de OC ≤ 5% del contrato.
✓ OC + CM ≤ 15% (LP) o ≤ 10% (ANPE).
✓ Informe técnico y legal (Art. 89 I).
✓ Aprobación de la instancia responsable del seguimiento (Fiscal).

Cálculo:
% OC = (OC previas + OC actual) / Monto contrato × 100
% Total = (OC acumuladas + CM acumulados) / Monto contrato × 100

Ejemplo: contrato LP Bs 2.000.000, OC previas 60.000, nueva OC 30.000 → 4,5% ✓. Si fuera 50.000 → 5,5% ✗ → Contrato Modificatorio.
            """.trimIndent(),
            fundamento = "Art. 89"
        ),
        LeccionEntity(
            modulo = Modulos.LIBRO, orden = 5,
            titulo = "Caso de examen: napa freática en excavación",
            contenido = """
Asiento del contratista: 'A 1,60 m apareció la napa freática; continuamos hasta 3 m y la maquinaria no puede seguir. Solicitamos instrucciones; faltan 3 m para el nivel de fundación.'

Respuesta modelo del Supervisor (asiento tipo Orden):
'En atención al asiento N° X, se instruye: 1) instalar sistema de agotamiento mediante bombas sumergibles en sumideros perimetrales, manteniendo el nivel freático 0,50 m bajo el fondo de excavación; 2) continuar la excavación del ítem «Excavación lecho aluvial prof. 6 m» hasta la cota de fundación según plano; 3) presentar en 2 días el procedimiento de entibado y el registro diario de horas de bombeo. Supervisión verificará niveles diariamente.' Plazo: 2 días.

¿Cómo se paga?
• Si el contrato ya tiene ítem de agotamiento → mayores volúmenes con Orden de Cambio (≤5%) a precios del contrato.
• Si no existe el ítem → ítem nuevo → Contrato Modificatorio con precio negociado.
• Nunca se incrementan precios unitarios (Art. 89 II a y d).
• En ambos casos: informe técnico y legal (Art. 89 I).
            """.trimIndent(),
            fundamento = "Art. 89; examen pasado"
        ),
        LeccionEntity(
            modulo = Modulos.LIBRO, orden = 6,
            titulo = "Caso de examen: ampliación de plazo por lluvias",
            contenido = """
El contratista pide 30 días porque las lluvias superaron los registros de los últimos 10 años.

Documentación técnica que debe presentar:
1. Certificación de registros pluviométricos oficiales (SENAMHI) del periodo, comparada con la serie histórica de 10 años.
2. Asientos del Libro de Órdenes donde se registró cada día de lluvia y su efecto en los trabajos.
3. Informe técnico que cuantifique los días efectivamente perdidos y demuestre que afectaron actividades de la RUTA CRÍTICA.
4. Cronograma reprogramado.
5. Fotografías fechadas.

El Supervisor evalúa, emite informe y, si corresponde, se tramita Orden de Cambio (plazo, ≤5%) o Contrato Modificatorio, con informe técnico y legal.
            """.trimIndent(),
            fundamento = "Art. 89 I; examen pasado"
        ),

        // ---- Cómputos ----
        LeccionEntity(
            modulo = Modulos.COMPUTOS, orden = 1,
            titulo = "Método para cubicar cualquier pieza",
            contenido = """
1. Descompón la pieza en cuerpos simples (prismas, troncos de pirámide, cilindros, sectores).
2. Para piezas lineales (muros, vigas, fundaciones corridas): V = Área de la sección × Longitud del eje.
   • Muros en L o T: suma de rectángulos de la sección.
   • Esquinas: mide por el EJE para no contar dos veces el encuentro.
3. Para piezas de revolución o curvas (Teorema de Pappus): V = Área × 2π·r̄ (r̄ = distancia del centroide al eje). Un cuarto de círculo usa la cuarta parte: V = A × (π·r̄/2).
4. Transiciones entre dos secciones distintas: fórmula prismoidal V = L/6·(A1 + 4·Am + A2), con Am = sección intermedia.
5. Zapatas: prisma + tronco de pirámide V = h/3·(A1 + A2 + √(A1·A2)).
6. Huecos (ductos, aberturas): réstalos.
7. Costo = Volumen × Precio unitario.

Siempre escribe unidades y redondea al final (no en pasos intermedios).
            """.trimIndent(),
            fundamento = "Cómputos métricos"
        ),
        LeccionEntity(
            modulo = Modulos.COMPUTOS, orden = 2,
            titulo = "Ejercicio resuelto: muro de contención",
            contenido = """
Sección N° 1: pantalla 0,50 × 4,27 m; zapata 2,00 × 0,50 m. Tramo de 5 m. H°A° = 4000 Bs/m³.

A = 0,50 × 4,27 + 2,00 × 0,50 = 2,135 + 1,000 = 3,135 m²
V = 3,135 × 5 = 15,675 m³
Costo = 15,675 × 4000 = Bs 62.700

Si el siguiente tramo es una transición entre Sección 1 y Sección 2 (distinta), usa la fórmula prismoidal con la sección media Am (promedio de cada dimensión), o como aproximación V ≈ L·(A1 + A2)/2.
            """.trimIndent(),
            fundamento = "Examen pasado"
        ),
        LeccionEntity(
            modulo = Modulos.COMPUTOS, orden = 3,
            titulo = "Ejercicio resuelto: zapatas aisladas",
            contenido = """
10 zapatas: base 1,20 × 1,20 × 0,30 m y tronco de pirámide de 0,50 m de alto que remata en 0,40 × 0,40 m.

Prisma: 1,20 × 1,20 × 0,30 = 0,432 m³
Tronco: A1 = 1,44; A2 = 0,16; √(A1·A2) = 0,48
        V = 0,50/3 × (1,44 + 0,16 + 0,48) = 0,3467 m³
Una zapata: 0,7787 m³ → 10 zapatas: 7,787 m³

(Ejemplo tipo examen; usa la calculadora 'Zapata aislada' para tus propias medidas.)
            """.trimIndent(),
            fundamento = "Cómputos métricos"
        ),
        LeccionEntity(
            modulo = Modulos.COMPUTOS, orden = 4,
            titulo = "Rendimientos: cemento, mano de obra y madera",
            contenido = """
Rendimiento = cantidad de recurso / cantidad de obra.

Ejemplo (examen): sobrecimiento de 100 m, sección 0,40 × 0,80.
V = 100 × 0,40 × 0,80 = 32 m³
Cemento: 64 bolsas / 32 m³ = 2 bolsas/m³ = 100 kg/m³
Mano de obra: 18 jornales / 32 m³ = 0,5625 jornal/m³ × 8 h = 4,5 h/m³

Madera de encofrado (examen): sobrecimiento 0,20 × 0,45 × 5 m (V = 0,45 m³)
Tablones: 2 caras × 0,50 × 5,0 = 5,0 m² × 10,76 = 53,80 pie²
Listones 2"×2": 10 × 0,60 = 6,0 m; ancho equivalente 4" = 0,1016 m → 0,6096 m² = 6,56 pie²
Total = 60,36 pie² / 0,45 m³ = 134,13 pie²/m³ (un solo uso)
            """.trimIndent(),
            fundamento = "Examen pasado"
        ),
        LeccionEntity(
            modulo = Modulos.COMPUTOS, orden = 5,
            titulo = "Precio unitario, IVA e IT compensados",
            contenido = """
Impuesto compensado: c = i / (1 − i)
• IVA 14% → 16,28%   • IVA 15% → 17,65%
• IT 5% → 5,26%      • IT 4% → 4,17%
Inverso: i = c / (1 + c). Ej.: c = 16,96% → i = 14,5%; c = 21,21% → 17,5%.

Estructura del precio unitario (Formulario B-2):
1. Materiales (M).
2. Mano de obra: MO + cargas sociales (% MO) + IVA compensado sobre (MO + CS) = MO total.
3. Equipo y herramientas: equipo + herramientas menores (% de MO total).
4. Gastos generales: % de (1 + 2 + 3).
5. Utilidad: % de (1 + 2 + 3 + 4).
6. IT compensado: % de (1 + … + 5).
PU = suma de 1 a 6.

Usa la fórmula 'Precio unitario (B-2)' de la calculadora.
            """.trimIndent(),
            fundamento = "Análisis de precios unitarios"
        ),
        LeccionEntity(
            modulo = Modulos.COMPUTOS, orden = 6,
            titulo = "Cronograma y ruta crítica paso a paso",
            contenido = """
1. Ordena las actividades lógicamente (Instalación de faenas → Replanteo → Desbroce → Excavación → … → Limpieza). Respeta notas del enunciado (ej.: martillo después de retroexcavadora).
2. Asigna predecesoras.
3. Duración = días base / cuadrillas (redondea hacia arriba).
4. Pasada adelante: IT (inicio temprano) = máximo FT de predecesoras; FT = IT + duración.
5. Pasada atrás: FTd (fin tardío) = mínimo ITd de sucesoras; ITd = FTd − duración.
6. Holgura = ITd − IT. Holgura 0 → ruta crítica.
7. Si la duración total supera el plazo referencial, aumenta cuadrillas en actividades CRÍTICAS.
8. Monto de la propuesta = Σ volumen × precio unitario. Debe ser ≤ precio referencial.

Practícalo en la pestaña 'Cronograma' con la tabla del examen ya cargada.
            """.trimIndent(),
            fundamento = "Programación de obras"
        )
    )

    // -----------------------------------------------------------------------
    // PLANTILLAS (MODELOS CORRECTOS) DE LIBRO DE ÓRDENES
    // -----------------------------------------------------------------------
    val plantillasAsiento: List<PlantillaAsientoEntity> = listOf(
        PlantillaAsientoEntity(
            titulo = "Corrección de armadura antes del vaciado",
            escenario = "En la inspección previa al vaciado, el Supervisor detecta estribos a 25 cm en la columna C-4 cuando el plano E-02 exige 15 cm.",
            emisor = Emisores.SUPERVISOR, nombreEmisor = "Ing. Supervisor de obra",
            destinatario = "Superintendente de obra", tipoAsiento = TiposAsiento.ORDEN,
            itemReferencia = "Ítem 14 – Columnas de H°A° (C-4, eje B-3)",
            instruccion = "Se instruye retirar y recolocar los estribos de la columna C-4 del eje B-3, actualmente a 25 cm, a una separación de 15 cm conforme plano estructural E-02, antes del vaciado. Supervisión verificará la armadura antes de autorizar el hormigonado.",
            plazoDias = 2, fundamento = DBC
        ),
        PlantillaAsientoEntity(
            titulo = "Napa freática: solicitud del contratista",
            escenario = "Durante la excavación de tanques enterrados aparece la napa freática a 1,60 m; la maquinaria no puede continuar y faltan 3 m para el nivel de fundación.",
            emisor = Emisores.CONTRATISTA, nombreEmisor = "Ing. Superintendente",
            destinatario = "Supervisor de obra", tipoAsiento = TiposAsiento.SOLICITUD,
            itemReferencia = "Ítem 5 – Excavación lecho aluvial prof. 6 m",
            instruccion = "Se informa que en la excavación del ítem 5 se encontró la napa freática a 1,60 m de profundidad; se excavó hasta 3,00 m y la maquinaria no puede continuar por el nivel de agua. Se solicita a Supervisión instruir el procedimiento de agotamiento para alcanzar el nivel de fundación de los tanques.",
            plazoDias = null, fundamento = "Examen pasado"
        ),
        PlantillaAsientoEntity(
            titulo = "Napa freática: respuesta del Supervisor",
            escenario = "El Supervisor debe responder la solicitud anterior con una instrucción clara y ejecutable.",
            emisor = Emisores.SUPERVISOR, nombreEmisor = "Ing. Supervisor de obra",
            destinatario = "Superintendente de obra", tipoAsiento = TiposAsiento.ORDEN,
            itemReferencia = "Ítem 5 – Excavación lecho aluvial prof. 6 m",
            instruccion = "En atención a la solicitud del contratista, se instruye instalar bombas sumergibles en sumideros perimetrales para mantener el nivel freático 0,50 m bajo el fondo de excavación, continuar la excavación hasta la cota de fundación según plano y presentar el procedimiento de entibado y el registro diario de horas de bombeo. Supervisión verificará niveles diariamente.",
            plazoDias = 2, fundamento = "Art. 89; examen pasado"
        ),
        PlantillaAsientoEntity(
            titulo = "Orden de Cambio por mayores volúmenes",
            escenario = "Por la profundidad real del estrato firme, la excavación del ítem 4 aumenta en volumen. Se mantiene el precio unitario del contrato y no hay ítems nuevos.",
            emisor = Emisores.SUPERVISOR, nombreEmisor = "Ing. Supervisor de obra",
            destinatario = "Superintendente de obra", tipoAsiento = TiposAsiento.ORDEN_CAMBIO,
            itemReferencia = "Ítem 4 – Excavación suelo duro con retroexcavadora",
            instruccion = "Se emite Orden de Cambio N° 1 para ejecutar 350 m3 adicionales del ítem 4 a precio unitario de contrato, por cambio de cota de fundación según informe técnico N° 12/2026, sin creación de ítems nuevos. Supervisión verificará cómputos en planilla.",
            plazoDias = 10, fundamento = "Art. 89 II d)",
            montoContrato = 2_000_000.0, montoVariacion = 60_000.0,
            acumuladoOrdenesCambio = 0.0, acumuladoContratosModificatorios = 0.0,
            modalidad = Modalidades.LICITACION
        ),
        PlantillaAsientoEntity(
            titulo = "Solicitud de ampliación de plazo por lluvias",
            escenario = "Lluvias extraordinarias superaron los registros de los últimos 10 años. El contratista pide 30 días de ampliación.",
            emisor = Emisores.CONTRATISTA, nombreEmisor = "Ing. Superintendente",
            destinatario = "Supervisor de obra", tipoAsiento = TiposAsiento.AMPLIACION_PLAZO,
            itemReferencia = "Actividades críticas: excavaciones y fundaciones",
            instruccion = "Se solicita ampliación de plazo de 30 días calendario por caso fortuito: precipitaciones superiores a la serie histórica de 10 años. Se adjunta certificación de registros pluviométricos del SENAMHI, informe técnico de días perdidos en actividades de la ruta crítica, cronograma reprogramado y fotografías fechadas.",
            plazoDias = null, fundamento = "Art. 89 I; examen pasado"
        ),
        PlantillaAsientoEntity(
            titulo = "Paralización por seguridad",
            escenario = "Se detecta un talud inestable junto a la excavación con personal trabajando en el fondo.",
            emisor = Emisores.SUPERVISOR, nombreEmisor = "Ing. Supervisor de obra",
            destinatario = "Superintendente de obra", tipoAsiento = TiposAsiento.PARALIZACION,
            itemReferencia = "Ítem 5 – Excavación, frente norte progresiva 0+120",
            instruccion = "Se instruye suspender los trabajos de excavación del frente norte (progresiva 0+120) por riesgo de seguridad: talud inestable sin entibado. Retirar al personal, ejecutar entibado conforme especificación técnica y solicitar verificación de Supervisión para reanudar.",
            plazoDias = 1, fundamento = DBC
        ),
        PlantillaAsientoEntity(
            titulo = "Observación de calidad del hormigón",
            escenario = "Las probetas a 7 días del vaciado de la losa del nivel 1 muestran resistencia por debajo de lo esperado.",
            emisor = Emisores.SUPERVISOR, nombreEmisor = "Ing. Supervisor de obra",
            destinatario = "Superintendente de obra", tipoAsiento = TiposAsiento.OBSERVACION,
            itemReferencia = "Ítem 18 – Losa alivianada H°A° nivel 1",
            instruccion = "Se observa que las probetas a 7 días de la losa del nivel 1 alcanzaron 60% de la resistencia especificada. Se instruye presentar el informe de laboratorio, ensayar probetas a 28 días y realizar extracción de núcleos si no se alcanza la resistencia de diseño según especificación técnica. Supervisión verificará resultados.",
            plazoDias = 28, fundamento = DBC
        )
    )

    // -----------------------------------------------------------------------
    // FÓRMULAS PARAMÉTRICAS
    // -----------------------------------------------------------------------
    private fun f(
        nombre: String, categoria: String, expresion: String, unidad: String, descripcion: String,
        vars: List<Triple<String, Double, String>>
    ) = FormulaComputoEntity(
        nombre = nombre, categoria = categoria, expresion = expresion,
        variables = vars.map { it.first }, valoresPorDefecto = vars.map { it.second },
        descripcionVariables = vars.map { it.third }, unidad = unidad, descripcion = descripcion
    )

    val formulas: List<FormulaComputoEntity> = listOf(
        // Excavación
        f("Excavación de zanja (sección rectangular)", "Excavación", "L*b*h", "m³",
            "Volumen en banco de una zanja de paredes verticales.",
            listOf(Triple("L", 20.0, "Longitud (m)"), Triple("b", 0.6, "Ancho (m)"), Triple("h", 1.2, "Profundidad (m)"))),
        f("Excavación con taludes (sección trapecial)", "Excavación", "L*h*(b+z*h)", "m³",
            "Sección trapecial con talud z:1 (horizontal:vertical) en ambos lados: A = h·(b + z·h).",
            listOf(Triple("L", 20.0, "Longitud (m)"), Triple("b", 0.8, "Ancho de fondo (m)"), Triple("h", 2.0, "Profundidad (m)"), Triple("z", 0.5, "Talud horizontal por 1 vertical"))),
        f("Excavación para zapata (tronco de pirámide)", "Excavación", "n*h/3*(a1*b1+a2*b2+sqrt(a1*b1*a2*b2))", "m³",
            "Fosa con taludes: base inferior a1×b1 y superior a2×b2.",
            listOf(Triple("n", 1.0, "Cantidad"), Triple("h", 1.5, "Profundidad (m)"), Triple("a1", 1.6, "Largo fondo (m)"), Triple("b1", 1.6, "Ancho fondo (m)"), Triple("a2", 2.6, "Largo superior (m)"), Triple("b2", 2.6, "Ancho superior (m)"))),
        f("Volumen suelto (esponjamiento)", "Excavación", "V*(1+esp/100)", "m³",
            "Volumen a transportar considerando el esponjamiento del suelo.",
            listOf(Triple("V", 50.0, "Volumen en banco (m³)"), Triple("esp", 25.0, "Esponjamiento (%)"))),

        // Hormigón armado
        f("Zapata aislada (prisma + tronco de pirámide)", "Hormigón armado", "n*(B*L*e+h/3*(B*L+b*l+sqrt(B*L*b*l)))", "m³",
            "Base prismática B×L×e y tronco de pirámide de altura h que remata en b×l.",
            listOf(Triple("n", 10.0, "Cantidad de zapatas"), Triple("B", 1.2, "Ancho base (m)"), Triple("L", 1.2, "Largo base (m)"), Triple("e", 0.3, "Espesor base (m)"), Triple("h", 0.5, "Altura tronco (m)"), Triple("b", 0.4, "Ancho superior (m)"), Triple("l", 0.4, "Largo superior (m)"))),
        f("Columnas", "Hormigón armado", "n*a*b*h", "m³", "Columnas rectangulares.",
            listOf(Triple("n", 12.0, "Cantidad"), Triple("a", 0.3, "Lado a (m)"), Triple("b", 0.3, "Lado b (m)"), Triple("h", 3.0, "Altura (m)"))),
        f("Vigas", "Hormigón armado", "n*b*h*L", "m³", "Vigas rectangulares (descontar la losa si es viga T).",
            listOf(Triple("n", 4.0, "Cantidad"), Triple("b", 0.2, "Base (m)"), Triple("h", 0.4, "Altura (m)"), Triple("L", 5.0, "Longitud (m)"))),
        f("Losa maciza", "Hormigón armado", "Lx*Ly*e", "m³", "Losa de espesor constante.",
            listOf(Triple("Lx", 6.0, "Largo (m)"), Triple("Ly", 4.0, "Ancho (m)"), Triple("e", 0.15, "Espesor (m)"))),
        f("Muro en T / L (pantalla + zapata)", "Hormigón armado", "L*(t*H+B*e)", "m³",
            "Sección compuesta: pantalla t×H más zapata B×e. Ej. examen: t=0,5 H=4,27 B=2 e=0,5 L=5 → 15,675 m³.",
            listOf(Triple("L", 5.0, "Longitud (m)"), Triple("t", 0.5, "Espesor pantalla (m)"), Triple("H", 4.27, "Altura pantalla (m)"), Triple("B", 2.0, "Ancho zapata (m)"), Triple("e", 0.5, "Espesor zapata (m)"))),
        f("Muro de sección trapecial", "Hormigón armado", "L*(a+b)/2*h", "m³", "Muro con corona a, base b y altura h.",
            listOf(Triple("L", 10.0, "Longitud (m)"), Triple("a", 0.5, "Corona (m)"), Triple("b", 1.5, "Base (m)"), Triple("h", 4.0, "Altura (m)"))),
        f("Transición entre secciones (prismoidal)", "Hormigón armado", "L/6*(A1+4*Am+A2)", "m³",
            "Volumen entre dos secciones distintas A1 y A2 con sección media Am.",
            listOf(Triple("L", 5.0, "Longitud del tramo (m)"), Triple("A1", 3.135, "Área sección inicial (m²)"), Triple("Am", 2.9, "Área sección media (m²)"), Triple("A2", 2.7, "Área sección final (m²)"))),
        f("Pieza curva por Pappus (fracción de giro)", "Hormigón armado", "A*rc*ang*pi/180", "m³",
            "V = Área × longitud del arco recorrido por el centroide. Cuarto de círculo: ang = 90.",
            listOf(Triple("A", 2.35, "Área de la sección (m²)"), Triple("rc", 19.0, "Radio al centroide (m)"), Triple("ang", 90.0, "Ángulo barrido (°)"))),
        f("Cilindro / anillo (tanque, ducto)", "Hormigón armado", "pi*(R^2-r^2)*h", "m³", "Anillo circular; con r = 0 es un cilindro macizo.",
            listOf(Triple("R", 2.0, "Radio exterior (m)"), Triple("r", 1.2, "Radio interior (m)"), Triple("h", 1.0, "Longitud o altura (m)"))),
        f("Acero de refuerzo (peso)", "Hormigón armado", "n*Lb*d^2/162", "kg",
            "Peso lineal ≈ d²/162 kg/m (d en mm).",
            listOf(Triple("n", 40.0, "Número de barras"), Triple("Lb", 6.0, "Longitud por barra (m)"), Triple("d", 12.0, "Diámetro (mm)"))),
        f("Cemento para hormigón", "Hormigón armado", "V*dos/50", "bolsas",
            "Bolsas de 50 kg según la dosificación en kg/m³.",
            listOf(Triple("V", 32.0, "Volumen (m³)"), Triple("dos", 350.0, "Cemento (kg/m³)"))),

        // Encofrado
        f("Encofrado de columnas", "Encofrado", "n*2*(a+b)*h", "m²", "Perímetro × altura.",
            listOf(Triple("n", 12.0, "Cantidad"), Triple("a", 0.3, "Lado a (m)"), Triple("b", 0.3, "Lado b (m)"), Triple("h", 3.0, "Altura (m)"))),
        f("Encofrado de vigas", "Encofrado", "n*(b+2*h)*L", "m²", "Fondo + dos costados.",
            listOf(Triple("n", 4.0, "Cantidad"), Triple("b", 0.2, "Base (m)"), Triple("h", 0.4, "Altura (m)"), Triple("L", 5.0, "Longitud (m)"))),
        f("Encofrado de muros (dos caras)", "Encofrado", "2*L*H", "m²", "Dos caras del muro.",
            listOf(Triple("L", 10.0, "Longitud (m)"), Triple("H", 3.0, "Altura (m)"))),
        f("Rendimiento madera de encofrado", "Encofrado", "(Atab+Alist)*10.764/V", "pie²/m³",
            "Ej. examen: Atab=5,0 m², Alist=0,6096 m², V=0,45 m³ → 134,13 pie²/m³.",
            listOf(Triple("Atab", 5.0, "Área de tablones (m²)"), Triple("Alist", 0.6096, "Área equivalente listones (m²)"), Triple("V", 0.45, "Volumen hormigonado (m³)"))),

        // Costos
        f("Costo de un ítem", "Costos", "V*PU", "Bs", "Cantidad × precio unitario.",
            listOf(Triple("V", 15.675, "Cantidad"), Triple("PU", 4000.0, "Precio unitario (Bs)"))),
        f("Impuesto compensado", "Costos", "100*i/(100-i)", "%", "IVA 14% → 16,28%; IT 4% → 4,17%.",
            listOf(Triple("i", 14.0, "Tasa nominal (%)"))),
        f("Impuesto nominal desde compensado", "Costos", "100*c/(100+c)", "%", "Compensado 16,96% → 14,5%.",
            listOf(Triple("c", 16.96, "Tasa compensada (%)"))),
        f("Precio unitario (Formulario B-2)", "Costos",
            "(M+MO*(1+cs/100)*(1+iva/100)*(1+herr/100)+E)*(1+gg/100)*(1+ut/100)*(1+it/100)", "Bs",
            "M materiales, MO mano de obra, E equipo; cargas sociales e IVA compensado sobre MO; herramientas % de MO total; GG, utilidad e IT compensado en cascada.",
            listOf(Triple("M", 500.0, "Materiales (Bs)"), Triple("MO", 200.0, "Mano de obra (Bs)"), Triple("E", 50.0, "Equipo (Bs)"),
                Triple("cs", 66.0, "Cargas sociales (%)"), Triple("iva", 14.94, "IVA compensado de MO (%)"), Triple("herr", 5.0, "Herramientas (%)"),
                Triple("gg", 8.0, "Gastos generales (%)"), Triple("ut", 9.0, "Utilidad (%)"), Triple("it", 3.09, "IT compensado (%)"))),
        f("Rendimiento de recurso", "Costos", "Q/V", "unid/m³", "Recurso usado / volumen ejecutado. Ej.: 64 bolsas / 32 m³.",
            listOf(Triple("Q", 64.0, "Cantidad de recurso"), Triple("V", 32.0, "Volumen (m³)"))),
        f("Horas de mano de obra por m³", "Costos", "J*hj/V", "h/m³", "Ej.: 18 jornales × 8 h / 32 m³ = 4,5 h/m³.",
            listOf(Triple("J", 18.0, "Jornales"), Triple("hj", 8.0, "Horas por jornal"), Triple("V", 32.0, "Volumen (m³)")))
    )
}
