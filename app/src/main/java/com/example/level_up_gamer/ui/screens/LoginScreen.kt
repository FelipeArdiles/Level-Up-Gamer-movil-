// ui/screens/LoginScreen.kt

package com.example.level_up_gamer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.level_up_gamer.ui.navigation.Screen
import com.example.level_up_gamer.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel() // 💡 Inyección de ViewModel
) {
    // Observa el estado del ViewModel (patrón MVVM)
    val uiState by authViewModel.uiState.collectAsState()

    // 💡 Efecto secundario: Reacciona cuando la autenticación es exitosa
    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) {
            navController.navigate(Screen.ProductMenu.route) {
                // Evita volver al login al presionar 'Atrás'
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Level Up Gamer - Login") }) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("¡Bienvenido/a!", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(32.dp))

            // Campo de Email
            OutlinedTextField(
                value = uiState.email, // ⬅️ Atado al estado del ViewModel
                onValueChange = { authViewModel.onEmailChange(it) }, // ⬅️ Llama al evento del ViewModel
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading // Deshabilitado durante la carga
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Contraseña
            OutlinedTextField(
                value = uiState.password, // ⬅️ Atado al estado del ViewModel
                onValueChange = { authViewModel.onPasswordChange(it) }, // ⬅️ Llama al evento del ViewModel
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(), // Oculta el texto
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Mensaje de Error
            uiState.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Botón de Iniciar Sesión
            Button(
                onClick = { authViewModel.login() }, // ⬅️ Llama a la función de Login
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading // Deshabilitado mientras carga
            ) {
                if (uiState.isLoading) {
                    // Muestra un indicador de carga mientras se espera la respuesta
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Iniciar Sesión")
                }
            }
        }
    }
}