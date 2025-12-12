package com.example.level_up_gamer.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

// Animación de pulso para elementos interactivos
@Composable
fun rememberPulseAnimation(): Float {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val animatedValue = infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    return animatedValue.value
}

// Animación de brillo/glow
@Composable
fun rememberGlowAnimation(): Float {
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val animatedValue = infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )
    return animatedValue.value
}

// Modifier para animación de entrada con escala
fun Modifier.animateEnterScale(
    enabled: Boolean = true,
    initialScale: Float = 0.8f
): Modifier = composed {
    val scale = remember { Animatable(if (enabled) initialScale else 1f) }
    
    LaunchedEffect(enabled) {
        if (enabled) {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = 0.6f,
                    stiffness = 300f
                )
            )
        }
    }
    
    this.scale(scale.value)
}

// Modifier para animación de entrada con fade
fun Modifier.animateEnterFade(
    enabled: Boolean = true,
    initialAlpha: Float = 0f
): Modifier = composed {
    val alpha = remember { Animatable(if (enabled) initialAlpha else 1f) }
    
    LaunchedEffect(enabled) {
        if (enabled) {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(400)
            )
        }
    }
    
    this.alpha(alpha.value)
}

// Modifier para efecto de hover/press con elevación
fun Modifier.animateElevation(
    isPressed: Boolean = false,
    defaultElevation: Float = 4f,
    pressedElevation: Float = 8f
): Modifier = composed {
    val elevation = remember { Animatable(defaultElevation) }
    
    LaunchedEffect(isPressed) {
        elevation.animateTo(
            targetValue = if (isPressed) pressedElevation else defaultElevation,
            animationSpec = spring(
                dampingRatio = 0.7f,
                stiffness = 500f
            )
        )
    }
    
    this.shadow(elevation.value.dp, shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
}

// Modifier para efecto de brillo neón
fun Modifier.neonGlow(
    enabled: Boolean = true,
    color: androidx.compose.ui.graphics.Color = com.example.level_up_gamer.ui.theme.PrimaryGreen
): Modifier = composed {
    val glowAlpha = rememberGlowAnimation()
    
    if (enabled) {
        this.graphicsLayer {
            shadowElevation = (glowAlpha * 20).dp.toPx()
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
        }
    } else {
        this
    }
}

