package com.example.level_up_gamer.ui.theme

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.level_up_gamer.ui.theme.PrimaryGreen

@Composable
fun rememberNeonBackgroundBrush(): Brush = remember {
    Brush.linearGradient(
        colors = listOf(
            Color(0xFF03080C),
            Color(0xFF05171A),
            Color(0xFF04110C),
            Color(0xFF03080C)
        ),
        start = Offset.Zero,
        end = Offset(0f, 1400f)
    )
}

@Composable
fun rememberAnimatedNeonBackgroundBrush(): Brush {
    val infiniteTransition = rememberInfiniteTransition(label = "background_animation")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "background_offset"
    )
    
    return remember(offset) {
        Brush.linearGradient(
            colors = listOf(
                Color(0xFF03080C),
                Color(0xFF05171A).copy(alpha = 0.8f + offset * 0.2f),
                Color(0xFF04110C),
                Color(0xFF03080C)
            ),
            start = Offset(0f, 0f),
            end = Offset(0f, 1400f + offset * 200f)
        )
    }
}

@Composable
fun rememberGradientBrush(
    colors: List<Color>,
    start: Offset = Offset.Zero,
    end: Offset = Offset(0f, 1000f)
): Brush = remember {
    Brush.linearGradient(
        colors = colors,
        start = start,
        end = end
    )
}

@Composable
fun rememberNeonGlowBrush(): Brush {
    val infiniteTransition = rememberInfiniteTransition(label = "glow_animation")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )
    
    return remember(alpha) {
        Brush.radialGradient(
            colors = listOf(
                PrimaryGreen.copy(alpha = alpha),
                PrimaryGreen.copy(alpha = alpha * 0.5f),
                Color.Transparent
            ),
            center = Offset(500f, 500f),
            radius = 800f
        )
    }
}

