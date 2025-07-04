package com.example.booking.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.booking.view.EnterpriseAddScreen
import com.example.booking.view.EnterpriseEditScreen
import com.example.booking.view.EnterpriseRegistrationScreen
import com.example.booking.view.EnterpriseSelectionScreen
import com.example.booking.view.EntryScreen
import com.example.booking.view.LoginRegistrationScreen
import com.example.booking.view.ProfileEditScreen
import com.example.booking.view.ProfileEnterpriseSelectionScreen
import com.example.booking.view.ProfileScreen
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
    object Profile : Screen("profile/{userId}&{enterpriseId}") {
        fun createRoute(userId: Int, enterpriseId: Int) = "profile/$userId&$enterpriseId"
    }
    object ProfileEdit : Screen("profileEdit/{userId}&{enterpriseId}") {
        fun createRoute(userId: Int, enterpriseId: Int) = "profileEdit/$userId&$enterpriseId"
    }
    object ProfileEnterprises : Screen("profileEnterprises/{userId}&{enterpriseId}") {
        fun createRoute(userId: Int, enterpriseId: Int) = "profileEnterprises/$userId&$enterpriseId"
    }
    object EnterpriseEdit : Screen("editEnterprise/{userId}&{enterpriseId}") {
        fun createRoute(userId: Int, enterpriseId: Int) = "editEnterprise/$userId&$enterpriseId"
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
                    userId?.let {
                        enterpriseId?.let {
                            navController.navigate(Screen.Profile.createRoute(userId, enterpriseId))
                        }
                    }
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
                    userId?.let {
                        enterpriseId?.let {
                            navController.navigate(Screen.Profile.createRoute(userId, enterpriseId))
                        }
                    }
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

        composable(
            route = Screen.Profile.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType },
                navArgument("enterpriseId") { type = NavType.IntType }
                )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId")
            val enterpriseId = backStackEntry.arguments?.getInt("enterpriseId")
            ProfileScreen(
                userId = userId,
                enterpriseId = enterpriseId,
                onProfileClick = { userId, enterpriseId ->
                    userId?.let {
                        enterpriseId?.let {
                            navController.navigate(Screen.ProfileEdit.createRoute(userId, enterpriseId))
                        }
                    }
                },
                onEnterprisesClick = { userId, enterpriseId ->
                    userId?.let {
                        enterpriseId?.let {
                            navController.navigate(Screen.ProfileEnterprises.createRoute(userId, enterpriseId))
                        }
                    }
                },
                onLogoutClick = {
                    navController.navigate(Screen.Login.route)
                },
                onManagementClick = {userId, enterpriseId ->

                },
                onCalendarClick = {userId, enterpriseId ->

                }
            )
        }

        composable(
            route = Screen.ProfileEdit.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType },
                navArgument("enterpriseId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId")
            val enterpriseId = backStackEntry.arguments?.getInt("enterpriseId")
            ProfileEditScreen(
                userId = userId,
                enterpriseId = enterpriseId,
                onSaveClick = { updatedUserId, updatedEnterpriseId ->
                    if (updatedUserId != null && updatedEnterpriseId != null) {
                        navController.navigate(
                            Screen.Profile.createRoute(updatedUserId, updatedEnterpriseId)
                        ) {
                            popUpTo(Screen.Profile.route) {
                                inclusive = true
                            }
                        }
                    } else {
                        Log.e("Navigation", "ID is null")
                    }
                },
                onBackClick = { userId, enterpriseId ->
                    userId?.let {
                        enterpriseId?.let {
                            navController.navigate(Screen.Profile.createRoute(userId, enterpriseId))
                        }
                    }
                }
            )
        }

        composable(
            route = Screen.ProfileEnterprises.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType },
                navArgument("enterpriseId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId")
            val enterpriseId = backStackEntry.arguments?.getInt("enterpriseId")
            ProfileEnterpriseSelectionScreen(
                userId = userId,
                enterpriseId = enterpriseId,
                onEnterpriseSelected = { userId, enterpriseId ->
                    userId?.let {
                        enterpriseId?.let {
                            navController.navigate(Screen.Profile.createRoute(userId, enterpriseId))
                        }
                    }
                },
                onEditEnterprise = {serId, enterpriseId ->
                    userId?.let {
                        enterpriseId?.let {
                            navController.navigate(Screen.EnterpriseEdit.createRoute(userId, enterpriseId))
                        }
                    }
                },
                onBackClick = {serId, enterpriseId ->
                    userId?.let {
                        enterpriseId?.let {
                            navController.navigate(Screen.Profile.createRoute(userId, enterpriseId))
                        }
                    }
                }
            )
        }

        composable(
            route = Screen.EnterpriseEdit.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType },
                navArgument("enterpriseId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId")
            val enterpriseId = backStackEntry.arguments?.getInt("enterpriseId")
            EnterpriseEditScreen(
                userId = userId,
                enterpriseId = enterpriseId,
                onSaveClick = { updatedUserId, updatedEnterpriseId ->
                    if (updatedUserId != null && updatedEnterpriseId != null) {
                        navController.navigate(
                            Screen.Profile.createRoute(updatedUserId, updatedEnterpriseId)
                        ) {
                            popUpTo(Screen.Profile.route) {
                                inclusive = true
                            }
                        }
                    } else {
                        Log.e("Navigation", "ID is null")
                    }
                },
                onDeleteClick = { userId ->
                    userId?.let {
                        navController.navigate(Screen.EnterpriseSelect.createRoute(it))
                    }
                },
                onBackClick = { userId, enterpriseId ->
                    userId?.let {
                        enterpriseId?.let {
                            navController.navigate(Screen.Profile.createRoute(userId, enterpriseId))
                        }
                    }
                }
            )
        }
    }
}