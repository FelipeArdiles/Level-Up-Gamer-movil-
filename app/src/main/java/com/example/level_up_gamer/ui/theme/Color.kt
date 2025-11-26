// ui/theme/Color.kt

package com.example.level_up_gamer.ui.theme

import androidx.compose.ui.graphics.Color

// Tonos base para el esquema de colores (Material 3)
// Colores principales de la marca (Verde Neón/Vibrante)
val PrimaryGreen = Color(0xFF00FF40) // Verde brillante para botones, iconos
val OnPrimaryDark = Color(0xFF000000) // Negro para texto sobre verde brillante

// Colores del fondo (Negro y variantes)
val BackgroundDark = Color(0xFF0C0C0C) // Negro profundo
val SurfaceDark = Color(0xFF1F1F1F)     // Superficie un poco más clara (para Cards)
val OnSurfaceLight = Color(0xFFFFFFFF)  // Texto blanco sobre Surface/Background
val OnBackgroundLight = Color(0xFFFFFFFF) // Texto blanco sobre Background

// Colores de realce (secundarios)
val SecondaryDark = Color(0xFFB0FFC9) // Verde claro para realces menores
val OnSecondaryDark = Color(0xFF000000)

// Color de error (para mensajes de login fallido)
val ErrorRed = Color(0xFFFF4040)
val OnError = Color(0xFFFFFFFF) // Texto blanco sobre error