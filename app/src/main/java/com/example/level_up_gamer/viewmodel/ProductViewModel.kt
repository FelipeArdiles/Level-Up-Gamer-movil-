// viewmodel/ProductViewModel.kt

package com.example.level_up_gamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer.R // 💡 Necesario para acceder a R.drawable
import com.example.level_up_gamer.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            // Simulamos la carga asíncrona de datos de videojuegos reales
            _products.value = listOf(
                Product(
                    id = 1,
                    name = "Elden Ring: Shadow of the Erdtree Edition",
                    price = 89.99,
                    description = "Aventura épica de mundo abierto de FromSoftware. Edición con DLC.",
                    imageResId = R.drawable.elden_ring, // ✅ Usa el recurso de imagen
                    stock = 5
                ),
                Product(
                    id = 2,
                    name = "The Legend of Zelda: Tears of the Kingdom",
                    price = 69.99,
                    description = "La última entrega de la saga de Hyrule, exploración y construcción.",
                    imageResId = R.drawable.zelda_totk, // ✅ Usa el recurso de imagen
                    stock = 0 // Simula producto sin stock
                ),
                Product(
                    id = 3,
                    name = "Cyberpunk 2077 (Ultimate Edition)",
                    price = 59.99,
                    description = "RPG futurista en Night City con la expansión Phantom Liberty.",
                    imageResId = R.drawable.cyberpunk, // ✅ Usa el recurso de imagen
                    stock = 12
                )
            )
        }
    }
}