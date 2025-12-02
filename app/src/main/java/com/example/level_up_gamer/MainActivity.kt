// MainActivity.kt
package com.example.level_up_gamer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.level_up_gamer.data.DatabaseProvider
import com.example.level_up_gamer.ui.navigation.AppNavigation
// La importación también debe cambiar
import com.example.level_up_gamer.ui.theme.Theme
import com.mapbox.common.MapboxOptions

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Configurar el token de Mapbox antes de renderizar cualquier contenido
        MapboxOptions.accessToken = getString(R.string.mapbox_access_token)
        
        setContent {
            // Usa el nombre correcto del tema aquí
            Theme {
                AppNavigation()
            }
        }
    }
}
    