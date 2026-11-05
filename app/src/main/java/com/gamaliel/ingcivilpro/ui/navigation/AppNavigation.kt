package com.gamaliel.ingcivilpro.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gamaliel.ingcivilpro.ui.screens.LibroOrdenesScreen
import com.gamaliel.ingcivilpro.ui.screens.NormativaScreen
import com.gamaliel.ingcivilpro.ui.screens.VolumenesObraScreen
import com.gamaliel.ingcivilpro.ui.viewmodel.EngineeringViewModel

enum class Destino(val ruta: String, val etiqueta: String, val icono: ImageVector) {
    NORMATIVA("normativa", "Normativa", Icons.AutoMirrored.Filled.MenuBook),
    LIBRO("libro_ordenes", "Libro de Órdenes", Icons.Filled.EditNote),
    VOLUMENES("volumenes", "Cómputos", Icons.Filled.Calculate)
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: EngineeringViewModel = viewModel()
    val backStack by navController.currentBackStackEntryAsState()
    val rutaActual = backStack?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                Destino.entries.forEach { destino ->
                    NavigationBarItem(
                        selected = rutaActual == destino.ruta,
                        onClick = {
                            navController.navigate(destino.ruta) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(destino.icono, contentDescription = destino.etiqueta) },
                        label = { Text(destino.etiqueta) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Destino.NORMATIVA.ruta,
            modifier = Modifier.padding(padding)
        ) {
            composable(Destino.NORMATIVA.ruta) { NormativaScreen(viewModel) }
            composable(Destino.LIBRO.ruta) { LibroOrdenesScreen(viewModel) }
            composable(Destino.VOLUMENES.ruta) { VolumenesObraScreen(viewModel) }
        }
    }
}
