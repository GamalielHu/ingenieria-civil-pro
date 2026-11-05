package com.gamaliel.ingcivilpro.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gamaliel.ingcivilpro.data.local.AsientoEntity
import com.gamaliel.ingcivilpro.data.local.Emisores
import com.gamaliel.ingcivilpro.data.local.Modalidades
import com.gamaliel.ingcivilpro.data.local.Modulos
import com.gamaliel.ingcivilpro.data.local.TiposAsiento
import com.gamaliel.ingcivilpro.ui.theme.AmbarAdvertencia
import com.gamaliel.ingcivilpro.ui.theme.RojoAlerta
import com.gamaliel.ingcivilpro.ui.theme.VerdeConforme
import com.gamaliel.ingcivilpro.ui.viewmodel.AsientoForm
import com.gamaliel.ingcivilpro.ui.viewmodel.EngineeringViewModel
import com.gamaliel.ingcivilpro.ui.viewmodel.Hallazgo
import com.gamaliel.ingcivilpro.ui.viewmodel.LibroUiState
import com.gamaliel.ingcivilpro.ui.viewmodel.Severidad

@Composable
fun LibroOrdenesScreen(viewModel: EngineeringViewModel) {
    val estado by viewModel.libro.collectAsStateWithLifecycle()
    val lecciones by viewModel.lecciones.collectAsStateWithLifecycle()
    var pestana by rememberSaveable { mutableIntStateOf(1) }
    val titulos = listOf("Aprender", "Simulador", "Mi libro (${estado.asientos.size})")

    Column(Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = pestana) {
            titulos.forEachIndexed { i, t -> Tab(selected = pestana == i, onClick = { pestana = i }, text = { Text(t, maxLines = 1) }) }
        }
        when (pestana) {
            0 -> ListaLecciones(
                lecciones.filter { it.modulo == Modulos.LIBRO },
                "Cómo se usa y cómo se llena el Libro de Órdenes. Practica en el Simulador: carga un modelo correcto o genera un ejercicio con errores para corregir."
            )
            1 -> Simulador(estado, viewModel)
            2 -> MiLibro(estado.asientos, onVaciar = viewModel::vaciarLibro)
        }
    }
}

@Composable
private fun Simulador(estado: LibroUiState, vm: EngineeringViewModel) {
    val f = estado.form
    var menuModelos by remember { mutableStateOf(false) }

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = vm::nuevoAsientoEnBlanco) {
                Icon(Icons.Filled.Add, null); Text("Nuevo")
            }
            Button(onClick = { vm.generarEjercicio() }) {
                Icon(Icons.Filled.AutoFixHigh, null); Text("Ejercicio")
            }
            Box {
                OutlinedButton(onClick = { menuModelos = true }) {
                    Icon(Icons.Filled.LibraryBooks, null); Text("Modelos")
                }
                DropdownMenu(expanded = menuModelos, onDismissRequest = { menuModelos = false }) {
                    estado.plantillas.forEach { p ->
                        DropdownMenuItem(
                            text = { Text(p.titulo) },
                            onClick = { menuModelos = false; vm.cargarModelo(p) }
                        )
                    }
                }
            }
        }

        estado.ejercicio?.let { ej ->
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        if (ej.resuelto) "✔ Ejercicio resuelto: ${ej.titulo}" else "Ejercicio: ${ej.titulo}",
                        fontWeight = FontWeight.Bold
                    )
                    Text("Situación: ${ej.escenario}", style = MaterialTheme.typography.bodySmall)
                    Text(
                        "Este asiento contiene ${ej.problemasOcultos.size} inconsistencias. Encuéntralas, corrígelas y presiona Validar.",
                        style = MaterialTheme.typography.bodySmall
                    )
                    TextButton(onClick = vm::alternarPistas) { Text(if (ej.mostrarPistas) "Ocultar pistas" else "Ver pistas") }
                    if (ej.mostrarPistas) ej.problemasOcultos.forEach { Text("• $it", style = MaterialTheme.typography.bodySmall) }
                }
            }
        }

        Text("Datos del asiento", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Campo("N° asiento", f.numero, Modifier.weight(0.8f), numerico = true) { v -> vm.actualizarForm { it.copy(numero = v) } }
            Campo("Fecha dd/mm/aaaa", f.fecha, Modifier.weight(1.4f)) { v -> vm.actualizarForm { it.copy(fecha = v) } }
            Campo("Hora", f.hora, Modifier.weight(0.9f)) { v -> vm.actualizarForm { it.copy(hora = v) } }
        }

        Text("De (emisor)", style = MaterialTheme.typography.labelLarge)
        Selector(Emisores.todos, f.emisor, Emisores::etiqueta) { v -> vm.actualizarForm { it.copy(emisor = v) } }
        Campo("Nombre y cargo de quien firma", f.nombreEmisor) { v -> vm.actualizarForm { it.copy(nombreEmisor = v) } }
        Campo("A (destinatario)", f.destinatario) { v -> vm.actualizarForm { it.copy(destinatario = v) } }

        Text("Tipo de asiento", style = MaterialTheme.typography.labelLarge)
        Selector(TiposAsiento.todos, f.tipo, TiposAsiento::etiqueta) { v -> vm.actualizarForm { it.copy(tipo = v) } }
        Campo("Ítem / frente de obra (eje, nivel, progresiva)", f.itemReferencia) { v -> vm.actualizarForm { it.copy(itemReferencia = v) } }
        OutlinedTextField(
            value = f.instruccion,
            onValueChange = { v -> vm.actualizarForm { it.copy(instruccion = v) } },
            label = { Text("Instrucción / contenido (QUÉ, DÓNDE, CÓMO, PLAZO, VERIFICACIÓN)") },
            minLines = 4,
            modifier = Modifier.fillMaxWidth()
        )
        Campo("Plazo de cumplimiento (días)", f.plazoDias, numerico = true) { v -> vm.actualizarForm { it.copy(plazoDias = v) } }

        Casilla("Rúbrica (firma) del emisor", f.rubricaEmisor) { v -> vm.actualizarForm { it.copy(rubricaEmisor = v) } }
        Casilla("Firma de recepción del destinatario", f.firmaRecepcion) { v -> vm.actualizarForm { it.copy(firmaRecepcion = v) } }

        if (f.tipo == TiposAsiento.ORDEN_CAMBIO) SeccionOrdenCambio(f, vm)

        Button(onClick = vm::validarAsiento, modifier = Modifier.fillMaxWidth()) { Text("Validar contra la norma") }

        estado.mensaje?.let {
            val color = when {
                !estado.validado -> MaterialTheme.colorScheme.onSurface
                estado.criticas == 0 -> VerdeConforme
                else -> RojoAlerta
            }
            Text(it, color = color, fontWeight = FontWeight.SemiBold)
        }

        if (estado.validado) {
            Text(
                "Críticas: ${estado.criticas} · Advertencias: ${estado.advertencias}",
                style = MaterialTheme.typography.labelLarge
            )
            estado.hallazgos.forEach { TarjetaHallazgo(it) }
            Button(
                onClick = vm::registrarAsiento,
                enabled = estado.puedeRegistrar,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Registrar en el Libro de Órdenes") }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun SeccionOrdenCambio(f: AsientoForm, vm: EngineeringViewModel) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Control de la Orden de Cambio (Art. 89)", fontWeight = FontWeight.Bold)
            Selector(listOf(Modalidades.LICITACION, Modalidades.ANPE), f.modalidad, Modalidades::etiqueta) { v -> vm.actualizarForm { it.copy(modalidad = v) } }
            Campo("Monto del contrato principal (Bs)", f.montoContrato, numerico = true) { v -> vm.actualizarForm { it.copy(montoContrato = v) } }
            Campo("Monto de esta Orden de Cambio (Bs)", f.montoVariacion, numerico = true) { v -> vm.actualizarForm { it.copy(montoVariacion = v) } }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Campo("OC previas (Bs)", f.acumuladoOC, Modifier.weight(1f), numerico = true) { v -> vm.actualizarForm { it.copy(acumuladoOC = v) } }
                Campo("Contratos modif. (Bs)", f.acumuladoCM, Modifier.weight(1f), numerico = true) { v -> vm.actualizarForm { it.copy(acumuladoCM = v) } }
            }
            Casilla("Crea ítems nuevos", f.creaNuevosItems) { v -> vm.actualizarForm { it.copy(creaNuevosItems = v) } }
            Casilla("Incrementa precios unitarios", f.incrementaPreciosUnitarios) { v -> vm.actualizarForm { it.copy(incrementaPreciosUnitarios = v) } }
            Casilla("Cuenta con informe técnico", f.informeTecnico) { v -> vm.actualizarForm { it.copy(informeTecnico = v) } }
            Casilla("Cuenta con informe legal", f.informeLegal) { v -> vm.actualizarForm { it.copy(informeLegal = v) } }
            Casilla("Aprobada por la instancia de seguimiento (Fiscal)", f.aprobadaPorFiscal) { v -> vm.actualizarForm { it.copy(aprobadaPorFiscal = v) } }
        }
    }
}

@Composable
private fun TarjetaHallazgo(h: Hallazgo) {
    val (color, icono) = when (h.severidad) {
        Severidad.CRITICA -> RojoAlerta to Icons.Filled.Error
        Severidad.ADVERTENCIA -> AmbarAdvertencia to Icons.Filled.Warning
        Severidad.INFO -> MaterialTheme.colorScheme.primary to Icons.Filled.Info
    }
    Card(colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f))) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
            Icon(icono, contentDescription = h.severidad.name, tint = color)
            Column(Modifier.padding(start = 8.dp)) {
                Text("${etiquetaSeveridad(h.severidad)} · ${h.campo}", color = color, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                Text(h.mensaje, style = MaterialTheme.typography.bodyMedium)
                Text("Fundamento: ${h.fundamento}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
        }
    }
}

private fun etiquetaSeveridad(s: Severidad) = when (s) {
    Severidad.CRITICA -> "OMISIÓN CRÍTICA"
    Severidad.ADVERTENCIA -> "Advertencia"
    Severidad.INFO -> "Información"
}

@Composable
private fun MiLibro(asientos: List<AsientoEntity>, onVaciar: () -> Unit) {
    var confirmar by remember { mutableStateOf(false) }
    if (confirmar) {
        AlertDialog(
            onDismissRequest = { confirmar = false },
            title = { Text("¿Vaciar el libro?") },
            text = { Text("Se borrarán todos los asientos registrados.") },
            confirmButton = { TextButton(onClick = { confirmar = false; onVaciar() }) { Text("Vaciar") } },
            dismissButton = { TextButton(onClick = { confirmar = false }) { Text("Cancelar") } }
        )
    }
    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (asientos.isEmpty()) {
            item { Text("El libro está vacío. Valida y registra asientos desde el Simulador.") }
        }
        items(asientos, key = { it.id }) { a ->
            Card {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Asiento N° ${a.numero} · ${a.fecha} ${a.hora}", fontWeight = FontWeight.Bold)
                    Text("De: ${a.nombreEmisor} (${Emisores.etiqueta(a.emisor)})  →  A: ${a.destinatario}", style = MaterialTheme.typography.bodySmall)
                    Text("${TiposAsiento.etiqueta(a.tipoAsiento)} · ${a.itemReferencia}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                    HorizontalDivider()
                    Text(a.instruccion)
                    a.plazoDias?.let { Text("Plazo: $it días", fontWeight = FontWeight.SemiBold) }
                    Text(
                        "Firma emisor ✓   Recepción ${if (a.firmaRecepcion) "✓" else "pendiente"}" +
                            if (a.advertencias > 0) "   · ${a.advertencias} advertencias" else "",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
        if (asientos.isNotEmpty()) {
            item { OutlinedButton(onClick = { confirmar = true }) { Text("Vaciar libro") } }
        }
    }
}

// ---------------------------------------------------------------------------
// Controles reutilizables del formulario
// ---------------------------------------------------------------------------

@Composable
internal fun Campo(
    etiqueta: String,
    valor: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    numerico: Boolean = false,
    onCambio: (String) -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta, maxLines = 1) },
        singleLine = true,
        keyboardOptions = if (numerico) KeyboardOptions(keyboardType = KeyboardType.Decimal) else KeyboardOptions.Default,
        modifier = modifier
    )
}

@Composable
private fun Casilla(texto: String, marcado: Boolean, onCambio: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = marcado, onCheckedChange = onCambio)
        Text(texto)
    }
}

@Composable
private fun Selector(opciones: List<String>, seleccion: String, etiqueta: (String) -> String, onSeleccion: (String) -> Unit) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(opciones) { op ->
            FilterChip(selected = op == seleccion, onClick = { onSeleccion(op) }, label = { Text(etiqueta(op)) })
        }
    }
}
