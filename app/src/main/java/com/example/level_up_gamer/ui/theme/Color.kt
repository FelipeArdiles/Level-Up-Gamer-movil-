// ui/theme/Color.kt

package com.example.level_up_gamer.ui.theme

import androidx.compose.ui.graphics.Color

// Tonos base para el esquema de colores (Material 3)
// Colores principales de la marca (Verde Neón/Vibrante mejorado)
val PrimaryGreen = Color(0xFF00FF88) // Verde neón más vibrante
val PrimaryGreenBright = Color(0xFF00FFAA) // Verde más brillante para efectos
val PrimaryGreenDark = Color(0xFF00CC66) // Verde oscuro para sombras
val OnPrimaryDark = Color(0xFF000000) // Negro para texto sobre verde brillante

// Colores del fondo (Negro y variantes mejorados)
val BackgroundDark = Color(0xFF050505) // Negro más profundo
val BackgroundDarkVariant = Color(0xFF0A0A0A) // Variante del fondo
val SurfaceDark = Color(0xFF1A1A1A)     // Superficie más clara (para Cards)
val SurfaceDarkElevated = Color(0xFF252525) // Superficie elevada
val OnSurfaceLight = Color(0xFFFFFFFF)  // Texto blanco sobre Surface/Background
val OnBackgroundLight = Color(0xFFFFFFFF) // Texto blanco sobre Background

// Colores de realce (secundarios mejorados)
val SecondaryDark = Color(0xFFB0FFC9) // Verde claro para realces menores
val SecondaryBright = Color(0xFF88FFAA) // Verde secundario brillante
val OnSecondaryDark = Color(0xFF000000)

// Colores de acento (nuevos)
val AccentCyan = Color(0xFF00FFFF) // Cian neón para acentos
val AccentPurple = Color(0xFFAA00FF) // Púrpura neón
val AccentBlue = Color(0xFF0088FF) // Azul neón

// Color de error (para mensajes de login fallido)
val ErrorRed = Color(0xFFFF4040)
val ErrorRedBright = Color(0xFFFF6666) // Rojo más brillante
val OnError = Color(0xFFFFFFFF) // Texto blanco sobre error

// Colores de éxito
val SuccessGreen = Color(0xFF00FF88)
val SuccessGreenBright = Color(0xFF44FFAA)