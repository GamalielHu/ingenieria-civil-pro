package com.gamaliel.ingcivilpro.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gamaliel.ingcivilpro.data.local.LeccionEntity

/** Lista de lecciones expandibles (pestaña "Aprender" de cada módulo). */
@Composable
fun ListaLecciones(lecciones: List<LeccionEntity>, introduccion: String) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(introduccion, style = MaterialTheme.typography.bodyMedium)
        }
        items(lecciones, key = { it.id }) { leccion -> TarjetaLeccion(leccion) }
    }
}

@Composable
private fun TarjetaLeccion(leccion: LeccionEntity) {
    var abierta by rememberSaveable(leccion.id) { mutableStateOf(false) }
    Card(modifier = Modifier.fillMaxWidth().clickable { abierta = !abierta }) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    leccion.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Icon(if (abierta) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore, contentDescription = null)
            }
            Text(leccion.fundamento, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            if (abierta) {
                Spacer(Modifier.padding(4.dp))
                Text(leccion.contenido, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

/** Fila horizontal de chips de filtro con opción "Todos". */
@Composable
fun FiltroChips(opciones: List<String>, seleccion: String?, onSeleccion: (String?) -> Unit, etiquetaTodos: String = "Todos") {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(selected = seleccion == null, onClick = { onSeleccion(null) }, label = { Text(etiquetaTodos) })
        }
        items(opciones) { op ->
            FilterChip(selected = seleccion == op, onClick = { onSeleccion(op) }, label = { Text(op) })
        }
    }
}

@Composable
fun EspacioH(ancho: Int = 8) = Spacer(Modifier.width(ancho.dp))
