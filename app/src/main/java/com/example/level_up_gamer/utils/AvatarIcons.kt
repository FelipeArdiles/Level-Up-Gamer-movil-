package com.example.level_up_gamer.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.SentimentVerySatisfied
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Utilidad para manejar los iconos de avatar disponibles.
 * Usamos iconos de Material Icons como identificadores.
 */
object AvatarIcons {
    // Lista de iconos de avatar disponibles
    val availableAvatars = listOf(
        AvatarOption(0, "Por defecto", Icons.Default.Person),
        AvatarOption(1, "Usuario", Icons.Default.AccountCircle),
        AvatarOption(2, "Cara", Icons.Default.Face),
        AvatarOption(3, "Persona", Icons.Default.PersonOutline),
        AvatarOption(4, "Sonriente", Icons.Default.SentimentSatisfied),
        AvatarOption(5, "Muy feliz", Icons.Default.SentimentVerySatisfied),
        AvatarOption(6, "Estrella", Icons.Default.Star)
    )
    
    /**
     * Obtiene el icono por defecto (índice 0)
     */
    fun getDefaultIcon(): ImageVector = Icons.Default.Person
    
    /**
     * Obtiene un icono por su ID
     */
    fun getIconById(id: Int): ImageVector {
        return availableAvatars.firstOrNull { it.id == id }?.icon ?: getDefaultIcon()
    }
    
    /**
     * Obtiene la opción de avatar por su ID
     */
    fun getAvatarOptionById(id: Int): AvatarOption {
        return availableAvatars.firstOrNull { it.id == id } ?: availableAvatars[0]
    }
}

data class AvatarOption(
    val id: Int,
    val name: String,
    val icon: ImageVector
)

