package com.example.level_up_gamer.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun rememberNeonBackgroundBrush(): Brush = remember {
    Brush.linearGradient(
        colors = listOf(
            Color(0xFF03080C),
            Color(0xFF05171A),
            Color(0xFF04110C)
        ),
        start = Offset.Zero,
        end = Offset(0f, 1400f)
    )
}

