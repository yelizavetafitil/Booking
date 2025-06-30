package com.example.booking.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.booking.view.EnterpriseAddScreen
import com.example.booking.view.EnterpriseRegistrationScreen
import com.example.booking.view.EnterpriseSelectionScreen
import com.example.booking.view.EntryScreen
import com.example.booking.view.LoginRegistrationScreen
import com.example.booking.view.RegistrationScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Entry : Screen("entry")
    object EnterpriseSelect : Screen("entrySelect/{userId}") {
        fun createRoute(userId: Int) = "entrySelect/$userId"
    }
    object Registration : Screen("registration")
    object EnterpriseRegistration : Screen("enterpriseRegistration/{userId}") {
        fun createRoute(userId: Int) = "enterpriseRegistration/$userId"
    }
    object EnterpriseAdd : Screen("enterpriseAdd/{userId}") {
        fun createRoute(userId: Int) = "enterpriseAdd/$userId"
    }
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
                onEntryClick = {
                    navController.navigate(Screen.Entry.route)
                },
                onRegisterClick = {
                    navController.navigate(Screen.Registration.route)
                }
            )
        }

        composable(Screen.Registration.route) {
            RegistrationScreen(
                onNextClick = { userId ->
                    userId?.let {
                        navController.navigate(Screen.EnterpriseRegistration.createRoute(it))
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.EnterpriseRegistration.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId")
            EnterpriseRegistrationScreen(
                userId = userId,
                onNextClick = { userId, enterpriseId ->

                },
                onSkipClick = { userId ->
                    userId?.let {
                        navController.navigate(Screen.EnterpriseSelect.createRoute(it))
                    }
                },
                onBackClick = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        composable(Screen.Entry.route) {
            EntryScreen(
                onNextClick = { userId ->
                    userId?.let {
                        navController.navigate(Screen.EnterpriseSelect.createRoute(it))
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.EnterpriseSelect.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId")
            EnterpriseSelectionScreen(
                userId = userId,
                onEnterpriseSelected = { userId, enterpriseId ->

                },
                onAddEnterprise = { userId ->
                    userId?.let {
                        navController.navigate(Screen.EnterpriseAdd.createRoute(it))
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.EnterpriseAdd.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId")
            EnterpriseAddScreen(
                userId = userId,
                onNextClick = { userId ->
                    userId?.let {
                        navController.navigate(Screen.EnterpriseSelect.createRoute(it))
                    }
                },
                onBackClick = { userId ->
                    userId?.let {
                        navController.navigate(Screen.EnterpriseSelect.createRoute(it))
                    }
                },
            )
        }
    }
}