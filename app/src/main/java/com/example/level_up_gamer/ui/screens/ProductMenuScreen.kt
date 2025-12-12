package com.example.level_up_gamer.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.level_up_gamer.R
import com.example.level_up_gamer.model.Product
import com.example.level_up_gamer.ui.navigation.Screen
import com.example.level_up_gamer.ui.theme.rememberNeonBackgroundBrush
import com.example.level_up_gamer.ui.theme.rememberAnimatedNeonBackgroundBrush
import com.example.level_up_gamer.ui.theme.animateEnterScale
import com.example.level_up_gamer.ui.theme.animateEnterFade
import com.example.level_up_gamer.ui.theme.PrimaryGreen
import com.example.level_up_gamer.ui.theme.SurfaceDarkElevated
import com.example.level_up_gamer.ui.components.BottomNavigationBar
import com.example.level_up_gamer.ui.components.getBottomNavItems
import com.example.level_up_gamer.utils.AdminUtils
import com.example.level_up_gamer.utils.ImageManager
import com.example.level_up_gamer.viewmodel.ProductViewModel
import com.example.level_up_gamer.viewmodel.UserViewModel
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductMenuScreen(
    navController: NavController,
    productViewModel: ProductViewModel,
    userViewModel: UserViewModel = viewModel()
) {
    val products by productViewModel.products.collectAsState()
    val cartItemCount by productViewModel.cartItemCount.collectAsState()
    val uiState by productViewModel.uiState.collectAsState()
    val currentUser by userViewModel.userProfile.collectAsState()
    val isAdmin = AdminUtils.isAdmin(currentUser)
    val backgroundBrush = rememberAnimatedNeonBackgroundBrush()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Estado local para controlar la visibilidad del mensaje de éxito
    var showSuccessMessage by remember { mutableStateOf(false) }

    // Manejar mensajes de éxito: mostrar y ocultar automáticamente después de 3 segundos
    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            // Mostrar el mensaje
            showSuccessMessage = true
            // Esperar 3 segundos antes de ocultar y limpiar el mensaje
            kotlinx.coroutines.delay(3000)
            showSuccessMessage = false
            productViewModel.clearMessages()
        } else {
            // Si el mensaje se limpia externamente, ocultar también
            showSuccessMessage = false
        }
    }

    LaunchedEffect(navController.currentBackStackEntry) {
        productViewModel.refreshCart()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                BottomNavigationBar(
                    items = getBottomNavItems(),
                    selectedRoute = navController.currentDestination?.route,
                    onItemClick = { route ->
                        navController.navigate(route) {
                            // Evitar múltiples instancias de la misma pantalla
                            launchSingleTop = true
                            // Si estamos en la misma pantalla, no hacer nada
                            if (navController.currentDestination?.route == route) {
                                return@navigate
                            }
                            // Limpiar el back stack hasta la pantalla de inicio
                            popUpTo(Screen.ProductMenu.route) {
                                saveState = true
                            }
                            // Restaurar el estado al volver
                            restoreState = true
                        }
                    }
                )
            },
            topBar = {
                Surface(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TopAppBar(
                        title = {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.level_up_logo),
                                    contentDescription = "Logo Level Up Gamer",
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            titleContentColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            },
            floatingActionButton = {
                if (isAdmin) {
                    FloatingActionButton(
                        onClick = { navController.navigate(Screen.AddProduct.route) },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar producto")
                    }
                }
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { paddingValues ->
            LazyColumn(
                contentPadding = paddingValues,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
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
                            Text(
                                "No hay productos disponibles",
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(16.dp)
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
                    uiState.successMessage?.let { success ->
                        item {
                            AnimatedVisibility(
                                visible = showSuccessMessage,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically()
                            ) {
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
                    }
                    items(products.size) { index ->
                        val product = products[index]
                        ProductCard(
                            product = product,
                            onAddToCart = { productViewModel.addToCart(product.id) },
                            onEditProduct = {
                                navController.navigate(Screen.EditProduct.buildRoute(product.id))
                            },
                            canEdit = isAdmin,
                            index = index
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onAddToCart: () -> Unit,
    onEditProduct: () -> Unit,
    canEdit: Boolean = false,
    index: Int = 0
) {
    val context = LocalContext.current
    val stockColor = when {
        product.stock > 10 -> Color(0xFF00FF88)
        product.stock > 0 -> Color(0xFFFFAA00)
        else -> Color(0xFFFF4040)
    }

    val eurToClp = 1000.0
    val clpAmount = product.price * eurToClp
    val clpFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply {
        currency = Currency.getInstance("CLP")
        maximumFractionDigits = 0
    }
    
    // Animación de entrada
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(150),
        label = "card_scale"
    )
    
    val elevation by animateFloatAsState(
        targetValue = if (isPressed) 4f else 12f,
        animationSpec = tween(150),
        label = "card_elevation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .animateEnterScale(enabled = true, initialScale = 0.8f)
            .animateEnterFade(enabled = true)
            .scale(scale)
            .shadow(elevation.dp, shape = RoundedCornerShape(20.dp), spotColor = PrimaryGreen.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceDarkElevated.copy(alpha = 0.95f)
        ),
        border = BorderStroke(
            1.5.dp,
            PrimaryGreen.copy(alpha = if (isPressed) 0.6f else 0.4f)
        )
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
                // Mostrar imagen desde archivo si existe, sino desde recurso
                if (!product.imagePath.isNullOrBlank()) {
                    val imageFile = ImageManager.getImageFile(context, product.imagePath)
                    if (imageFile != null && imageFile.exists()) {
                        AsyncImage(
                            model = imageFile,
                            contentDescription = product.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .padding(end = 12.dp)
                        )
                    } else {
                        // Fallback a imagen predefinida si el archivo no existe
                        Image(
                            painter = painterResource(id = product.imageResId),
                            contentDescription = product.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .padding(end = 12.dp)
                        )
                    }
                } else {
                    // Usar imagen predefinida
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
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = product.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = clpFormat.format(clpAmount),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Stock: ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${product.stock}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = stockColor
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(stockColor, shape = CircleShape)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val addToCartInteraction = remember { MutableInteractionSource() }
                val isAddToCartPressed by addToCartInteraction.collectIsPressedAsState()
                val addToCartScale by animateFloatAsState(
                    targetValue = if (isAddToCartPressed) 0.9f else 1f,
                    animationSpec = tween(100),
                    label = "button_scale"
                )
                
                Button(
                    onClick = onAddToCart,
                    enabled = product.stock > 0,
                    modifier = Modifier
                        .weight(1f)
                        .scale(addToCartScale),
                    interactionSource = addToCartInteraction,
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen,
                        contentColor = Color.Black,
                        disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
                    )
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Al carrito", fontWeight = FontWeight.Bold)
                }
                if (canEdit) {
                    val editInteraction = remember { MutableInteractionSource() }
                    val isEditPressed by editInteraction.collectIsPressedAsState()
                    val editScale by animateFloatAsState(
                        targetValue = if (isEditPressed) 0.9f else 1f,
                        animationSpec = tween(100),
                        label = "edit_scale"
                    )
                    
                    OutlinedButton(
                        onClick = onEditProduct,
                        modifier = Modifier
                            .weight(1f)
                            .scale(editScale),
                        interactionSource = editInteraction,
                        border = BorderStroke(1.5.dp, PrimaryGreen.copy(alpha = 0.7f)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = PrimaryGreen
                        )
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Editar", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
