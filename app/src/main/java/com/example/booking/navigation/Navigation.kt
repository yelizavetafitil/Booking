package com.example.booking.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.booking.view.EnterpriseRegistrationScreen
import com.example.booking.view.LoginRegistrationScreen
import com.example.booking.view.RegistrationScreen
import com.example.booking.viewmodel.EnterpriseRegistrationViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Registration : Screen("registration")
    object Enterprise : Screen("enterpriseRegistration/{userId}")
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
                onNextClick = { userId ->
                    navController.navigate("enterpriseRegistration/$userId")
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Enterprise.route) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull()
            EnterpriseRegistrationScreen(
                userId = userId,
                onNextClick = { userId, enterpriseId ->

                },
                onSkipClick={ userId ->

                },
                onBackClick = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }
    }
}