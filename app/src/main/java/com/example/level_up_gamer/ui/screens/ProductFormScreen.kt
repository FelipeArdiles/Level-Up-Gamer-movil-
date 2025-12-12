package com.example.level_up_gamer.ui.screens

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.level_up_gamer.R
import com.example.level_up_gamer.utils.AdminUtils
import com.example.level_up_gamer.utils.ImageManager
import com.example.level_up_gamer.viewmodel.ProductViewModel
import com.example.level_up_gamer.viewmodel.UserViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch

// Formato chileno: punto como separador de miles
private val numberFormat = DecimalFormat("#,###", DecimalFormatSymbols(Locale("es", "CL")))

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ProductFormScreen(
    navController: NavController,
    productId: Int? = null,
    productViewModel: ProductViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    val context = LocalContext.current
    val products by productViewModel.products.collectAsState()
    val uiState by productViewModel.uiState.collectAsState()
    val currentUser by userViewModel.userProfile.collectAsState()
    val isAdmin = AdminUtils.isAdmin(currentUser)
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val currentProduct = products.firstOrNull { it.id == productId }
    val isEdit = productId != null && currentProduct != null

    var name by rememberSaveable { mutableStateOf(currentProduct?.name.orEmpty()) }
    var description by rememberSaveable { mutableStateOf(currentProduct?.description.orEmpty()) }
    // Mostrar el precio completo en CLP (sin dividir por 1000)
    var priceInput by rememberSaveable { 
        mutableStateOf(
            currentProduct?.price?.let { 
                numberFormat.format(it.toLong())
            } ?: ""
        )
    }
    var stockInput by rememberSaveable { mutableStateOf(currentProduct?.stock?.toString().orEmpty()) }
    var selectedImageResId by rememberSaveable {
        mutableStateOf(currentProduct?.imageResId ?: R.drawable.level_up_logo)
    }
    var selectedImagePath by rememberSaveable {
        mutableStateOf<String?>(currentProduct?.imagePath)
    }
    var imageMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var useCustomImage by rememberSaveable { mutableStateOf(currentProduct?.imagePath != null) }

    val imageOptions = PRODUCT_IMAGE_OPTIONS

    // Estado para controlar cuándo abrir el selector después de otorgar permisos
    var shouldOpenPickerAfterPermission by remember { mutableStateOf(false) }

    // Launcher para seleccionar imagen desde galería/archivos
    // NOTA: GetContent() en Android 13+ (API 33+) no requiere permisos explícitos
    // En versiones anteriores puede necesitar READ_EXTERNAL_STORAGE
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            coroutineScope.launch {
                val savedPath = ImageManager.saveImageFromUri(context, it)
                if (savedPath != null) {
                    selectedImagePath = savedPath
                    useCustomImage = true
                    snackbarHostState.showSnackbar("Imagen cargada correctamente")
                } else {
                    snackbarHostState.showSnackbar("Error al cargar la imagen")
                }
            }
        }
    }

    // Permisos: solo necesarios para Android 12 y anteriores
    // En Android 13+, GetContent() funciona sin permisos explícitos
    val readExternalStoragePermission = rememberPermissionState(
        permission = Manifest.permission.READ_EXTERNAL_STORAGE
    )

    // Función para abrir el selector de imágenes
    fun openImagePicker() {
        // En Android 13+ (API 33+), GetContent() no requiere permisos, abrir directamente
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            imagePickerLauncher.launch("image/*")
            return
        }

        // Para Android 12 y anteriores, verificar si tenemos permiso
        if (readExternalStoragePermission.status.isGranted) {
            // Permiso otorgado, abrir selector
            imagePickerLauncher.launch("image/*")
        } else {
            // No tenemos permiso, pedirlo y marcar para abrir después
            shouldOpenPickerAfterPermission = true
            readExternalStoragePermission.launchPermissionRequest()
        }
    }

    // Cuando se otorgan los permisos, abrir automáticamente el selector
    LaunchedEffect(readExternalStoragePermission.status.isGranted) {
        if (readExternalStoragePermission.status.isGranted && shouldOpenPickerAfterPermission) {
            shouldOpenPickerAfterPermission = false
            // Esperar un momento para que el diálogo de permisos se cierre completamente
            kotlinx.coroutines.delay(500)
            imagePickerLauncher.launch("image/*")
        }
    }

    LaunchedEffect(currentProduct) {
        currentProduct?.let {
            name = it.name
            description = it.description
            // Mostrar el precio completo en CLP con formato de miles
            priceInput = numberFormat.format(it.price.toLong())
            stockInput = it.stock.toString()
            selectedImageResId = it.imageResId
            selectedImagePath = it.imagePath
            useCustomImage = it.imagePath != null
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Solo los administradores pueden modificar productos",
                    style = MaterialTheme.typography.bodyLarge
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
            // Mostrar imagen: desde archivo o recurso
            if (useCustomImage && selectedImagePath != null) {
                val imageFile = ImageManager.getImageFile(context, selectedImagePath)
                if (imageFile != null && imageFile.exists()) {
                    AsyncImage(
                        model = imageFile,
                        contentDescription = "Imagen del producto",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = selectedImageResId),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            } else {
                Image(
                    painter = painterResource(id = selectedImageResId),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
            }

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
                onValueChange = { newValue ->
                    // Solo permitir números, remover separadores de miles para guardar
                    val digitsOnly = newValue.filter { it.isDigit() }
                    if (digitsOnly.isEmpty()) {
                        priceInput = ""
                    } else {
                        // Formatear con separadores de miles mientras el usuario escribe
                        val number = digitsOnly.toLongOrNull() ?: 0L
                        priceInput = numberFormat.format(number)
                    }
                },
                label = { Text("Precio (CLP)") },
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

            // Botón para cargar imagen desde dispositivo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        openImagePicker()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.Image,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Cargar desde dispositivo")
                }
                
                if (useCustomImage && selectedImagePath != null) {
                    OutlinedButton(
                        onClick = {
                            selectedImagePath = null
                            useCustomImage = false
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Usar imagen predefinida")
                    }
                }
            }

            // Dropdown para seleccionar imagen predefinida (solo si no hay imagen personalizada)
            if (!useCustomImage) {
                ExposedDropdownMenuBox(
                    expanded = imageMenuExpanded,
                    onExpandedChange = { imageMenuExpanded = !imageMenuExpanded }
                ) {
                    OutlinedTextField(
                        value = imageOptions.firstOrNull { it.resId == selectedImageResId }?.label
                            ?: "Logo Level Up",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Imagen predefinida") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = imageMenuExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
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
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    // Remover separadores de miles (punto en formato chileno) y convertir a número
                    val priceString = priceInput.replace(".", "").replace(",", "")
                    val price = priceString.toDoubleOrNull() ?: 0.0
                    val stock = stockInput.toIntOrNull() ?: 0
                    if (isEdit) {
                        productViewModel.updateProduct(
                            productId = productId,
                            name = name.trim(),
                            description = description.trim(),
                            price = price,
                            stock = stock,
                            imageResId = selectedImageResId,
                            imagePath = if (useCustomImage) selectedImagePath else null
                        )
                    } else {
                        productViewModel.createProduct(
                            name = name.trim(),
                            description = description.trim(),
                            price = price,
                            stock = stock,
                            imageResId = selectedImageResId,
                            imagePath = if (useCustomImage) selectedImagePath else null
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
