package com.example.booking.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.booking.view.LoginRegistrationScreen
import com.example.booking.view.RegistrationScreen


sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Registration : Screen("registration")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginRegistrationScreen(
                onRegisterClick = {
                    navController.navigate(Screen.Registration.route)
                }
            )
        }

        composable(Screen.Registration.route) {
            RegistrationScreen(
                onNextClick = {

                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // другие экраны здесь
    }
}