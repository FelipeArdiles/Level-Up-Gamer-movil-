package com.example.level_up_gamer.ui.screens

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
import androidx.compose.foundation.Image // 💡 Importación para el Logo
import androidx.compose.ui.res.painterResource // 💡 Importación para el Logo
import com.example.level_up_gamer.R // 💡 Importación para R.drawable

// 💡 Anotación necesaria para el TopAppBar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel()
) {
    val user by userViewModel.userProfile.collectAsState()

    Scaffold(
        topBar = {
            // ✅ INICIO DE SECCIÓN MODIFICADA (TopAppBar con Logo)
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { 
                                navController.navigate(Screen.ProductMenu.route) {
                                    popUpTo(Screen.ProductMenu.route) { inclusive = false }
                                }
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.level_up_logo),
                                contentDescription = "Logo Level Up Gamer",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Mi Perfil de Jugador")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
            // ✅ FIN DE SECCIÓN MODIFICADA
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (user == null) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 50.dp))
                Text("Cargando perfil...")
            } else {
                ProfileHeader(user?.username ?: "Usuario")
                Spacer(modifier = Modifier.height(32.dp))

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
                Spacer(modifier = Modifier.height(32.dp))

                OutlinedButton(
                    onClick = { navController.navigate(Screen.EditProfile.route) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Editar Perfil")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0)
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

// Componente ProfileHeader (Sin cambios)
@Composable
fun ProfileHeader(username: String) {
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

// Componente ProfileInfoItem (Sin cambios)
@Composable
fun ProfileInfoItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
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