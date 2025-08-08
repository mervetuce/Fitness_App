package com.example.fitness_app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Route sabitleri
private const val ROUTE_HOME = "home"
private const val ROUTE_DIET = "diet"
private const val ROUTE_WATER = "water"
private const val ROUTE_FITNESS = "fitness"
private const val ROUTE_SETTINGS = "settings"

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ROUTE_HOME
    ) {
        // HOME (HomeScreen kendi içinde bottom bar kullanıyor)
        composable(ROUTE_HOME) {
            HomeScreen(navController = navController)
        }

        // DIET
        composable(ROUTE_DIET) {
            Scaffold(bottomBar = { BottomNavigationBar(navController) }) { padding ->
                Box(Modifier.padding(padding)) {
                    DietTrackerScreen()
                }
            }
        }

        // WATER
        composable(ROUTE_WATER) {
            Scaffold(bottomBar = { BottomNavigationBar(navController) }) { padding ->
                Box(Modifier.padding(padding)) {
                    WaterTrackerScreen()
                }
            }
        }

        // FITNESS
        composable(ROUTE_FITNESS) {
            Scaffold(bottomBar = { BottomNavigationBar(navController) }) { padding ->
                Box(Modifier.padding(padding)) {
                    FitnessTrackerScreen()
                }
            }
        }

        // SETTINGS
        composable(ROUTE_SETTINGS) {
            Scaffold(bottomBar = { BottomNavigationBar(navController) }) { padding ->
                Box(Modifier.padding(padding)) {
                    SettingsScreen(navController = navController)
                }
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
