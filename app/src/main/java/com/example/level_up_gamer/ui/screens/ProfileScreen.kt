// ui/screens/ProfileScreen.kt

package com.example.level_up_gamer.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.level_up_gamer.ui.navigation.Screen
import com.example.level_up_gamer.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel() // 💡 Inyección de ViewModel
) {
    // 💡 Observa el estado del perfil del usuario
    val user by userViewModel.userProfile.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil de Jugador") },
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
            // Separar elementos verticalmente (Guía 8)
            verticalArrangement = Arrangement.Top
        ) {
            if (user == null) {
                // Estado de Carga
                CircularProgressIndicator(modifier = Modifier.padding(top = 50.dp))
                Text("Cargando perfil...")
            } else {
                // 1. Cabecera del Perfil
                ProfileHeader(user?.username ?: "Usuario") // Muestra el nombre
                Spacer(modifier = Modifier.height(32.dp))

                // 2. Tarjeta con Información
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        ProfileInfoItem(label = "Nombre de Usuario:", value = user!!.username)
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        ProfileInfoItem(label = "Email de Acceso:", value = user!!.email)
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        ProfileInfoItem(label = "ID de Jugador:", value = user!!.id)
                    }
                }
                Spacer(modifier = Modifier.height(48.dp))

                // 3. Botón de Cerrar Sesión
                Button(
                    onClick = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) // Borra todas las pantallas para cerrar sesión
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cerrar Sesión")
                }
            }
        }
    }
}

@Composable
fun ProfileHeader(username: String) {
    // Icono grande del perfil (usando un icono predeterminado por simplicidad)
    Icon(
        imageVector = Icons.Filled.Person,
        contentDescription = "Icono de Perfil",
        modifier = Modifier.size(80.dp),
        tint = MaterialTheme.colorScheme.primary
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = "¡Hola, $username!",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun ProfileInfoItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween // Alinea a los lados
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}