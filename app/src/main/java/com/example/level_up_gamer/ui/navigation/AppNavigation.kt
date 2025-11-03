// ui/navigation/AppNavigation.kt

package com.example.level_up_gamer.ui.navigation

import com.example.level_up_gamer.ui.screens.LoginScreen
import com.example.level_up_gamer.ui.screens.ProductMenuScreen
import com.example.level_up_gamer.ui.screens.ProfileScreen
import com.example.level_up_gamer.ui.screens.RegistrationScreen
import com.example.level_up_gamer.ui.screens.CartScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

sealed class Screen(val route: String) {
    object Login : Screen("login_screen")
    object ProductMenu : Screen("product_menu_screen")
    object UserProfile : Screen("user_profile_screen")
    object Register : Screen("register_screen")
    object Cart : Screen("cart_screen")
    // Aquí puedes añadir rutas con argumentos, ej: object ProductDetail : Screen("product_detail/{id}")
}

@Composable
fun AppNavigation() {
    // NavController es el objeto que maneja el estado de la navegación
    val navController = rememberNavController()

    // NavHost enlaza el NavController con las pantallas (Composable)
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route // Pantalla inicial
    ) {
        // 1. Pantalla de Login/Registro
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }

        // Registro de Usuario
        composable(Screen.Register.route) {
            RegistrationScreen(navController = navController)
        }

        // 2. Menú de Productos
        composable(Screen.ProductMenu.route) {
            ProductMenuScreen(navController = navController)
        }

        // 3. Perfil de Usuario
        composable(Screen.UserProfile.route) {
            ProfileScreen(navController = navController)
        }

        // 4. Carrito de Compras
        composable(Screen.Cart.route) {
            CartScreen(navController = navController)
        }
    }
}