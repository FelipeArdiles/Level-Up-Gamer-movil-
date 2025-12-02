package com.example.level_up_gamer.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.level_up_gamer.R
import com.example.level_up_gamer.utils.AdminUtils
import com.example.level_up_gamer.viewmodel.ProductViewModel
import com.example.level_up_gamer.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFormScreen(
    navController: NavController,
    productId: Int? = null,
    productViewModel: ProductViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    val products by productViewModel.products.collectAsState()
    val uiState by productViewModel.uiState.collectAsState()
    val currentUser by userViewModel.userProfile.collectAsState()
    val isAdmin = AdminUtils.isAdmin(currentUser)
    val snackbarHostState = remember { SnackbarHostState() }

    val currentProduct = products.firstOrNull { it.id == productId }
    val isEdit = productId != null && currentProduct != null

    var name by rememberSaveable { mutableStateOf(currentProduct?.name.orEmpty()) }
    var description by rememberSaveable { mutableStateOf(currentProduct?.description.orEmpty()) }
    var priceInput by rememberSaveable { mutableStateOf(currentProduct?.price?.toString().orEmpty()) }
    var stockInput by rememberSaveable { mutableStateOf(currentProduct?.stock?.toString().orEmpty()) }
    var selectedImageResId by rememberSaveable {
        mutableStateOf(currentProduct?.imageResId ?: R.drawable.level_up_logo)
    }
    var imageMenuExpanded by rememberSaveable { mutableStateOf(false) }

    val imageOptions = PRODUCT_IMAGE_OPTIONS

    LaunchedEffect(currentProduct) {
        currentProduct?.let {
            name = it.name
            description = it.description
            priceInput = it.price.toString()
            stockInput = it.stock.toString()
            selectedImageResId = it.imageResId
        }
    }

    LaunchedEffect(uiState.successMessage) {
        val message = uiState.successMessage
        if (!message.isNullOrBlank()) {
            snackbarHostState.showSnackbar(message)
            productViewModel.clearMessages()
            navController.popBackStack()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        val message = uiState.errorMessage
        if (!message.isNullOrBlank()) {
            snackbarHostState.showSnackbar(message)
            productViewModel.clearMessages()
        }
    }

    // Verificar permisos de administrador
    LaunchedEffect(isAdmin) {
        if (!isAdmin) {
            snackbarHostState.showSnackbar("Solo los administradores pueden modificar productos")
            navController.popBackStack()
        }
    }

    if (!isAdmin) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Acceso denegado") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Solo los administradores pueden modificar productos",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
                )
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEdit) "Editar producto" else "Agregar producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(snackbarData = data)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = selectedImageResId),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = priceInput,
                onValueChange = { priceInput = it.filter { char -> char.isDigit() || char == '.' || char == ',' } },
                label = { Text("Precio (EUR)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = VisualTransformation.None
            )

            OutlinedTextField(
                value = stockInput,
                onValueChange = { stockInput = it.filter { char -> char.isDigit() } },
                label = { Text("Stock disponible") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                enabled = isAdmin
            )

            ExposedDropdownMenuBox(
                expanded = imageMenuExpanded,
                onExpandedChange = { imageMenuExpanded = !imageMenuExpanded }
            ) {
                OutlinedTextField(
                    value = imageOptions.firstOrNull { it.resId == selectedImageResId }?.label
                        ?: "Logo Level Up",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Imagen del producto") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = imageMenuExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = imageMenuExpanded,
                    onDismissRequest = { imageMenuExpanded = false }
                ) {
                    imageOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.label) },
                            onClick = {
                                selectedImageResId = option.resId
                                imageMenuExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    val price = priceInput.replace(",", ".").toDoubleOrNull() ?: 0.0
                    val stock = stockInput.toIntOrNull() ?: 0
                    if (isEdit && productId != null) {
                        productViewModel.updateProduct(
                            productId = productId,
                            name = name.trim(),
                            description = description.trim(),
                            price = price,
                            stock = stock,
                            imageResId = selectedImageResId
                        )
                    } else {
                        productViewModel.createProduct(
                            name = name.trim(),
                            description = description.trim(),
                            price = price,
                            stock = stock,
                            imageResId = selectedImageResId
                        )
                    }
                },
                enabled = name.isNotBlank() && description.isNotBlank() && priceInput.isNotBlank() && stockInput.isNotBlank() && !uiState.isProcessing,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (uiState.isProcessing) "Guardando..."
                    else if (isEdit) "Actualizar producto" else "Crear producto"
                )
            }
        }
    }
}

private val PRODUCT_IMAGE_OPTIONS = listOf(
    ProductImageOption("Elden Ring", R.drawable.elden_ring),
    ProductImageOption("Zelda TOTK", R.drawable.zelda_totk),
    ProductImageOption("Cyberpunk 2077", R.drawable.cyberpunk),
    ProductImageOption("GTA VI", R.drawable.gta_vi),
    ProductImageOption("Call of Duty BO6", R.drawable.cod_bo6),
    ProductImageOption("EA FC 25", R.drawable.ea_fc25),
    ProductImageOption("Helldivers 2", R.drawable.helldivers2),
    ProductImageOption("Baldur's Gate 3", R.drawable.baldurs_gate_3),
    ProductImageOption("Starfield", R.drawable.starfield),
    ProductImageOption("Dragon's Dogma 2", R.drawable.dragons_dogma_2),
    ProductImageOption("Alan Wake 2", R.drawable.alan_wake_2),
    ProductImageOption("Assassin's Creed Mirage", R.drawable.ac_mirage),
    ProductImageOption("Resident Evil 4 Remake", R.drawable.re4_remake),
    ProductImageOption("Logo Level Up", R.drawable.level_up_logo)
)

private data class ProductImageOption(
    val label: String,
    @DrawableRes val resId: Int
)

