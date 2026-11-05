package com.gamaliel.ingcivilpro.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gamaliel.ingcivilpro.data.local.Modulos
import com.gamaliel.ingcivilpro.data.local.PreguntaEntity
import com.gamaliel.ingcivilpro.data.local.TiposPregunta
import com.gamaliel.ingcivilpro.ui.theme.RojoAlerta
import com.gamaliel.ingcivilpro.ui.theme.VerdeConforme
import com.gamaliel.ingcivilpro.ui.viewmodel.EngineeringViewModel
import com.gamaliel.ingcivilpro.ui.viewmodel.EstadisticaTema
import com.gamaliel.ingcivilpro.ui.viewmodel.QuizUiState

@Composable
fun NormativaScreen(viewModel: EngineeringViewModel) {
    val quiz by viewModel.quiz.collectAsStateWithLifecycle()
    val lecciones by viewModel.lecciones.collectAsStateWithLifecycle()
    var pestana by rememberSaveable { mutableIntStateOf(0) }
    val titulos = listOf("Aprender", "Quiz", "Progreso")

    Column(Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = pestana) {
            titulos.forEachIndexed { i, t -> Tab(selected = pestana == i, onClick = { pestana = i }, text = { Text(t) }) }
        }
        when (pestana) {
            0 -> ListaLecciones(
                lecciones.filter { it.modulo == Modulos.NORMATIVA },
                "NB-SABS (DS 0181): subsistema de Contratación y Anexo 1 de Ética. Toca una lección para abrirla y luego pon a prueba lo aprendido en la pestaña Quiz."
            )
            1 -> Quiz(
                estado = quiz,
                onTema = viewModel::seleccionarTema,
                onSoloFalladas = viewModel::alternarSoloFalladas,
                onElegir = viewModel::elegirOpcion,
                onResponder = viewModel::responder,
                onSiguiente = viewModel::siguientePregunta,
                onReiniciar = viewModel::reiniciarQuiz
            )
            2 -> Progreso(quiz, onReiniciar = viewModel::reiniciarEstadisticas)
        }
    }
}

@Composable
private fun Quiz(
    estado: QuizUiState,
    onTema: (String?) -> Unit,
    onSoloFalladas: (Boolean) -> Unit,
    onElegir: (Int) -> Unit,
    onResponder: () -> Unit,
    onSiguiente: () -> Unit,
    onReiniciar: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Spacer(Modifier.height(8.dp))
        FiltroChips(estado.temas, estado.temaSeleccionado, onTema, etiquetaTodos = "Todos los temas")
        Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("Repasar sólo las que fallé", Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
            Switch(checked = estado.soloFalladas, onCheckedChange = onSoloFalladas)
        }
        when {
            estado.cargando -> Centrado { CircularProgressIndicator(Modifier.padding(32.dp)) }
            estado.cola.isEmpty() -> MensajeCentro(
                if (estado.soloFalladas) "No tienes preguntas falladas en este tema. ¡Bien!" else "No hay preguntas para este filtro."
            )
            estado.terminada -> ResultadoSesion(estado, onReiniciar)
            else -> TarjetaPregunta(estado, onElegir, onResponder, onSiguiente)
        }
    }
}

@Composable
private fun Centrado(content: @Composable () -> Unit) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) { content() }
}

@Composable
private fun MensajeCentro(texto: String) {
    Text(texto, Modifier.fillMaxWidth().padding(32.dp), style = MaterialTheme.typography.bodyLarge)
}

@Composable
private fun TarjetaPregunta(
    estado: QuizUiState,
    onElegir: (Int) -> Unit,
    onResponder: () -> Unit,
    onSiguiente: () -> Unit
) {
    val p: PreguntaEntity = estado.actual ?: return
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        LinearProgressIndicator(
            progress = { (estado.indice + 1).toFloat() / estado.cola.size },
            modifier = Modifier.fillMaxWidth()
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Pregunta ${estado.indice + 1} de ${estado.cola.size} · Aciertos ${estado.aciertosSesion}/${estado.respondidasSesion}",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AssistChip(onClick = {}, label = { Text(p.tema, maxLines = 1) })
            if (p.tipo == TiposPregunta.CASO) AssistChip(onClick = {}, label = { Text("Caso de estudio") })
        }
        if (p.origen.startsWith("Examen")) {
            Text("★ Pregunta de examen pasado", color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.labelLarge)
        }
        Text(p.enunciado, style = MaterialTheme.typography.titleMedium)

        p.opciones.forEachIndexed { i, opcion ->
            val esCorrecta = i == p.indiceCorrecto
            val elegida = estado.seleccion == i
            val borde = when {
                estado.respondida && esCorrecta -> BorderStroke(2.dp, VerdeConforme)
                estado.respondida && elegida -> BorderStroke(2.dp, RojoAlerta)
                elegida -> BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                else -> BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            }
            OutlinedCard(
                modifier = Modifier.fillMaxWidth().clickable(enabled = !estado.respondida) { onElegir(i) },
                border = borde
            ) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = elegida, onClick = if (estado.respondida) null else ({ onElegir(i) }))
                    Text(opcion, Modifier.weight(1f))
                    if (estado.respondida && esCorrecta) Icon(Icons.Filled.CheckCircle, null, tint = VerdeConforme)
                    if (estado.respondida && elegida && !esCorrecta) Icon(Icons.Filled.Cancel, null, tint = RojoAlerta)
                }
            }
        }

        if (!estado.respondida) {
            Button(onClick = onResponder, enabled = estado.seleccion != null, modifier = Modifier.fillMaxWidth()) {
                Text("Comprobar respuesta")
            }
        } else {
            Retroalimentacion(p, estado.esCorrecta, estado.seleccion)
            Button(onClick = onSiguiente, modifier = Modifier.fillMaxWidth()) { Text("Siguiente") }
        }
    }
}

@Composable
private fun Retroalimentacion(p: PreguntaEntity, correcta: Boolean, seleccion: Int?) {
    val color = if (correcta) VerdeConforme else RojoAlerta
    Card(colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.10f))) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                if (correcta) "¡Correcto!" else "Incorrecto",
                color = color, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium
            )
            if (!correcta && seleccion != null) {
                Text("Elegiste: \"${p.opciones[seleccion]}\"", style = MaterialTheme.typography.bodySmall)
                Text("Respuesta correcta: \"${p.opciones[p.indiceCorrecto]}\"", fontWeight = FontWeight.SemiBold)
            }
            Text(p.explicacion, style = MaterialTheme.typography.bodyMedium)
            Text("Fundamento: ${p.fundamento}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun ResultadoSesion(estado: QuizUiState, onReiniciar: () -> Unit) {
    val pct = if (estado.respondidasSesion == 0) 0 else estado.aciertosSesion * 100 / estado.respondidasSesion
    Column(
        Modifier.fillMaxWidth().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Sesión terminada", style = MaterialTheme.typography.headlineSmall)
        Text("${estado.aciertosSesion} de ${estado.respondidasSesion} correctas ($pct%)", style = MaterialTheme.typography.titleMedium)
        Text(
            when {
                pct >= 85 -> "Excelente nivel para el examen."
                pct >= 60 -> "Vas bien. Activa 'Repasar sólo las que fallé' para reforzar."
                else -> "Repasa las lecciones de la pestaña Aprender y vuelve a intentarlo."
            }
        )
        Button(onClick = onReiniciar) { Text("Nueva sesión") }
    }
}

@Composable
private fun Progreso(estado: QuizUiState, onReiniciar: () -> Unit) {
    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Banco de ${estado.totalPreguntas} preguntas", style = MaterialTheme.typography.titleMedium)
            Text("Rendimiento acumulado por tema (los temas más débiles aparecen primero).", style = MaterialTheme.typography.bodySmall)
        }
        if (estado.estadisticas.isEmpty()) {
            item { Text("Aún no respondiste preguntas.", Modifier.padding(top = 16.dp)) }
        }
        items(estado.estadisticas, key = { it.tema }) { FilaEstadistica(it) }
        item {
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = onReiniciar) { Text("Reiniciar estadísticas") }
        }
    }
}

@Composable
private fun FilaEstadistica(e: EstadisticaTema) {
    val color: Color = when {
        e.porcentaje >= 80 -> VerdeConforme
        e.porcentaje >= 50 -> MaterialTheme.colorScheme.secondary
        else -> RojoAlerta
    }
    Card {
        Column(Modifier.padding(12.dp)) {
            Row {
                Text(e.tema, Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                Text("${e.porcentaje}%", color = color, fontWeight = FontWeight.Bold)
            }
            LinearProgressIndicator(
                progress = { e.porcentaje / 100f },
                color = color,
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
            )
            Text("${e.aciertos} aciertos de ${e.respondidas} intentos", style = MaterialTheme.typography.bodySmall)
        }
    }
}
