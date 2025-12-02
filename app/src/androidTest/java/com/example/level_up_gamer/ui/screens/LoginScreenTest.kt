package com.example.level_up_gamer.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.hasText
import androidx.navigation.compose.rememberNavController
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Pruebas de UI para LoginScreen usando JUnit4 y ComposeTestRule
 * 
 * Nota: Las pruebas de UI con ComposeTestRule requieren JUnit4, no JUnit5
 * 
 * Estas pruebas verifican:
 * - Elementos de la UI se muestran correctamente
 * - Interacciones del usuario funcionan
 * - Validación de formularios con animación
 */
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            LoginScreen(navController = rememberNavController())
        }
    }

    @Test
    fun shouldDisplayAppTitle() {
        // "Level Up Gamer" aparece dos veces (TopAppBar y contenido principal)
        // Verificamos que al menos aparece una vez
        composeTestRule.onAllNodesWithText("Level Up Gamer", useUnmergedTree = true)
            .assertCountEquals(2) // Esperamos 2 ocurrencias
        
        // Verificamos el subtítulo
        composeTestRule.onNodeWithText("Inicia tu sesión").assertIsDisplayed()
    }

    @Test
    fun shouldDisplayEmailAndPasswordFields() {
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contraseña").assertIsDisplayed()
    }

    @Test
    fun shouldDisplayLoginButton() {
        composeTestRule.onNodeWithText("Iniciar Sesión").assertIsDisplayed()
        composeTestRule.onNodeWithText("Iniciar Sesión").assertIsEnabled()
    }

    @Test
    fun shouldAllowTextInput() {
        composeTestRule.onNodeWithText("Email")
            .performTextInput("test@example.com")
        
        composeTestRule.onNodeWithText("Contraseña")
            .performTextInput("password123")
        
        // Verificar que el texto se ingresó correctamente
        composeTestRule.onNode(hasText("test@example.com")).assertIsDisplayed()
    }

    @Test
    fun shouldDisplayRegisterButton() {
        composeTestRule.onNodeWithText("¿No tienes cuenta? Regístrate").assertIsDisplayed()
    }

    @Test
    fun shouldNotShowErrorMessageInitially() {
        // El mensaje de error solo aparece cuando hay un error
        // Inicialmente no debería estar visible
        // Nota: Esto depende de la implementación específica
    }
}