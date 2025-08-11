// AppNavigation.kt
package com.example.fitness_app

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Auth durumuna göre startDestination seç
    val activity = LocalContext.current as ComponentActivity
    val authVm: AuthViewModel = viewModel(viewModelStoreOwner = activity)
    val isLoggedIn = authVm.isLoggedIn   // Boolean; collectAsState() yok

    val start = if (isLoggedIn) Routes.HOME else Routes.LOGIN

    NavHost(
        navController = navController,
        startDestination = start
    ) {
        // AUTH
        composable(Routes.LOGIN)    { LoginScreen(navController) }
        composable(Routes.REGISTER) { RegisterScreen(navController) }
        composable(Routes.RESET)    { ResetPasswordScreen(navController) }

        // HOME
        composable(Routes.HOME) { HomeScreen(navController = navController) }

        // DIET
        composable(Routes.DIET) {
            Scaffold(bottomBar = { BottomNavigationBar(navController) }) { padding ->
                Box(Modifier.padding(padding)) { DietTrackerScreen() }
            }
        }

        // WATER
        composable(Routes.WATER) {
            Scaffold(bottomBar = { BottomNavigationBar(navController) }) { padding ->
                Box(Modifier.padding(padding)) { WaterTrackerScreen() }
            }
        }

        // FITNESS
        composable(Routes.FITNESS) {
            Scaffold(bottomBar = { BottomNavigationBar(navController) }) { padding ->
                Box(Modifier.padding(padding)) { FitnessTrackerScreen() }
            }
        }

        // SETTINGS
        composable(Routes.SETTINGS) {
            Scaffold(bottomBar = { BottomNavigationBar(navController) }) { padding ->
                Box(Modifier.padding(padding)) { SettingsScreen(navController = navController) }
            }
        }
    }
}

/** Aynı route’a tekrar gitmeyi engelleyen ve state’i koruyan navigate helper */
private fun NavController.safeNavigate(route: String) {
    val current = currentBackStackEntry?.destination?.route
    if (current == route) return
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
