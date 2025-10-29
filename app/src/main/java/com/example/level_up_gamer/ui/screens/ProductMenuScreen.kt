// ui/screens/ProductMenuScreen.kt

package com.example.level_up_gamer.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.level_up_gamer.model.Product
import com.example.level_up_gamer.viewmodel.ProductViewModel
import com.example.level_up_gamer.ui.navigation.Screen



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductMenuScreen(
    navController: NavController,
    productViewModel: ProductViewModel = viewModel()
) {
    val products by productViewModel.products.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Menú Level Up Gamer") },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.UserProfile.route) }) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(8.dp) // Espaciado entre tarjetas
        ) {
            if (products.isEmpty()) {
                item { Text("Cargando productos...", modifier = Modifier.padding(16.dp)) }
            } else {
                items(products) { product ->
                    ProductCard(product = product)
                }
            }
        }
    }
}

// 💡 Nuevo y mejorado componente de tarjeta de producto
@Composable
fun ProductCard(product: Product) {
    // Definimos el color del indicador de stock
    val stockColor = when {
        product.stock > 10 -> Color.Green
        product.stock > 0 -> Color.Yellow
        else -> Color.Red
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
            // 1. Imagen del Producto
            Image(
                painter = painterResource(id = product.imageResId),
                contentDescription = product.name,
                contentScale = ContentScale.Crop, // Escala para llenar el espacio
                modifier = Modifier
                    .size(90.dp)
                    .padding(end = 12.dp)
            )

            // 2. Columna de Texto
            Column(
                modifier = Modifier.weight(1f) // Usa el espacio restante
            ) {
                Text(
                    product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    product.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 2
                )
            }

            // 3. Columna de Precio y Stock
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    "€${String.format("%.2f", product.price)}", // Formato de moneda
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Indicador de Stock
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
}

