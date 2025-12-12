package com.example.level_up_gamer.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.level_up_gamer.R
import com.example.level_up_gamer.model.Store
import com.example.level_up_gamer.ui.components.BottomNavigationBar
import com.example.level_up_gamer.ui.components.getBottomNavItems
import com.example.level_up_gamer.ui.navigation.Screen
import com.mapbox.common.MapboxOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState

// Función para generar tiendas ficticias en Santiago
fun generateSantiagoStores(): List<Store> {
    // Ubicaciones reales en Santiago de Chile (coordenadas aproximadas)
    return listOf(
        Store(
            id = "store_1",
            name = "Level Up Gamer - Centro",
            address = "Av. Libertador Bernardo O'Higgins 1234, Santiago",
            phone = "+56 2 2345 6789",
            location = Point.fromLngLat(-70.6483, -33.4489) // Cerca de Plaza de Armas
        ),
        Store(
            id = "store_2",
            name = "Level Up Gamer - Providencia",
            address = "Av. Providencia 2567, Providencia",
            phone = "+56 2 2345 6790",
            location = Point.fromLngLat(-70.6100, -33.4320) // Providencia
        ),
        Store(
            id = "store_3",
            name = "Level Up Gamer - Las Condes",
            address = "Av. Apoquindo 4500, Las Condes",
            phone = "+56 2 2345 6791",
            location = Point.fromLngLat(-70.5500, -33.4100) // Las Condes
        ),
        Store(
            id = "store_4",
            name = "Level Up Gamer - Ñuñoa",
            address = "Av. Irarrázaval 3456, Ñuñoa",
            phone = "+56 2 2345 6792",
            location = Point.fromLngLat(-70.6000, -33.4500) // Ñuñoa
        ),
        Store(
            id = "store_5",
            name = "Level Up Gamer - Maipú",
            address = "Av. Pajaritos 5678, Maipú",
            phone = "+56 2 2345 6793",
            location = Point.fromLngLat(-70.7500, -33.5100) // Maipú
        ),
        Store(
            id = "store_6",
            name = "Level Up Gamer - San Miguel",
            address = "Av. Gran Avenida 8901, San Miguel",
            phone = "+56 2 2345 6794",
            location = Point.fromLngLat(-70.6500, -33.4900) // San Miguel
        ),
        Store(
            id = "store_7",
            name = "Level Up Gamer - La Florida",
            address = "Av. Vicuña Mackenna 12345, La Florida",
            phone = "+56 2 2345 6795",
            location = Point.fromLngLat(-70.5800, -33.5200) // La Florida
        ),
        Store(
            id = "store_8",
            name = "Level Up Gamer - Estación Central",
            address = "Av. Alameda 6789, Estación Central",
            phone = "+56 2 2345 6796",
            location = Point.fromLngLat(-70.6800, -33.4500) // Estación Central
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoresMapScreen(navController: NavController) {
    val context = LocalContext.current
    
    // Configurar el token de Mapbox si no está configurado
    if (MapboxOptions.accessToken == null || MapboxOptions.accessToken!!.isEmpty()) {
        MapboxOptions.accessToken = context.getString(R.string.mapbox_access_token)
    }
    
    // Generar tiendas ficticias en Santiago
    val stores = remember { generateSantiagoStores() }
    
    // Estado del viewport del mapa - centrado en Santiago
    val santiagoCenter = Point.fromLngLat(-70.6693, -33.4489)
    
    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            zoom(11.5)
            center(santiagoCenter)
        }
    }
    
    Scaffold(
        bottomBar = {
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
        },
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
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            MapboxMap(
                modifier = Modifier.fillMaxSize(),
                mapViewportState = mapViewportState
            )
            
            // Mostrar información de las tiendas en los logs
            // Nota: Los marcadores visuales requerirían acceso a la API de anotaciones de Mapbox
            // que puede variar según la versión. Por ahora, el mapa muestra las ubicaciones
            // y las tiendas están definidas en el código.
            LaunchedEffect(stores) {
                if (stores.isNotEmpty()) {
                    android.util.Log.d("StoresMap", "Tiendas en Santiago: ${stores.size}")
                    stores.forEach { store ->
                        android.util.Log.d(
                            "StoresMap",
                            "${store.name} - ${store.address} - ${store.location.latitude()}, ${store.location.longitude()}"
                        )
                    }
                }
            }
        }
    }
}
