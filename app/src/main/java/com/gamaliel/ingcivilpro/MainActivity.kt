package com.gamaliel.ingcivilpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.gamaliel.ingcivilpro.ui.navigation.AppNavigation
import com.gamaliel.ingcivilpro.ui.theme.IngenieriaCivilProTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IngenieriaCivilProTheme {
                AppNavigation()
            }
        }
    }
}
