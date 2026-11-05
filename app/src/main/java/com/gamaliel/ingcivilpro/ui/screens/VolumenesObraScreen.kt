package com.gamaliel.ingcivilpro.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gamaliel.ingcivilpro.data.local.FormulaComputoEntity
import com.gamaliel.ingcivilpro.data.local.Modulos
import com.gamaliel.ingcivilpro.domain.ResultadoActividad
import com.gamaliel.ingcivilpro.ui.theme.RojoAlerta
import com.gamaliel.ingcivilpro.ui.theme.VerdeConforme
import com.gamaliel.ingcivilpro.ui.viewmodel.ComputosUiState
import com.gamaliel.ingcivilpro.ui.viewmodel.CpmUiState
import com.gamaliel.ingcivilpro.ui.viewmodel.EngineeringViewModel
import java.text.NumberFormat
import java.util.Locale

private val formatoBs: NumberFormat = NumberFormat.getNumberInstance(Locale("es", "BO")).apply {
    minimumFractionDigits = 2
    maximumFractionDigits = 2
}

private fun numero(n: Double, decimales: Int = 4): String =
    NumberFormat.getNumberInstance(Locale("es", "BO")).apply { maximumFractionDigits = decimales }.format(n)

@Composable
fun VolumenesObraScreen(viewModel: EngineeringViewModel) {
    val estado by viewModel.computos.collectAsStateWithLifecycle()
    val lecciones by viewModel.lecciones.collectAsStateWithLifecycle()
    var pestana by rememberSaveable { mutableIntStateOf(1) }
    val titulos = listOf("Aprender", "Fórmulas", "Cronograma")

    Column(Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = pestana) {
            titulos.forEachIndexed { i, t -> Tab(selected = pestana == i, onClick = { pestana = i }, text = { Text(t) }) }
        }
        when (pestana) {
            0 -> ListaLecciones(
                lecciones.filter { it.modulo == Modulos.COMPUTOS },
                "Método de cubicación, ejercicios resueltos de exámenes, rendimientos, precio unitario y ruta crítica."
            )
            1 -> if (estado.formulaSeleccionada == null) ListaFormulas(estado, viewModel) else DetalleFormula(estado, viewModel)
            2 -> Cronograma(estado.cpm, viewModel)
        }
    }
}

// ---------------------------------------------------------------------------
// FÓRMULAS
// ---------------------------------------------------------------------------

@Composable
private fun ListaFormulas(estado: ComputosUiState, vm: EngineeringViewModel) {
    val visibles = estado.formulas.filter { estado.categoriaSeleccionada == null || it.categoria == estado.categoriaSeleccionada }
    Column(Modifier.fillMaxSize()) {
        Spacer(Modifier.height(8.dp))
        FiltroChips(estado.categorias, estado.categoriaSeleccionada, vm::seleccionarCategoria, etiquetaTodos = "Todas")
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                OutlinedButton(onClick = vm::nuevaFormula) {
                    Icon(Icons.Filled.Add, null); Text("Crear fórmula propia")
                }
            }
            items(visibles, key = { it.id }) { f -> TarjetaFormula(f) { vm.seleccionarFormula(f) } }
        }
    }
}

@Composable
private fun TarjetaFormula(f: FormulaComputoEntity, onClick: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(Modifier.padding(12.dp)) {
            Text(f.nombre, fontWeight = FontWeight.SemiBold)
            Text("${f.categoria} · resultado en ${f.unidad}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            Text(f.expresion, fontFamily = FontFamily.Monospace, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun DetalleFormula(estado: ComputosUiState, vm: EngineeringViewModel) {
    val f = estado.formulaSeleccionada ?: return
    var nombre by remember(f.id, f.nombre) { mutableStateOf(f.nombre) }
    var unidad by remember(f.id, f.unidad) { mutableStateOf(f.unidad) }

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = vm::cerrarFormula) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") }
            Text(f.nombre, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
        }
        Text(f.descripcion, style = MaterialTheme.typography.bodySmall)

        OutlinedTextField(
            value = estado.expresion,
            onValueChange = vm::editarExpresion,
            label = { Text("Fórmula paramétrica (editable)") },
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontFamily = FontFamily.Monospace),
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            "Operadores: + − * / ^ ( ). Funciones: sqrt, abs, min, max, ceil, floor, round, sin/cos/tan (rad), sind/cosd/tand (grados). Constante: pi.",
            style = MaterialTheme.typography.labelSmall
        )

        Text("Parámetros", style = MaterialTheme.typography.titleSmall)
        estado.variablesActuales.forEach { v ->
            val i = f.variables.indexOf(v)
            val descripcion = if (i >= 0) f.descripcionVariables.getOrElse(i) { "" } else ""
            OutlinedTextField(
                value = estado.valores[v] ?: "",
                onValueChange = { vm.editarValor(v, it) },
                label = { Text(if (descripcion.isBlank()) v else "$v – $descripcion") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (estado.error == null) MaterialTheme.colorScheme.primaryContainer else RojoAlerta.copy(alpha = 0.1f)
            )
        ) {
            Column(Modifier.fillMaxWidth().padding(16.dp)) {
                if (estado.error != null) {
                    Text("Error", color = RojoAlerta, fontWeight = FontWeight.Bold)
                    Text(estado.error)
                } else {
                    Text("Resultado", style = MaterialTheme.typography.labelLarge)
                    Text(
                        "${estado.resultado?.let { numero(it) } ?: "—"} ${unidad}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        HorizontalDivider()
        Text("Guardar como fórmula propia", style = MaterialTheme.typography.titleSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Campo("Nombre", nombre, Modifier.weight(2f)) { nombre = it }
            Campo("Unidad", unidad, Modifier.weight(1f)) { unidad = it }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { vm.guardarFormula(nombre, unidad) }) { Text("Guardar") }
            if (f.esPersonalizada && f.id != 0L) {
                OutlinedButton(onClick = vm::borrarFormula) {
                    Icon(Icons.Filled.Delete, null); Text("Borrar")
                }
            }
        }
        estado.mensaje?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
        Spacer(Modifier.height(24.dp))
    }
}

// ---------------------------------------------------------------------------
// CRONOGRAMA / RUTA CRÍTICA
// ---------------------------------------------------------------------------

@Composable
private fun Cronograma(cpm: CpmUiState, vm: EngineeringViewModel) {
    var dialogoNueva by remember { mutableStateOf(false) }
    if (dialogoNueva) DialogoNuevaActividad(onCerrar = { dialogoNueva = false }, onAgregar = vm::agregarActividad)

    val r = cpm.resultado
    val plazoRef = cpm.plazoReferencial.toIntOrNull()
    val precioRef = cpm.precioReferencial.replace(',', '.').toDoubleOrNull()

    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                "Pregunta de examen precargada: ordena las actividades (predecesoras), ajusta cuadrillas y verifica plazo y monto contra los referenciales.",
                style = MaterialTheme.typography.bodySmall
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Campo("Plazo ref. (días)", cpm.plazoReferencial, Modifier.weight(1f), numerico = true) { vm.cambiarReferenciales(it, cpm.precioReferencial) }
                Campo("Precio ref. (Bs)", cpm.precioReferencial, Modifier.weight(1.4f), numerico = true) { vm.cambiarReferenciales(cpm.plazoReferencial, it) }
            }
        }
        item {
            if (cpm.error != null) {
                Text(cpm.error, color = RojoAlerta, fontWeight = FontWeight.Bold)
            } else if (r != null) {
                val plazoOk = plazoRef == null || r.duracionTotal <= plazoRef
                val montoOk = precioRef == null || r.montoTotal <= precioRef
                Card {
                    Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            "Duración total: ${r.duracionTotal} días" + (plazoRef?.let { " (referencial $it)" } ?: ""),
                            color = if (plazoOk) VerdeConforme else RojoAlerta, fontWeight = FontWeight.Bold
                        )
                        if (!plazoOk) Text("Excede el plazo: aumenta cuadrillas en actividades CRÍTICAS (holgura 0).", style = MaterialTheme.typography.bodySmall)
                        Text(
                            "Monto de la propuesta: Bs ${formatoBs.format(r.montoTotal)}" + (precioRef?.let { " (referencial ${formatoBs.format(it)})" } ?: ""),
                            color = if (montoOk) VerdeConforme else RojoAlerta, fontWeight = FontWeight.Bold
                        )
                        if (!montoOk) Text("La propuesta supera el precio referencial: sería descalificada (Art. 27 I b – desierta si todas lo superan). Revisa precios unitarios.", style = MaterialTheme.typography.bodySmall)
                        Text("Ruta crítica: ${r.rutaCritica.joinToString(" → ")}", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { dialogoNueva = true }) { Icon(Icons.Filled.Add, null); Text("Actividad") }
                OutlinedButton(onClick = vm::restaurarCronogramaExamen) { Icon(Icons.Filled.Restore, null); Text("Datos del examen") }
            }
        }
        val resultados = r?.actividades ?: emptyList()
        if (resultados.isNotEmpty()) {
            items(resultados, key = { it.actividad.id }) { ra -> FilaActividad(ra, vm) }
        } else {
            items(cpm.actividades, key = { it.id }) { a ->
                Text("${a.id}. ${a.nombre} (pred: ${a.predecesoras.joinToString()})")
            }
        }
    }
}

@Composable
private fun FilaActividad(ra: ResultadoActividad, vm: EngineeringViewModel) {
    val a = ra.actividad
    var dias by remember(a.id) { mutableStateOf(EngineeringViewModel.formatoNumero(a.diasBase)) }
    var preds by remember(a.id) { mutableStateOf(a.predecesoras.joinToString(",")) }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (ra.esCritica) RojoAlerta.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("${a.id}. ${a.nombre}", Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                IconButton(onClick = { vm.eliminarActividad(a.id) }) { Icon(Icons.Filled.Delete, "Eliminar") }
            }
            Text(
                "${numero(a.volumen, 2)} ${a.unidad} × ${formatoBs.format(a.precioUnitario)} = Bs ${formatoBs.format(a.parcial)}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "Duración ${a.duracion} d · IT ${ra.inicioTemprano} · FT ${ra.finTemprano} · ITd ${ra.inicioTardio} · FTd ${ra.finTardio} · Holgura ${ra.holgura}" +
                    if (ra.esCritica) " · CRÍTICA" else "",
                style = MaterialTheme.typography.labelMedium,
                color = if (ra.esCritica) RojoAlerta else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                OutlinedTextField(
                    value = dias,
                    onValueChange = { dias = it; vm.cambiarDiasBase(a.id, it) },
                    label = { Text("Días base") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = preds,
                    onValueChange = { preds = it; vm.cambiarPredecesoras(a.id, it) },
                    label = { Text("Predec.") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Cuadrillas:", style = MaterialTheme.typography.labelLarge)
                IconButton(onClick = { vm.cambiarCuadrillas(a.id, -1) }) { Icon(Icons.Filled.Remove, "Menos") }
                Text("${a.cuadrillas}", fontWeight = FontWeight.Bold)
                IconButton(onClick = { vm.cambiarCuadrillas(a.id, 1) }) { Icon(Icons.Filled.Add, "Más") }
                Spacer(Modifier.width(8.dp))
                Text("→ ${a.duracion} días", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun DialogoNuevaActividad(
    onCerrar: () -> Unit,
    onAgregar: (String, String, String, String, String, String) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var unidad by remember { mutableStateOf("M3") }
    var volumen by remember { mutableStateOf("") }
    var unitario by remember { mutableStateOf("") }
    var dias by remember { mutableStateOf("1") }
    var preds by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text("Nueva actividad") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Campo("Nombre", nombre) { nombre = it }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Campo("Unidad", unidad, Modifier.weight(1f)) { unidad = it }
                    Campo("Volumen", volumen, Modifier.weight(1f), numerico = true) { volumen = it }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Campo("P. unitario", unitario, Modifier.weight(1f), numerico = true) { unitario = it }
                    Campo("Días", dias, Modifier.weight(1f), numerico = true) { dias = it }
                }
                Campo("Predecesoras (ej. 2,3)", preds) { preds = it }
            }
        },
        confirmButton = {
            TextButton(onClick = { onAgregar(nombre, unidad, volumen, unitario, dias, preds); onCerrar() }) { Text("Agregar") }
        },
        dismissButton = { TextButton(onClick = onCerrar) { Text("Cancelar") } }
    )
}
