// ui/theme/Theme.kt

package com.example.level_up_gamer.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// 1. Esquema de Colores Oscuro (Nuestro Tema Gamer Principal)
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryGreen,          // Verde principal para botones, AppBar iconos
    onPrimary = OnPrimaryDark,       // Texto sobre PrimaryGreen
    secondary = SecondaryDark,       // Verde claro para realces
    background = BackgroundDark,     // Negro profundo
    surface = SurfaceDark,           // Gris oscuro para Cards, TextFields
    onSurface = OnSurfaceLight,      // Texto sobre Surface/Background
    error = ErrorRed                 // Rojo para errores de login
)

// 2. Esquema de Colores Claro (Por si acaso, aunque no lo usaremos)
private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = Color.White,
    secondary = SecondaryDark,
    background = Color.White,
    surface = Color.White,
    onSurface = Color.Black,
    error = ErrorRed
)

@Composable
fun Theme(
    // Forzamos el tema oscuro para garantizar la estética gamer
    darkTheme: Boolean = true, // isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Usamos DarkColorScheme ya que queremos el look oscuro/verde
    val colorScheme = DarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Colorea la barra de estado superior para que sea consistente con el fondo
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    // Aplica el tema a toda la aplicación
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Se refiere al archivo Type.kt que ya existe
        content = content
    )
}