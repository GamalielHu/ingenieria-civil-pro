# Ingeniería Civil Pro: Normativa, Órdenes y Cómputos

App Android nativa (Kotlin, Jetpack Compose, Material 3, Navigation, Room, MVVM con StateFlow) para estudiar
**Dirección / Gerencia de Obras** con la normativa boliviana **NB-SABS (DS 0181, compilado 2023)**.

## Módulos

| Módulo | Archivo | Qué hace |
|---|---|---|
| Normativa | `ui/screens/NormativaScreen.kt` | Lecciones (Contratación + Ética), quiz con validación instantánea, explicación y artículo de fundamento, repaso de falladas y progreso por tema. Incluye preguntas de exámenes pasados. |
| Libro de Órdenes | `ui/screens/LibroOrdenesScreen.kt` | Guía de llenado, simulador Supervisor vs. Contratista con validación normativa (fecha, correlatividad, rúbricas, plazo, instrucción clara, competencia del emisor, topes del Art. 89 para Órdenes de Cambio), modelos correctos y ejercicios con inconsistencias generadas al azar. |
| Cómputos | `ui/screens/VolumenesObraScreen.kt` | Fórmulas paramétricas editables (excavación, H°A°, encofrado, costos, IVA/IT compensado, precio unitario B-2) y cronograma con ruta crítica (CPM) precargado con la pregunta de examen. |

Persistencia: `data/local/EngineeringDatabase.kt` (preguntas, intentos, lecciones, plantillas y asientos del libro, fórmulas).
Lógica: `ui/viewmodel/EngineeringViewModel.kt`.

## Obtener el APK

Cada push a `main` ejecuta **GitHub Actions → Compilar APK**. Abre la ejecución más reciente en la pestaña *Actions* y descarga el artefacto `IngenieriaCivilPro-debug` (zip con `app-debug.apk`).

## Compilar localmente

Android Studio (Ladybug o superior) o `./gradlew assembleDebug` con JDK 17 y Android SDK 35.

> Nota: la NB-SABS no regula el formato del Libro de Órdenes; las reglas del simulador se basan en el modelo de contrato del DBC de obras y en el Art. 89. Verifica siempre las cláusulas de tu contrato.
