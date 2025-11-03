package com.example.level_up_gamer.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.level_up_gamer.R
import com.example.level_up_gamer.data.CartItemWithProduct
import com.example.level_up_gamer.ui.navigation.Screen
import com.example.level_up_gamer.viewmodel.ProductViewModel
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    productViewModel: ProductViewModel = viewModel()
) {
    val cartItems by productViewModel.cartItems.collectAsState()
    val uiState by productViewModel.uiState.collectAsState()

    LaunchedEffect(uiState.errorMessage, uiState.successMessage) {
        if (uiState.errorMessage != null || uiState.successMessage != null) {
            kotlinx.coroutines.delay(3000)
            productViewModel.clearMessages()
        }
    }
    
    // Asegurar que el carrito se recargue cuando se elimina algo
    LaunchedEffect(cartItems.size) {
        productViewModel.refreshCart()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.level_up_logo),
                            contentDescription = "Logo",
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Carrito de Compras")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                val total = cartItems.sumOf { it.product.price * it.cartItem.quantity }
                val eurToClp = 1000.0
                val clpTotal = total * eurToClp
                val clpFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply {
                    currency = Currency.getInstance("CLP")
                    maximumFractionDigits = 0
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    tonalElevation = 8.dp
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
                                fontWeight = FontWeight.Bold
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
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (cartItems.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("El carrito está vacío")
                    }
                }
            } else {
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

@Composable
fun CartItemCard(
    item: CartItemWithProduct,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    val eurToClp = 1000.0
    val clpAmount = item.product.price * eurToClp
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
                    fontWeight = FontWeight.Bold
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
                        Text("-", style = MaterialTheme.typography.titleLarge)
                    }
                    Text(
                        item.cartItem.quantity.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    IconButton(
                        onClick = { onQuantityChange(item.cartItem.quantity + 1) },
                        enabled = item.product.stock > item.cartItem.quantity,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Text("+", style = MaterialTheme.typography.titleLarge)
                    }
                }
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}

