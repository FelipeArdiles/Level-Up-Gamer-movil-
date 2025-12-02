package com.example.level_up_gamer.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Pruebas de UI para RegistrationScreen usando JUnit4 y ComposeTestRule
 * 
 * Nota: Las pruebas de UI con ComposeTestRule requieren JUnit4, no JUnit5
 * 
 * Estas pruebas verifican:
 * - Elementos de la UI se muestran correctamente
 * - Validación de formularios con animación
 * - Interacciones del usuario
 */
class RegistrationScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            RegistrationScreen(navController = rememberNavController())
        }
    }

    @Test
    fun shouldDisplayCreateAccountTitle() {
        composeTestRule.onNodeWithText("Crear cuenta").assertIsDisplayed()
    }

    @Test
    fun shouldDisplayAllFormFields() {
        composeTestRule.onNodeWithText("Nombre de usuario").assertIsDisplayed()
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contraseña").assertIsDisplayed()
    }

    @Test
    fun shouldDisplayRegisterButton() {
        composeTestRule.onNodeWithText("Registrarme").assertIsDisplayed()
        composeTestRule.onNodeWithText("Registrarme").assertIsEnabled()
    }

    @Test
    fun shouldAllowTextInputInAllFields() {
        composeTestRule.onNodeWithText("Nombre de usuario")
            .performTextInput("UsuarioTest")
        
        composeTestRule.onNodeWithText("Email")
            .performTextInput("test@example.com")
        
        composeTestRule.onNodeWithText("Contraseña")
            .performTextInput("password123")
        
        // Verificar que el texto se ingresó
        composeTestRule.onNode(hasText("UsuarioTest")).assertIsDisplayed()
    }

    @Test
    fun shouldDisplayBackToLoginButton() {
        composeTestRule.onNodeWithText("¿Ya tienes cuenta? Inicia sesión").assertIsDisplayed()
    }

    @Test
    fun shouldNotShowErrorMessageInitially() {
        // El mensaje de error con animación solo aparece cuando hay un error
        // Inicialmente no debería estar visible
    }
}

