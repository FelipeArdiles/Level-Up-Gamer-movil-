package com.example.level_up_gamer.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.level_up_gamer.R
import com.example.level_up_gamer.data.CartItemWithProduct
import com.example.level_up_gamer.ui.components.BottomNavigationBar
import com.example.level_up_gamer.ui.components.getBottomNavItems
import com.example.level_up_gamer.ui.navigation.Screen
import com.example.level_up_gamer.ui.theme.rememberNeonBackgroundBrush
import com.example.level_up_gamer.viewmodel.ProductViewModel
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    productViewModel: ProductViewModel
) {
    val cartItems by productViewModel.cartItems.collectAsState()
    val uiState by productViewModel.uiState.collectAsState()

    // Estado local para controlar la visibilidad del mensaje de compra exitosa
    var showSuccessMessage by remember { mutableStateOf(false) }

    // Manejar mensajes de éxito: mostrar y ocultar automáticamente
    LaunchedEffect(uiState.successMessage) {
        val message = uiState.successMessage
        if (message != null) {
            if (message.contains("Compra realizada exitosamente", ignoreCase = true)) {
                // Mostrar el mensaje
                showSuccessMessage = true
                // Esperar 3 segundos antes de ocultar y limpiar el mensaje
                kotlinx.coroutines.delay(3000)
                showSuccessMessage = false
                productViewModel.clearMessages()
            } else {
                // Limpiar inmediatamente si no es mensaje de compra exitosa
                kotlinx.coroutines.delay(100)
                productViewModel.clearMessages()
            }
        } else {
            // Si el mensaje se limpia externamente, ocultar también
            showSuccessMessage = false
        }
    }

    // Limpiar mensajes de error después de 3 segundos
    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            kotlinx.coroutines.delay(3000)
            productViewModel.clearMessages()
        }
    }
    

    val backgroundBrush = rememberNeonBackgroundBrush()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        // Overlay de mensaje de éxito llamativo - Solo para compra exitosa
        uiState.successMessage?.let { success ->
            // Solo mostrar el overlay si el mensaje es de compra exitosa y showSuccessMessage es true
            if (success.contains("Compra realizada exitosamente", ignoreCase = true) && showSuccessMessage) {
                AnimatedVisibility(
                    visible = showSuccessMessage,
                    enter = fadeIn(animationSpec = tween(300)) + scaleIn(
                        initialScale = 0.5f,
                        animationSpec = spring(
                            dampingRatio = 0.6f,
                            stiffness = 300f
                        )
                    ),
                    exit = fadeOut(animationSpec = tween(300)) + scaleOut(
                        targetScale = 0.8f,
                        animationSpec = tween(200)
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.7f)),
                        contentAlignment = Alignment.Center
                    ) {
                        val neonGreen = MaterialTheme.colorScheme.primary
                        val neonGreenBright = Color(0xFF00FF88)
                        
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(0.75f)
                                .shadow(
                                    elevation = 20.dp,
                                    shape = RoundedCornerShape(20.dp),
                                    spotColor = neonGreen
                                ),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF0A1A0F)
                            ),
                            border = BorderStroke(
                                width = 2.5.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        neonGreen,
                                        neonGreenBright,
                                        neonGreen
                                    )
                                )
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color(0xFF0A1A0F),
                                                Color(0xFF051A0F),
                                                Color(0xFF0A1A0F)
                                            )
                                        )
                                    )
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                // Icono de éxito
                                Text(
                                    text = "✓",
                                    style = MaterialTheme.typography.displayMedium,
                                    color = neonGreen,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                                
                                Text(
                                    text = "¡COMPRA EXITOSA!",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = neonGreen,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                                
                                Text(
                                    text = success,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(top = 6.dp)
                                )
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                // Efecto de brillo neón
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(2.dp)
                                        .background(
                                            brush = Brush.horizontalGradient(
                                                colors = listOf(
                                                    Color.Transparent,
                                                    neonGreen.copy(alpha = 0.8f),
                                                    neonGreenBright,
                                                    neonGreen.copy(alpha = 0.8f),
                                                    Color.Transparent
                                                )
                                            )
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
        
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Level Up Gamer",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            bottomBar = {
                Column {
                    // Barra de total y botón de compra (si hay items)
                    if (cartItems.isNotEmpty()) {
                        // El precio ya está en CLP, no necesita conversión
                        val clpTotal = cartItems.sumOf { it.product.price * it.cartItem.quantity }
                        val clpFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply {
                            currency = Currency.getInstance("CLP")
                            maximumFractionDigits = 0
                        }

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                                ),
                            tonalElevation = 8.dp,
                            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        "Total:",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        clpFormat.format(clpTotal),
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = { productViewModel.checkout() },
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = !uiState.isLoading
                                ) {
                                    if (uiState.isLoading) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(24.dp),
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    } else {
                                        Text("Realizar Compra")
                                    }
                                }
                            }
                        }
                    }
                    // Barra de navegación inferior
                    BottomNavigationBar(
                        items = getBottomNavItems(),
                        selectedRoute = navController.currentDestination?.route,
                        onItemClick = { route ->
                            navController.navigate(route) {
                                launchSingleTop = true
                                if (navController.currentDestination?.route == route) {
                                    return@navigate
                                }
                                popUpTo(Screen.ProductMenu.route) {
                                    saveState = true
                                }
                                restoreState = true
                            }
                        }
                    )
                }
            }
        ) { paddingValues ->
            LazyColumn(
                contentPadding = paddingValues,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
            ) {
                if (cartItems.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "El carrito está vacío",
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                } else {
                    uiState.errorMessage?.let { error ->
                        item {
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically()
                            ) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                                ) {
                                    Text(
                                        text = error,
                                        modifier = Modifier.padding(16.dp),
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            }
                        }
                    }
                    items(cartItems) { item ->
                        CartItemCard(
                            item = item,
                            onQuantityChange = { newQuantity ->
                                productViewModel.updateCartQuantity(item.cartItem.id, newQuantity)
                            },
                            onRemove = {
                                productViewModel.removeFromCart(item.cartItem.id)
                            }
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun CartItemCard(
    item: CartItemWithProduct,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    // El precio ya está en CLP, no necesita conversión
    val clpAmount = item.product.price
    val clpFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply {
        currency = Currency.getInstance("CLP")
        maximumFractionDigits = 0
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val painter = runCatching { painterResource(id = item.product.imageResId) }.getOrNull()
            if (painter != null) {
                Image(
                    painter = painter,
                    contentDescription = item.product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(end = 12.dp)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.level_up_logo),
                    contentDescription = "Imagen no disponible",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(end = 12.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    item.product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    clpFormat.format(clpAmount),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Stock disponible: ${item.product.stock}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { onQuantityChange(item.cartItem.quantity - 1) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Text(
                            "-",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        item.cartItem.quantity.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(
                        onClick = { onQuantityChange(item.cartItem.quantity + 1) },
                        enabled = item.product.stock > item.cartItem.quantity,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Text(
                            "+",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                IconButton(onClick = onRemove) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
