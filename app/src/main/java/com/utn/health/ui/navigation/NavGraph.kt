package com.utn.health.ui.navigation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.health.connect.client.PermissionController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.utn.health.HealthApplication
import com.utn.health.ui.screens.ConnectionStatusScreen
import com.utn.health.ui.screens.DashboardScreen
import com.utn.health.ui.screens.HeartRateDetailScreen
import com.utn.health.ui.screens.HistoryScreen
import com.utn.health.ui.screens.PermissionsScreen
import com.utn.health.ui.screens.PrivacyPolicyScreen
import com.utn.health.ui.screens.StreakDetailScreen
import com.utn.health.viewmodel.ConnectionViewModel
import com.utn.health.viewmodel.DashboardViewModel
import com.utn.health.viewmodel.HeartRateDetailViewModel
import com.utn.health.viewmodel.HistoryViewModel
import com.utn.health.viewmodel.StreakViewModel
import kotlinx.coroutines.launch

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Dashboard.route
) {
    val context = LocalContext.current
    val app = context.applicationContext as HealthApplication
    val coroutineScope = rememberCoroutineScope()

    // ViewModel Factories
    val dashboardViewModel: DashboardViewModel = viewModel(
        factory = DashboardViewModel.Factory(
            app.getTodaySummaryUseCase,
            app.manageStreakUseCase
        )
    )

    val historyViewModel: HistoryViewModel = viewModel(
        factory = HistoryViewModel.Factory(
            app.getHistoryUseCase
        )
    )

    val heartRateDetailViewModel: HeartRateDetailViewModel = viewModel(
        factory = HeartRateDetailViewModel.Factory(
            app.getHeartRateDetailsUseCase
        )
    )

    val connectionViewModel: ConnectionViewModel = viewModel(
        factory = ConnectionViewModel.Factory(
            app.checkConnectionUseCase
        )
    )

    val streakViewModel: StreakViewModel = viewModel(
        factory = StreakViewModel.Factory(
            app.manageStreakUseCase
        )
    )

    // Health Connect Permission Request Launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = PermissionController.createRequestPermissionResultContract()
    ) { granted ->
        connectionViewModel.refresh()
        dashboardViewModel.refresh()
    }

    val requestPermissionsAction: () -> Unit = {
        coroutineScope.launch {
            val permissions = app.repository.getConnectionState()
            // Solicitar permisos requeridos definidos en la aplicación
            val required = setOf(
                androidx.health.connect.client.permission.HealthPermission.getReadPermission(androidx.health.connect.client.records.StepsRecord::class),
                androidx.health.connect.client.permission.HealthPermission.getReadPermission(androidx.health.connect.client.records.DistanceRecord::class),
                androidx.health.connect.client.permission.HealthPermission.getReadPermission(androidx.health.connect.client.records.TotalCaloriesBurnedRecord::class),
                androidx.health.connect.client.permission.HealthPermission.getReadPermission(androidx.health.connect.client.records.HeartRateRecord::class),
                androidx.health.connect.client.permission.HealthPermission.getReadPermission(androidx.health.connect.client.records.SleepSessionRecord::class)
            )
            permissionLauncher.launch(required)
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                viewModel = dashboardViewModel,
                onNavigateToHeartRateDetail = { navController.navigate(Screen.HeartRateDetail.route) },
                onNavigateToHistory = { navController.navigate(Screen.History.route) },
                onNavigateToStreak = { navController.navigate(Screen.StreakDetail.route) },
                onNavigateToConnection = { navController.navigate(Screen.ConnectionStatus.route) },
                onNavigateToPrivacy = { navController.navigate(Screen.PrivacyPolicy.route) }
            )
        }

        composable(Screen.HeartRateDetail.route) {
            HeartRateDetailScreen(
                viewModel = heartRateDetailViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.History.route) {
            HistoryScreen(
                viewModel = historyViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.StreakDetail.route) {
            StreakDetailScreen(
                viewModel = streakViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.ConnectionStatus.route) {
            ConnectionStatusScreen(
                viewModel = connectionViewModel,
                onRequestPermissions = requestPermissionsAction,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Permissions.route) {
            PermissionsScreen(
                onRequestPermissions = requestPermissionsAction,
                onNavigateToPrivacy = { navController.navigate(Screen.PrivacyPolicy.route) }
            )
        }

        composable(Screen.PrivacyPolicy.route) {
            PrivacyPolicyScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
