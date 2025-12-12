package com.example.level_up_gamer.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.level_up_gamer.ui.theme.PrimaryGreen
import com.example.level_up_gamer.ui.theme.SurfaceDarkElevated

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector? = null
)

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    selectedRoute: String?,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        // Fondo con efecto frosted glass mejorado con gradiente neón
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            SurfaceDarkElevated.copy(alpha = 0.95f),
                            SurfaceDarkElevated.copy(alpha = 0.98f)
                        )
                    )
                )
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(30.dp),
                    spotColor = PrimaryGreen.copy(alpha = 0.4f)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    BottomNavItem(
                        item = item,
                        isSelected = selectedRoute == item.route,
                        onClick = { onItemClick(item.route) }
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // Animaciones
    val scale by animateFloatAsState(
        targetValue = when {
            isPressed -> 0.85f
            isSelected -> 1.1f
            else -> 1f
        },
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness = 400f
        ),
        label = "nav_item_scale"
    )
    
    val iconSize by animateFloatAsState(
        targetValue = if (isSelected) 28f else 24f,
        animationSpec = spring(
            dampingRatio = 0.7f,
            stiffness = 500f
        ),
        label = "icon_size"
    )
    
    val backgroundColor by animateFloatAsState(
        targetValue = if (isSelected) 0.6f else 0f,
        animationSpec = tween(300),
        label = "bg_alpha"
    )

    Box(
        modifier = Modifier
            .width(70.dp)
            .height(50.dp)
            .scale(scale)
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = if (isSelected) {
                    Brush.linearGradient(
                        colors = listOf(
                            PrimaryGreen.copy(alpha = backgroundColor),
                            PrimaryGreen.copy(alpha = backgroundColor * 0.8f)
                        )
                    )
                } else {
                    Brush.linearGradient(
                        colors = listOf(Color.Transparent, Color.Transparent)
                    )
                }
            )
            .shadow(
                elevation = if (isSelected) 8.dp else 0.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = PrimaryGreen.copy(alpha = if (isSelected) 0.5f else 0f)
            )
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = interactionSource
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Icon(
                imageVector = if (isSelected && item.selectedIcon != null) {
                    item.selectedIcon
                } else {
                    item.icon
                },
                contentDescription = item.label,
                modifier = Modifier.size(iconSize.dp),
                tint = if (isSelected) {
                    PrimaryGreen
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                }
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = item.label,
                fontSize = if (isSelected) 12.sp else 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) {
                    PrimaryGreen
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                }
            )
        }
    }
}

/**
 * Obtiene los items de navegación para la barra inferior
 */
fun getBottomNavItems(): List<BottomNavItem> {
    return listOf(
        BottomNavItem(
            route = "product_menu_screen",
            label = "Inicio",
            icon = Icons.Default.Home
        ),
        BottomNavItem(
            route = "game_suggestions_screen",
            label = "Juegos",
            icon = Icons.Default.Public
        ),
        BottomNavItem(
            route = "stores_map_screen",
            label = "Mapa",
            icon = Icons.Default.LocationOn
        ),
        BottomNavItem(
            route = "cart_screen",
            label = "Carrito",
            icon = Icons.Default.ShoppingCart
        ),
        BottomNavItem(
            route = "user_profile_screen",
            label = "Perfil",
            icon = Icons.Default.Person
        )
    )
}

