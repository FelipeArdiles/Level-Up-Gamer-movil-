package com.example.level_up_gamer.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun LoginScreen_showsTitle() {
        composeTestRule.setContent {
            LoginScreen(navController = rememberNavController())
        }
        composeTestRule.onNodeWithText("Level Up Gamer - Login").assertIsDisplayed()
    }
}