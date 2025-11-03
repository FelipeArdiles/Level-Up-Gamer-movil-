package com.example.level_up_gamer.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.level_up_gamer.R // 💡 Importante para R.drawable
import com.example.level_up_gamer.model.Product
import com.example.level_up_gamer.ui.navigation.Screen
import com.example.level_up_gamer.viewmodel.ProductViewModel
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

// 💡 Anotación necesaria para el TopAppBar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductMenuScreen(
    navController: NavController,
    productViewModel: ProductViewModel
) {
    val products by productViewModel.products.collectAsState()
    val cartItemCount by productViewModel.cartItemCount.collectAsState()
    val uiState by productViewModel.uiState.collectAsState()

    // Recargar el carrito cuando se vuelve a esta pantalla
    LaunchedEffect(navController.currentBackStackEntry) {
        productViewModel.refreshCart()
    }

    Scaffold(
        topBar = {
            // ✅ INICIO DE SECCIÓN MODIFICADA (TopAppBar con Logo)
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.level_up_logo),
                            contentDescription = "Logo Level Up Gamer",
                            modifier = Modifier.size(32.dp) // Tamaño del logo
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Menú de Productos")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Cart.route) }) {
                        val count = cartItemCount
                        if (count > 0) {
                            BadgedBox(badge = { Badge { Text(count.toString()) } }) {
                                Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                            }
                        } else {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                        }
                    }
                    IconButton(onClick = { navController.navigate(Screen.UserProfile.route) }) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil")
                    }
                }
            )
            // ✅ FIN DE SECCIÓN MODIFICADA
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (uiState.isLoading) {
                item { 
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else if (products.isEmpty()) {
                item { 
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay productos disponibles", modifier = Modifier.padding(16.dp))
                    }
                }
            } else {
                // Mostrar mensajes de estado
                uiState.errorMessage?.let { error ->
                    item {
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
                uiState.successMessage?.let { success ->
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Text(
                                text = success,
                                modifier = Modifier.padding(16.dp),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
                items(products) { product ->
                    ProductCard(
                        product = product,
                        onAddToCart = { productViewModel.addToCart(product.id) }
                    )
                }
            }
        }
    }
}

// Componente de Tarjeta de Producto
@Composable
fun ProductCard(
    product: Product,
    onAddToCart: () -> Unit
) {
    val stockColor = when {
        product.stock > 10 -> Color.Green
        product.stock > 0 -> Color.Yellow
        else -> Color.Red
    }

    val eurToClp = 1000.0
    val clpAmount = product.price * eurToClp
    val clpFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply {
        currency = Currency.getInstance("CLP")
        maximumFractionDigits = 0
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // 1. Imagen del Producto
                val painter = runCatching { painterResource(id = product.imageResId) }.getOrNull()
                if (painter != null) {
                    Image(
                        painter = painter,
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(end = 12.dp)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.level_up_logo),
                        contentDescription = "Imagen no disponible",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(end = 12.dp)
                    )
                }

                // 2. Información del Producto
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = product.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        maxLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Precio y Stock en la misma fila
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = clpFormat.format(clpAmount),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (product.stock > 0) "Stock: ${product.stock}" else "¡AGOTADO!",
                            color = stockColor,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .background(stockColor.copy(alpha = 0.1f), shape = MaterialTheme.shapes.small)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
            
            // Botón Agregar abajo
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onAddToCart,
                enabled = product.stock > 0,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Agregar al Carrito")
            }
        }
    }
}