// viewmodel/ProductViewModel.kt

package com.example.level_up_gamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer.model.Product
import com.example.level_up_gamer.model.CartItem
import com.example.level_up_gamer.model.Purchase
import com.example.level_up_gamer.model.PurchaseItem
import com.example.level_up_gamer.data.DatabaseProvider
import com.example.level_up_gamer.data.CartItemWithProduct
import com.example.level_up_gamer.data.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItemWithProduct>>(emptyList())
    val cartItems: StateFlow<List<CartItemWithProduct>> = _cartItems.asStateFlow()

    private val _cartItemCount = MutableStateFlow(0)
    val cartItemCount: StateFlow<Int> = _cartItemCount.asStateFlow()

    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    data class ProductUiState(
        val isLoading: Boolean = false,
        val isProcessing: Boolean = false,
        val errorMessage: String? = null,
        val successMessage: String? = null
    )

    init {
        loadProducts()
        loadCart()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                _products.value = DatabaseProvider.db().productDao().getAll()
                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _products.value = emptyList()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al cargar productos: ${e.message}"
                )
            }
        }
    }

    private fun loadCart() {
        viewModelScope.launch {
            try {
                val cartItems = DatabaseProvider.db().cartDao().getAll()
                val productDao = DatabaseProvider.db().productDao()
                val itemsWithProducts = cartItems.mapNotNull { cartItem ->
                    try {
                        val product = productDao.getById(cartItem.productId)
                        if (product != null) {
                            CartItemWithProduct(cartItem, product)
                        } else {
                            null
                        }
                    } catch (e: Exception) {
                        null
                    }
                }
                _cartItems.value = itemsWithProducts
                val totalItems = DatabaseProvider.db().cartDao().getTotalItems()
                // Asegurar que el conteo se actualice incluso si es 0
                _cartItemCount.value = totalItems
            } catch (e: Exception) {
                _cartItems.value = emptyList()
                _cartItemCount.value = 0
                // No mostramos error en la UI para evitar interrumpir el flujo
            }
        }
    }

    fun addToCart(productId: Int, quantity: Int = 1) {
        viewModelScope.launch {
            try {
                val product = DatabaseProvider.db().productDao().getById(productId)
                if (product == null) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Producto no encontrado"
                    )
                    return@launch
                }

                if (product.stock < quantity) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Stock insuficiente. Disponible: ${product.stock}"
                    )
                    return@launch
                }

                val existingCartItem = DatabaseProvider.db().cartDao().getByProductId(productId)
                if (existingCartItem != null) {
                    val newQuantity = existingCartItem.quantity + quantity
                    if (product.stock < newQuantity) {
                        _uiState.value = _uiState.value.copy(
                            errorMessage = "Stock insuficiente. Disponible: ${product.stock}"
                        )
                        return@launch
                    }
                    DatabaseProvider.db().cartDao().updateQuantity(productId, quantity)
                } else {
                    DatabaseProvider.db().cartDao().insert(CartItem(productId = productId, quantity = quantity))
                }

                loadCart()
                _uiState.value = _uiState.value.copy(
                    successMessage = "${product.name} agregado al carrito"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al agregar al carrito: ${e.message}"
                )
            }
        }
    }

    fun removeFromCart(cartItemId: Long) {
        viewModelScope.launch {
            try {
                val cartItem = _cartItems.value.firstOrNull { it.cartItem.id == cartItemId }
                if (cartItem != null) {
                    DatabaseProvider.db().cartDao().delete(cartItem.cartItem)
                    // Recargar el carrito inmediatamente para actualizar el conteo
                    loadCart()
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al eliminar del carrito: ${e.message}"
                )
                // Aún así, intentar recargar el carrito
                loadCart()
            }
        }
    }

    fun updateCartQuantity(cartItemId: Long, newQuantity: Int) {
        viewModelScope.launch {
            try {
                if (newQuantity <= 0) {
                    removeFromCart(cartItemId)
                    return@launch
                }

                val cartItemWithProduct = _cartItems.value.firstOrNull { it.cartItem.id == cartItemId }
                if (cartItemWithProduct == null) return@launch

                if (cartItemWithProduct.product.stock < newQuantity) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Stock insuficiente. Disponible: ${cartItemWithProduct.product.stock}"
                    )
                    return@launch
                }

                val updatedCartItem = cartItemWithProduct.cartItem.copy(quantity = newQuantity)
                DatabaseProvider.db().cartDao().insert(updatedCartItem)
                loadCart()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al actualizar cantidad: ${e.message}"
                )
            }
        }
    }

    fun checkout() {
        viewModelScope.launch {
            try {
                val items = _cartItems.value
                if (items.isEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "El carrito está vacío"
                    )
                    return@launch
                }

                // Verificar stock antes de comprar
                for (item in items) {
                    val product = DatabaseProvider.db().productDao().getById(item.product.id)
                    if (product == null || product.stock < item.cartItem.quantity) {
                        _uiState.value = _uiState.value.copy(
                            errorMessage = "${item.product.name}: Stock insuficiente"
                        )
                        return@launch
                    }
                }

                // El precio ya está en CLP, calcular el total directamente
                val totalAmount = items.sumOf { it.product.price * it.cartItem.quantity }
                
                // Obtener el ID del usuario actual
                val userId = SessionManager.getCurrentUserId()
                if (userId == null) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "No hay sesión activa"
                    )
                    return@launch
                }

                // Crear la compra
                val purchase = Purchase(
                    userId = userId,
                    totalAmount = totalAmount,
                    purchaseDate = System.currentTimeMillis()
                )
                val purchaseId = DatabaseProvider.db().purchaseDao().insertPurchase(purchase)

                // Crear los items de la compra (precios ya están en CLP)
                val purchaseItems = items.map { item ->
                    PurchaseItem(
                        purchaseId = purchaseId,
                        productId = item.product.id,
                        productName = item.product.name,
                        quantity = item.cartItem.quantity,
                        unitPrice = item.product.price,
                        subtotal = item.product.price * item.cartItem.quantity
                    )
                }
                DatabaseProvider.db().purchaseItemDao().insertPurchaseItems(purchaseItems)

                // Actualizar stock y limpiar carrito
                for (item in items) {
                    DatabaseProvider.db().productDao().decreaseStock(item.product.id, item.cartItem.quantity)
                }
                DatabaseProvider.db().cartDao().clear()

                loadProducts()
                loadCart()
                _uiState.value = _uiState.value.copy(
                    successMessage = "Compra realizada exitosamente"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al procesar compra: ${e.message}"
                )
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }

    fun refreshProducts() {
        loadProducts()
    }

    fun refreshCart() {
        loadCart()
    }

    fun getProduct(productId: Int): Product? = _products.value.firstOrNull { it.id == productId }

    fun createProduct(
        name: String,
        description: String,
        price: Double,
        stock: Int,
        imageResId: Int,
        imagePath: String? = null
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isProcessing = true,
                    errorMessage = null,
                    successMessage = null
                )
                val dao = DatabaseProvider.db().productDao()
                val nextId = (_products.value.maxOfOrNull { it.id } ?: 0) + 1
                val safeStock = stock.coerceAtLeast(0)
                dao.insert(
                    Product(
                        id = nextId,
                        name = name,
                        price = price,
                        description = description,
                        imageResId = imageResId,
                        imagePath = imagePath,
                        stock = safeStock
                    )
                )
                loadProducts()
                _uiState.value = _uiState.value.copy(
                    isProcessing = false,
                    successMessage = "Producto creado"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isProcessing = false,
                    errorMessage = "Error al crear producto: ${e.message}"
                )
            }
        }
    }

    fun updateProduct(
        productId: Int,
        name: String,
        description: String,
        price: Double,
        stock: Int,
        imageResId: Int,
        imagePath: String? = null
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isProcessing = true,
                    errorMessage = null,
                    successMessage = null
                )
                val dao = DatabaseProvider.db().productDao()
                // Si hay una imagen anterior diferente, eliminarla
                val oldProduct = dao.getById(productId)
                oldProduct?.imagePath?.let { oldPath ->
                    if (oldPath != imagePath) {
                        // La imagen anterior ya no se usa, se puede eliminar
                        // (opcional: implementar limpieza de imágenes no usadas)
                    }
                }
                dao.insert(
                    Product(
                        id = productId,
                        name = name,
                        price = price,
                        description = description,
                        imageResId = imageResId,
                        imagePath = imagePath,
                        stock = stock.coerceAtLeast(0)
                    )
                )
                loadProducts()
                _uiState.value = _uiState.value.copy(
                    isProcessing = false,
                    successMessage = "Producto actualizado"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isProcessing = false,
                    errorMessage = "Error al actualizar producto: ${e.message}"
                )
            }
        }
    }

    fun deleteProduct(productId: Int) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isProcessing = true,
                    errorMessage = null,
                    successMessage = null
                )
                // Eliminar del carrito si existe
                val cartItem = DatabaseProvider.db().cartDao().getByProductId(productId)
                cartItem?.let {
                    DatabaseProvider.db().cartDao().delete(it)
                }
                // Eliminar el producto
                DatabaseProvider.db().productDao().deleteById(productId)
                loadProducts()
                loadCart()
                _uiState.value = _uiState.value.copy(
                    isProcessing = false,
                    successMessage = "Producto eliminado"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isProcessing = false,
                    errorMessage = "Error al eliminar producto: ${e.message}"
                )
            }
        }
    }

}