    package com.example.booking.navigation

    import android.os.Build
    import android.util.Log
    import androidx.annotation.RequiresApi
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.saveable.rememberSaveable
    import androidx.compose.runtime.setValue
    import androidx.lifecycle.viewmodel.compose.viewModel
    import androidx.navigation.NavType
    import androidx.navigation.compose.NavHost
    import androidx.navigation.compose.composable
    import androidx.navigation.compose.rememberNavController
    import androidx.navigation.navArgument
    import com.example.booking.view.AccessEditSelectionScreen
    import com.example.booking.view.AccessSelectionScreen
    import com.example.booking.view.ChartChoiceHoursScreen
    import com.example.booking.view.ChartChoiceScreen
    import com.example.booking.view.ChartChoiceSetUpScreen
    import com.example.booking.view.ChartEmployeeSelectScreen
    import com.example.booking.view.ChartEmployeeSetUpScreen
    import com.example.booking.view.EmployeeAddScreen
    import com.example.booking.view.EmployeeEditScreen
    import com.example.booking.view.EmployeeScreen
    import com.example.booking.view.EmployeeSelectScreen
    import com.example.booking.view.EnterpriseAddScreen
    import com.example.booking.view.EnterpriseEditScreen
    import com.example.booking.view.EnterpriseRegistrationScreen
    import com.example.booking.view.EnterpriseSelectionScreen
    import com.example.booking.view.EntryScreen
    import com.example.booking.view.LoginRegistrationScreen
    import com.example.booking.view.ManagementScreen
    import com.example.booking.view.ProfileEditScreen
    import com.example.booking.view.ProfileEnterpriseSelectionScreen
    import com.example.booking.view.ProfileScreen
    import com.example.booking.view.RegistrationScreen
    import com.example.booking.view.ScheduleScreen
    import com.example.booking.view.ServiceAddEmployeeScreen
    import com.example.booking.view.ServiceAddScreen
    import com.example.booking.view.ServiceEditEmployeeScreen
    import com.example.booking.view.ServiceEditScreen
    import com.example.booking.view.ServiceScreen
    import com.example.booking.view.WorkingHoursScreen
    import com.example.booking.view.WorkingWeeksHoursScreen
    import com.example.booking.view.WorkingWeeksHoursSetUpScreen
    import com.example.booking.view.WorkingWeeksHoursSetUpScreenFinish
    import com.example.booking.viewmodel.EmployeeViewModel

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
        object Management : Screen("management/{userId}&{enterpriseId}") {
            fun createRoute(userId: Int, enterpriseId: Int) = "management/$userId&$enterpriseId"
        }
        object Service : Screen("service/{userId}&{enterpriseId}") {
            fun createRoute(userId: Int, enterpriseId: Int) = "service/$userId&$enterpriseId"
        }
        object ServiceAdd : Screen("serviceAdd/{userId}&{enterpriseId}") {
            fun createRoute(userId: Int, enterpriseId: Int) = "serviceAdd/$userId&$enterpriseId"
        }
        object ServiceEdit : Screen("serviceEdit/{userId}&{enterpriseId}&{serviceId}") {
            fun createRoute(userId: Int, enterpriseId: Int, serviceId: Int) = "serviceEdit/$userId&$enterpriseId&$serviceId"
        }
        object Employee : Screen("employee/{userId}&{enterpriseId}") {
            fun createRoute(userId: Int, enterpriseId: Int) = "employee/$userId&$enterpriseId"
        }
        object EmployeeSelect : Screen("employeeSelect/{userId}&{enterpriseId}") {
            fun createRoute(userId: Int, enterpriseId: Int) = "employeeSelect/$userId&$enterpriseId"
        }
        object EmployeeAdd : Screen("employeeAdd/{userId}&{enterpriseId}") {
            fun createRoute(userId: Int, enterpriseId: Int) = "employeeAdd/$userId&$enterpriseId"
        }
        object AccessSelection : Screen("accessSelection/{userId}&{enterpriseId}") {
            fun createRoute(userId: Int, enterpriseId: Int) = "accessSelection/$userId&$enterpriseId"
        }
        object EmployeeEdit : Screen("employeeEdit/{userId}&{enterpriseId}&{employeeId}") {
            fun createRoute(userId: Int, enterpriseId: Int, employeeId: Int) = "employeeEdit/$userId&$enterpriseId&$employeeId"
        }
        object AccessEditSelection : Screen("accessEditSelection/{userId}&{enterpriseId}&{employeeId}") {
            fun createRoute(userId: Int, enterpriseId: Int, employeeId: Int) = "accessEditSelection/$userId&$enterpriseId&$employeeId"
        }
        object ServiceAddEmployee : Screen("ServiceAddEmployee/{userId}&{enterpriseId}&{serviceId}") {
            fun createRoute(userId: Int, enterpriseId: Int, serviceId: Int) = "ServiceAddEmployee/$userId&$enterpriseId&$serviceId"
        }
        object ServiceEditEmployee : Screen("ServiceEditEmployee/{userId}&{enterpriseId}&{serviceId}") {
            fun createRoute(userId: Int, enterpriseId: Int, serviceId: Int) = "ServiceEditEmployee/$userId&$enterpriseId&$serviceId"
        }
        object ChartEmployeeSelect : Screen("ChartEmployeeSelect/{userId}&{enterpriseId}") {
            fun createRoute(userId: Int, enterpriseId: Int) = "ChartEmployeeSelect/$userId&$enterpriseId"
        }
        object ChartEmployeeSetUp : Screen("ChartEmployeeSetUp/{userId}&{enterpriseId}&{employeeId}") {
            fun createRoute(userId: Int, enterpriseId: Int, employeeId: Int) = "ChartEmployeeSetUp/$userId&$enterpriseId&$employeeId"
        }
        object WorkingHours : Screen("WorkingHours/{userId}&{enterpriseId}&{employeeId}&{level}") {
            fun createRoute(userId: Int, enterpriseId: Int, employeeId: Int, level: String) = "WorkingHours/$userId&$enterpriseId&$employeeId&$level"
        }
        object WorkingWeeksHours : Screen("WorkingWeeksHours/{userId}&{enterpriseId}&{employeeId}&{level}") {
            fun createRoute(userId: Int, enterpriseId: Int, employeeId: Int, level: String) = "WorkingWeeksHours/$userId&$enterpriseId&$employeeId&$level"
        }
        object WorkingWeeksHoursSetUp : Screen("WorkingWeeksHoursSetUp/{userId}&{enterpriseId}&{employeeId}&{level}&{dayOfWeek}") {
            fun createRoute(userId: Int, enterpriseId: Int, employeeId: Int, level: String, dayOfWeek: String) = "WorkingWeeksHoursSetUp/$userId&$enterpriseId&$employeeId&$level&$dayOfWeek"
        }
        object WorkingWeeksHoursSetUpFinish : Screen("WorkingWeeksHoursSetUpFinish/{userId}&{enterpriseId}&{employeeId}&{level}&{dayOfWeek}&{subType}") {
            fun createRoute(userId: Int, enterpriseId: Int, employeeId: Int, level: String, dayOfWeek: String, subType: String) = "WorkingWeeksHoursSetUpFinish/$userId&$enterpriseId&$employeeId&$level&$dayOfWeek&$subType"
        }
        object ChartChoice : Screen("ChartChoice/{userId}&{enterpriseId}&{employeeId}&{level}") {
            fun createRoute(userId: Int, enterpriseId: Int, employeeId: Int, level: String) = "ChartChoice/$userId&$enterpriseId&$employeeId&$level"
        }
        object ChartChoiceSetUp : Screen("ChartChoiceSetUp/{userId}&{enterpriseId}&{employeeId}&{level}&{dayWork}&{dayRest}") {
            fun createRoute(userId: Int, enterpriseId: Int, employeeId: Int, level: String, dayWork: String, dayRest: String) = "ChartChoiceSetUp/$userId&$enterpriseId&$employeeId&$level&$dayWork&$dayRest"
        }
        object ChartChoiceHours : Screen("ChartChoiceHours/{userId}&{enterpriseId}&{employeeId}&{level}&{dayWork}&{dayRest}&{subType}") {
            fun createRoute(userId: Int, enterpriseId: Int, employeeId: Int, level: String, dayWork: String, dayRest: String, subType: String) = "ChartChoiceHours/$userId&$enterpriseId&$employeeId&$level&$dayWork&$dayRest&$subType"
        }
        object Schedule : Screen("Schedule/{userId}&{enterpriseId}") {
            fun createRoute(userId: Int, enterpriseId: Int) = "Schedule/$userId&$enterpriseId"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun AppNavigation() {
        val navController = rememberNavController()
        val employeeViewModel: EmployeeViewModel = viewModel()

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
                        userId?.let {
                            enterpriseId?.let {
                                navController.navigate(Screen.Management.createRoute(userId, enterpriseId))
                            }
                        }
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

            composable(
                route = Screen.Management.route,
                arguments = listOf(navArgument("userId") { type = NavType.IntType },
                    navArgument("enterpriseId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId")
                val enterpriseId = backStackEntry.arguments?.getInt("enterpriseId")
                ManagementScreen(
                    userId = userId,
                    enterpriseId = enterpriseId,
                    onProfileClick = { userId, enterpriseId ->
                        userId?.let {
                            enterpriseId?.let {
                                navController.navigate(Screen.Profile.createRoute(userId, enterpriseId))
                            }
                        }
                    },
                    onCalendarClick = { userId, enterpriseId ->

                    },
                    onServiceClick = {userId, enterpriseId ->
                        userId?.let {
                            enterpriseId?.let {
                                navController.navigate(Screen.Service.createRoute(userId, enterpriseId))
                            }
                        }
                    },
                    onEmployeeClick = {userId, enterpriseId ->
                        userId?.let {
                            enterpriseId?.let {
                                navController.navigate(Screen.Employee.createRoute(userId, enterpriseId))
                            }
                        }

                    },
                    onAnalyticsClick = {userId, enterpriseId ->

                    }
                )
            }

            composable(
                route = Screen.Service.route,
                arguments = listOf(navArgument("userId") { type = NavType.IntType },
                    navArgument("enterpriseId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId")
                val enterpriseId = backStackEntry.arguments?.getInt("enterpriseId")
                ServiceScreen(
                    userId = userId,
                    enterpriseId = enterpriseId,
                    onEditService = { userId, enterpriseId, serviceId ->
                        userId?.let {
                            enterpriseId?.let {
                                serviceId?.let {
                                    navController.navigate(Screen.ServiceEdit.createRoute(userId, enterpriseId, serviceId))
                                }
                            }
                        }
                    },
                    onAddService = {userId, enterpriseId ->
                        userId?.let {
                            enterpriseId?.let {
                                navController.navigate(Screen.ServiceAdd.createRoute(userId, enterpriseId))
                            }
                        }
                    },
                    onBackClick = {userId, enterpriseId ->
                        userId?.let {
                            enterpriseId?.let {
                                navController.navigate(Screen.Management.createRoute(userId, enterpriseId))
                            }
                        }
                    }
                )
            }

            composable(
                route = Screen.ServiceAdd.route,
                arguments = listOf(navArgument("userId") { type = NavType.IntType },
                    navArgument("enterpriseId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId")
                val enterpriseId = backStackEntry.arguments?.getInt("enterpriseId")
                ServiceAddScreen(
                    userId = userId,
                    enterpriseId = enterpriseId,
                    onSaveClick = {userId, enterpriseId, serviceId ->
                        userId?.let {
                            enterpriseId?.let {
                                serviceId?.let {
                                    navController.navigate(
                                        Screen.ServiceAddEmployee.createRoute(userId, enterpriseId, serviceId)
                                    )
                                }
                            }
                        }
                    },
                    onBackClick = {userId, enterpriseId ->
                        userId?.let {
                            enterpriseId?.let {
                                navController.navigate(Screen.Service.createRoute(userId, enterpriseId))
                            }
                        }
                    }
                )
            }

            composable(
                route = Screen.ServiceEdit.route,
                arguments = listOf(navArgument("userId") { type = NavType.IntType },
                    navArgument("enterpriseId") { type = NavType.IntType },
                    navArgument("serviceId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId")
                val enterpriseId = backStackEntry.arguments?.getInt("enterpriseId")
                val serviceId = backStackEntry.arguments?.getInt("serviceId")
                ServiceEditScreen (
                    userId = userId,
                    enterpriseId = enterpriseId,
                    serviceId = serviceId,
                    onSaveClick = {userId, enterpriseId, serviceId ->
                        userId?.let {
                            enterpriseId?.let {
                                serviceId?.let { navController.navigate(Screen.ServiceEditEmployee.createRoute(userId, enterpriseId, serviceId))
                                }
                            }
                        }
                    },
                    onDeleteClick = {userId, enterpriseId ->
                        userId?.let {
                            enterpriseId?.let {
                                navController.navigate(Screen.Service.createRoute(userId, enterpriseId))
                            }
                        }
                    },
                    onBackClick = {userId, enterpriseId ->
                        userId?.let {
                            enterpriseId?.let {
                                navController.navigate(Screen.Service.createRoute(userId, enterpriseId))
                            }
                        }
                    }
                )
            }

            composable(
                route = Screen.Employee.route,
                arguments = listOf(navArgument("userId") { type = NavType.IntType },
                    navArgument("enterpriseId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId")
                val enterpriseId = backStackEntry.arguments?.getInt("enterpriseId")
                EmployeeScreen(
                    userId = userId,
                    enterpriseId = enterpriseId,
                    onEmployeeClick = {userId, enterpriseId ->
                        userId?.let {
                            enterpriseId?.let {
                                navController.navigate(Screen.EmployeeSelect.createRoute(userId, enterpriseId))
                            }
                        }
                    },
                    onChartClick = {userId, enterpriseId ->
                        userId?.let {
                            enterpriseId?.let {
                                navController.navigate(Screen.Schedule.createRoute(userId, enterpriseId))
                            }
                        }
                    },
                    onBackClick = {userId, enterpriseId ->
                        userId?.let {
                            enterpriseId?.let {
                                navController.navigate(Screen.Management.createRoute(userId, enterpriseId))
                            }
                        }
                    }
                )
            }

            composable(
                route = Screen.EmployeeSelect.route,
                arguments = listOf(navArgument("userId") { type = NavType.IntType },
                    navArgument("enterpriseId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId")
                val enterpriseId = backStackEntry.arguments?.getInt("enterpriseId")
                EmployeeSelectScreen(
                    userId = userId,
                    enterpriseId = enterpriseId,
                    onEditEmployee = {userId, enterpriseId, employeeId ->
                        employeeViewModel.resetForm()
                        userId?.let {
                            enterpriseId?.let {
                                employeeId?.let {
                                    navController.navigate(Screen.EmployeeEdit.createRoute(userId, enterpriseId, employeeId))
                                }
                            }
                        }
                    },
                    onAddEmployee = {userId, enterpriseId ->
                        userId?.let {
                            enterpriseId?.let {
                                navController.navigate(Screen.EmployeeAdd.createRoute(userId, enterpriseId))
                            }
                        }
                    },
                    onBackClick = {userId, enterpriseId ->
                        userId?.let {
                            enterpriseId?.let {
                                navController.navigate(Screen.Employee.createRoute(userId, enterpriseId))
                            }
                        }
                    }
                )
            }

            composable(
                route = Screen.EmployeeAdd.route,
                arguments = listOf(
                    navArgument("userId") { type = NavType.IntType },
                    navArgument("enterpriseId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId")
                val enterpriseId = backStackEntry.arguments?.getInt("enterpriseId")

                EmployeeAddScreen(
                    viewModel = employeeViewModel,
                    userId = userId,
                    enterpriseId = enterpriseId,
                    onBackClick = { userId, enterpriseId ->
                        employeeViewModel.resetForm()
                        userId?.let {
                            enterpriseId?.let {
                                navController.navigate(Screen.EmployeeSelect.createRoute(userId, enterpriseId))
                            }
                        }
                    },
                    onSaveClick = { userId, enterpriseId ->
                        employeeViewModel.resetForm()
                        userId?.let {
                            enterpriseId?.let {
                                navController.navigate(Screen.EmployeeSelect.createRoute(userId, enterpriseId))
                            }
                        }
                    },
                    onAccessClick = {
                        navController.navigate(Screen.AccessSelection.createRoute(
                            userId ?: return@EmployeeAddScreen,
                            enterpriseId ?: return@EmployeeAddScreen
                        ))
                    }
                )
            }

            composable(
                route = Screen.AccessSelection.route,
                arguments = listOf(
                    navArgument("userId") { type = NavType.IntType },
                    navArgument("enterpriseId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                AccessSelectionScreen(
                    viewModel = employeeViewModel,
                    onAccessSelected = { access ->
                        employeeViewModel.updateAccess(access)
                        navController.popBackStack()
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = Screen.EmployeeEdit.route,
                arguments = listOf(
                    navArgument("userId") { type = NavType.IntType },
                    navArgument("enterpriseId") { type = NavType.IntType },
                    navArgument("employeeId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId")
                val enterpriseId = backStackEntry.arguments?.getInt("enterpriseId")
                val employeeId = backStackEntry.arguments?.getInt("employeeId")

                var isInitialLoad by rememberSaveable  { mutableStateOf(true) }

                LaunchedEffect(employeeId) {
                    if (employeeId != null && isInitialLoad) {
                        println("данные загружены")
                        employeeViewModel.loadEmployee(
                            employeeId = employeeId,
                            onSuccess = { isInitialLoad = false }
                        )
                    }
                }

                EmployeeEditScreen(
                    viewModel = employeeViewModel,
                    userId = userId,
                    enterpriseId = enterpriseId,
                    employeeId = employeeId,
                    onBackClick = { userId, enterpriseId ->
                        employeeViewModel.resetForm()
                        userId?.let {
                            enterpriseId?.let {
                                navController.navigate(Screen.EmployeeSelect.createRoute(userId, enterpriseId))
                            }
                        }
                    },
                    onSaveClick = { userId, enterpriseId ->
                        employeeViewModel.resetForm()
                        userId?.let {
                            enterpriseId?.let {
                                navController.navigate(Screen.EmployeeSelect.createRoute(userId, enterpriseId))
                            }
                        }
                    },
                    onAccessClick = {
                        navController.navigate(Screen.AccessEditSelection.createRoute(
                            userId ?: return@EmployeeEditScreen,
                            enterpriseId ?: return@EmployeeEditScreen,
                            employeeId ?: return@EmployeeEditScreen
                        ))
                    },
                    onDeleteClick = { userId, enterpriseId ->
                        employeeViewModel.resetForm()
                        userId?.let {
                            enterpriseId?.let {
                                navController.navigate(Screen.EmployeeSelect.createRoute(userId, enterpriseId))
                            }
                        }
                    }
                )
            }

            composable(
                route = Screen.AccessEditSelection.route,
                arguments = listOf(
                    navArgument("userId") { type = NavType.IntType },
                    navArgument("enterpriseId") { type = NavType.IntType },
                    navArgument("employeeId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                AccessEditSelectionScreen(
                    viewModel = employeeViewModel,
                    initialAccess = employeeViewModel.currentAccess,
                    onAccessSelected = { access ->
                        employeeViewModel.updateAccess(access)
                        navController.popBackStack()
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = Screen.ServiceAddEmployee.route,
                arguments = listOf(
                    navArgument("userId") { type = NavType.IntType },
                    navArgument("enterpriseId") { type = NavType.IntType },
                    navArgument("serviceId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId")
                val enterpriseId = backStackEntry.arguments?.getInt("enterpriseId")
                val serviceId = backStackEntry.arguments?.getInt("serviceId")
                ServiceAddEmployeeScreen(
                    userId = userId,
                    enterpriseId = enterpriseId,
                    serviceId = serviceId,
                    onAddEmployee = { userId, enterpriseId ->
                        userId?.let {
                            enterpriseId?.let {
                                navController.navigate(Screen.Service.createRoute(userId, enterpriseId))
                            }
                        }
                    },
                    onBackClick = { userId, enterpriseId ->
                        userId?.let {
                            enterpriseId?.let {
                                navController.navigate(Screen.Service.createRoute(userId, enterpriseId))
                            }
                        }
                    }
                )
            }

            composable(
                route = Screen.ServiceEditEmployee.route,
                arguments = listOf(
                    navArgument("userId") { type = NavType.IntType },
                    navArgument("enterpriseId") { type = NavType.IntType },
                    navArgument("serviceId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId")
                val enterpriseId = backStackEntry.arguments?.getInt("enterpriseId")
                val serviceId = backStackEntry.arguments?.getInt("serviceId")
                ServiceEditEmployeeScreen(
                    userId = userId,
                    enterpriseId = enterpriseId,
                    serviceId = serviceId,
                    onSuccess = { userId, enterpriseId ->
                        userId?.let {
                            enterpriseId?.let {
                                navController.navigate(Screen.Service.createRoute(userId, enterpriseId))
                            }
                        }
                    },
                    onBackClick = { userId, enterpriseId ->
                        userId?.let {
                            enterpriseId?.let {
                                navController.navigate(Screen.Service.createRoute(userId, enterpriseId))
                            }
                        }
                    }
                )
            }

            composable(
                route = Screen.ChartEmployeeSelect.route,
                arguments = listOf(navArgument("userId") { type = NavType.IntType },
                    navArgument("enterpriseId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId")
                val enterpriseId = backStackEntry.arguments?.getInt("enterpriseId")
                ChartEmployeeSelectScreen(
                    userId = userId,
                    enterpriseId = enterpriseId,
                    onEditEmployee = {userId, enterpriseId, employeeId ->
                        userId?.let {
                            enterpriseId?.let {
                                employeeId?.let {
                                    navController.navigate(Screen.ChartEmployeeSetUp.createRoute(userId, enterpriseId, employeeId))
                                }
                            }
                        }
                    },
                    onBackClick = {userId, enterpriseId ->
                        userId?.let {
                            enterpriseId?.let {
                                navController.navigate(Screen.Schedule.createRoute(userId, enterpriseId))
                            }
                        }
                    }
                )
            }

            composable(
                route = Screen.ChartEmployeeSetUp.route,
                arguments = listOf(navArgument("userId") { type = NavType.IntType },
                    navArgument("enterpriseId") { type = NavType.IntType },
                    navArgument("employeeId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId")
                val enterpriseId = backStackEntry.arguments?.getInt("enterpriseId")
                val employeeId = backStackEntry.arguments?.getInt("employeeId")
                ChartEmployeeSetUpScreen(
                    userId = userId,
                    enterpriseId = enterpriseId,
                    employeeId = employeeId,
                    onNextClick = {userId, enterpriseId, employeeId, level ->
                        userId?.let {
                            enterpriseId?.let {
                                employeeId?.let {
                                    navController.navigate(Screen.WorkingHours.createRoute(userId, enterpriseId, employeeId, level ))
                                }
                            }
                        }
                    },
                    onNextWeekClick = {userId, enterpriseId, employeeId, level ->
                        userId?.let {
                            enterpriseId?.let {
                                employeeId?.let {
                                    navController.navigate(Screen.WorkingWeeksHours.createRoute(userId, enterpriseId, employeeId, level ))
                                }
                            }
                        }
                    },
                    onNextChartClick = {userId, enterpriseId, employeeId, level ->
                        userId?.let {
                            enterpriseId?.let {
                                employeeId?.let {
                                    navController.navigate(Screen.ChartChoice.createRoute(userId, enterpriseId, employeeId, level ))
                                }
                            }
                        }
                    },
                    onBackClick = {userId, enterpriseId ->
                        userId?.let {
                            enterpriseId?.let {
                                navController.navigate(Screen.ChartEmployeeSelect.createRoute(userId, enterpriseId))
                            }
                        }
                    }
                )
            }

            composable(
                route = Screen.WorkingHours.route,
                arguments = listOf(navArgument("userId") { type = NavType.IntType },
                    navArgument("enterpriseId") { type = NavType.IntType },
                    navArgument("employeeId") { type = NavType.IntType },
                    navArgument("level") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId")
                val enterpriseId = backStackEntry.arguments?.getInt("enterpriseId")
                val employeeId = backStackEntry.arguments?.getInt("employeeId")
                val level = backStackEntry.arguments?.getString("level")?: ""
                WorkingHoursScreen(
                    userId = userId,
                    enterpriseId = enterpriseId,
                    employeeId = employeeId,
                    level = level,
                    onSaveClick = {userId, enterpriseId ->
                        userId?.let {
                            enterpriseId?.let {
                                navController.navigate(Screen.ChartEmployeeSelect.createRoute(userId, enterpriseId))
                            }
                        }
                    },
                    onBackClick = {userId, enterpriseId, employeeId  ->
                        userId?.let {
                            enterpriseId?.let {
                                employeeId?.let {
                                    navController.navigate(Screen.ChartEmployeeSetUp.createRoute(userId, enterpriseId, employeeId))
                                }
                            }
                        }
                    }
                )
            }

            composable(
                route = Screen.WorkingWeeksHours.route,
                arguments = listOf(navArgument("userId") { type = NavType.IntType },
                    navArgument("enterpriseId") { type = NavType.IntType },
                    navArgument("employeeId") { type = NavType.IntType },
                    navArgument("level") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId")
                val enterpriseId = backStackEntry.arguments?.getInt("enterpriseId")
                val employeeId = backStackEntry.arguments?.getInt("employeeId")
                val level = backStackEntry.arguments?.getString("level")?: ""
                WorkingWeeksHoursScreen(
                    userId = userId,
                    enterpriseId = enterpriseId,
                    employeeId = employeeId,
                    level = level,
                    onSaveClick = {userId, enterpriseId, employeeId , level, dayOfWeek  ->
                        userId?.let {
                            enterpriseId?.let {
                                employeeId?.let {
                                    level?.let {
                                        dayOfWeek?.let {
                                            navController.navigate(Screen.WorkingWeeksHoursSetUp.createRoute(userId, enterpriseId, employeeId, level, dayOfWeek))
                                        }
                                    }
                                }
                            }
                        }
                    },
                    onBackClick = {userId, enterpriseId, employeeId  ->
                        userId?.let {
                            enterpriseId?.let {
                                employeeId?.let {
                                    navController.navigate(Screen.ChartEmployeeSetUp.createRoute(userId, enterpriseId, employeeId))
                                }
                            }
                        }
                    }
                )
            }

            composable(
                route = Screen.WorkingWeeksHoursSetUp.route,
                arguments = listOf(navArgument("userId") { type = NavType.IntType },
                    navArgument("enterpriseId") { type = NavType.IntType },
                    navArgument("employeeId") { type = NavType.IntType },
                    navArgument("level") { type = NavType.StringType },
                    navArgument("dayOfWeek") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId")
                val enterpriseId = backStackEntry.arguments?.getInt("enterpriseId")
                val employeeId = backStackEntry.arguments?.getInt("employeeId")
                val level = backStackEntry.arguments?.getString("level")?: ""
                val dayOfWeek = backStackEntry.arguments?.getString("dayOfWeek")?: ""
                WorkingWeeksHoursSetUpScreen(
                    userId = userId,
                    enterpriseId = enterpriseId,
                    employeeId = employeeId,
                    level = level,
                    dayOfWeek = dayOfWeek,
                    onSaveClick = {userId, enterpriseId, employeeId , level, dayOfWeek, subType  ->
                        userId?.let {
                            enterpriseId?.let {
                                employeeId?.let {
                                    level?.let {
                                        dayOfWeek?.let {
                                            subType?.let {
                                                navController.navigate(Screen.WorkingWeeksHoursSetUpFinish.createRoute(userId, enterpriseId, employeeId, level, dayOfWeek, subType))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    },
                    onBackClick = {userId, enterpriseId, employeeId, level ->
                        userId?.let {
                            enterpriseId?.let {
                                employeeId?.let {
                                    level?.let {
                                        navController.navigate(Screen.WorkingWeeksHours.createRoute(userId, enterpriseId, employeeId, level))
                                    }
                                }
                            }
                        }
                    }
                )
            }

            composable(
                route = Screen.WorkingWeeksHoursSetUpFinish.route,
                arguments = listOf(navArgument("userId") { type = NavType.IntType },
                    navArgument("enterpriseId") { type = NavType.IntType },
                    navArgument("employeeId") { type = NavType.IntType },
                    navArgument("level") { type = NavType.StringType },
                    navArgument("dayOfWeek") { type = NavType.StringType },
                    navArgument("subType") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId")
                val enterpriseId = backStackEntry.arguments?.getInt("enterpriseId")
                val employeeId = backStackEntry.arguments?.getInt("employeeId")
                val level = backStackEntry.arguments?.getString("level")?: ""
                val dayOfWeek = backStackEntry.arguments?.getString("dayOfWeek")?: ""
                val subType = backStackEntry.arguments?.getString("subType")?: ""
                WorkingWeeksHoursSetUpScreenFinish(
                    userId = userId,
                    enterpriseId = enterpriseId,
                    employeeId = employeeId,
                    level = level,
                    dayOfWeek = dayOfWeek,
                    subType = subType,
                    onSaveClick = {userId, enterpriseId ->
                        userId?.let {
                            enterpriseId?.let {

                                navController.navigate(Screen.ChartEmployeeSelect.createRoute(userId, enterpriseId))

                            }
                        }
                    },
                    onBackClick = {userId, enterpriseId, employeeId, level, dayOfWeek  ->
                        userId?.let {
                            enterpriseId?.let {
                                employeeId?.let {
                                    level?.let {
                                        dayOfWeek?.let {
                                            navController.navigate(Screen.WorkingWeeksHoursSetUp.createRoute(userId, enterpriseId, employeeId, level, dayOfWeek))
                                        }
                                    }
                                }
                            }
                        }
                    }
                )
            }

            composable(
                route = Screen.ChartChoice.route,
                arguments = listOf(navArgument("userId") { type = NavType.IntType },
                    navArgument("enterpriseId") { type = NavType.IntType },
                    navArgument("employeeId") { type = NavType.IntType },
                    navArgument("level") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId")
                val enterpriseId = backStackEntry.arguments?.getInt("enterpriseId")
                val employeeId = backStackEntry.arguments?.getInt("employeeId")
                val level = backStackEntry.arguments?.getString("level")?: ""
                ChartChoiceScreen (
                    userId = userId,
                    enterpriseId = enterpriseId,
                    employeeId = employeeId,
                    level = level,
                    onNextClick = {userId, enterpriseId, employeeId , level, dayWork, dayRest  ->
                        userId?.let {
                            enterpriseId?.let {
                                employeeId?.let {
                                    level?.let {
                                        dayWork?.let {
                                            dayRest?.let {
                                                navController.navigate(Screen.ChartChoiceSetUp.createRoute(userId, enterpriseId, employeeId, level, dayWork, dayRest))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    },
                    onBackClick = {userId, enterpriseId, employeeId  ->
                        userId?.let {
                            enterpriseId?.let {
                                employeeId?.let {
                                    navController.navigate(Screen.ChartEmployeeSetUp.createRoute(userId, enterpriseId, employeeId))
                                }
                            }
                        }
                    }
                )
            }

            composable(
                route = Screen.ChartChoiceSetUp.route,
                arguments = listOf(navArgument("userId") { type = NavType.IntType },
                    navArgument("enterpriseId") { type = NavType.IntType },
                    navArgument("employeeId") { type = NavType.IntType },
                    navArgument("level") { type = NavType.StringType },
                    navArgument("dayWork") { type = NavType.StringType },
                    navArgument("dayRest") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId")
                val enterpriseId = backStackEntry.arguments?.getInt("enterpriseId")
                val employeeId = backStackEntry.arguments?.getInt("employeeId")
                val level = backStackEntry.arguments?.getString("level")?: ""
                val dayWork = backStackEntry.arguments?.getString("dayWork")?: ""
                val dayRest = backStackEntry.arguments?.getString("dayRest")?: ""
                ChartChoiceSetUpScreen(
                    userId = userId,
                    enterpriseId = enterpriseId,
                    employeeId = employeeId,
                    level = level,
                    dayWork = dayWork,
                    dayRest = dayRest,
                    onSaveClick = {userId, enterpriseId, employeeId , level,  dayWork, dayRest, subType  ->
                        userId?.let {
                            enterpriseId?.let {
                                employeeId?.let {
                                    level?.let {
                                        dayWork?.let {
                                            dayRest?.let {
                                                subType?.let {
                                                    navController.navigate(Screen.ChartChoiceHours.createRoute(userId, enterpriseId, employeeId, level, dayWork, dayRest, subType))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    },
                    onBackClick = {userId, enterpriseId, employeeId, level ->
                        userId?.let {
                            enterpriseId?.let {
                                employeeId?.let {
                                    level?.let {
                                        navController.navigate(Screen.ChartChoice.createRoute(userId, enterpriseId, employeeId, level))
                                    }
                                }
                            }
                        }
                    }
                )
            }

            composable(
                route = Screen.ChartChoiceHours.route,
                arguments = listOf(navArgument("userId") { type = NavType.IntType },
                    navArgument("enterpriseId") { type = NavType.IntType },
                    navArgument("employeeId") { type = NavType.IntType },
                    navArgument("level") { type = NavType.StringType },
                    navArgument("dayWork") { type = NavType.StringType },
                    navArgument("dayRest") { type = NavType.StringType },
                    navArgument("subType") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId")
                val enterpriseId = backStackEntry.arguments?.getInt("enterpriseId")
                val employeeId = backStackEntry.arguments?.getInt("employeeId")
                val level = backStackEntry.arguments?.getString("level")?: ""
                val dayWork = backStackEntry.arguments?.getString("dayWork")?: ""
                val dayRest = backStackEntry.arguments?.getString("dayRest")?: ""
                val subType = backStackEntry.arguments?.getString("subType")?: ""
                ChartChoiceHoursScreen (
                    userId = userId,
                    enterpriseId = enterpriseId,
                    employeeId = employeeId,
                    level = level,
                    dayWork = dayWork,
                    dayRest = dayRest,
                    subType = subType,
                    onSaveClick = {userId, enterpriseId ->
                        userId?.let {
                            enterpriseId?.let {
                                navController.navigate(Screen.ChartEmployeeSelect.createRoute(userId, enterpriseId))
                            }
                        }
                    },
                    onBackClick = {userId, enterpriseId, employeeId, level,  dayWork, dayRest,  ->
                        userId?.let {
                            enterpriseId?.let {
                                employeeId?.let {
                                    level?.let {
                                        dayWork?.let {
                                            dayRest?.let {
                                                navController.navigate(Screen.ChartChoiceSetUp.createRoute(userId, enterpriseId, employeeId, level, dayWork, dayRest))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                )
            }

            composable(
                route = Screen.Schedule.route,
                arguments = listOf(navArgument("userId") { type = NavType.IntType },
                    navArgument("enterpriseId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId")
                val enterpriseId = backStackEntry.arguments?.getInt("enterpriseId")
                ScheduleScreen(
                    userId = userId,
                    enterpriseId = enterpriseId,
                    onConfigureClick = {userId, enterpriseId ->
                        userId?.let {
                            enterpriseId?.let {
                                navController.navigate(Screen.ChartEmployeeSelect.createRoute(userId, enterpriseId))
                            }
                        }
                    },
                    onBackClick = {userId, enterpriseId ->
                        userId?.let {
                            enterpriseId?.let {
                                navController.navigate(Screen.Employee.createRoute(userId, enterpriseId))
                            }
                        }
                    }
                )
            }
        }
    }