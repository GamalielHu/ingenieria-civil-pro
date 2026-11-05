package com.gamaliel.ingcivilpro.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val AzulObra = Color(0xFF0D3B66)
val AmarilloCasco = Color(0xFFF4D35E)
val NaranjaSeguridad = Color(0xFFEE964B)
val RojoAlerta = Color(0xFFC62828)
val VerdeConforme = Color(0xFF2E7D32)
val AmbarAdvertencia = Color(0xFFB26A00)

private val Claro = lightColorScheme(
    primary = AzulObra,
    onPrimary = Color.White,
    secondary = NaranjaSeguridad,
    tertiary = AmarilloCasco,
    error = RojoAlerta
)

private val Oscuro = darkColorScheme(
    primary = Color(0xFF8FB8E8),
    onPrimary = Color(0xFF00203F),
    secondary = NaranjaSeguridad,
    tertiary = AmarilloCasco,
    error = Color(0xFFEF9A9A)
)

@Composable
fun IngenieriaCivilProTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (isSystemInDarkTheme()) Oscuro else Claro, content = content)
}
