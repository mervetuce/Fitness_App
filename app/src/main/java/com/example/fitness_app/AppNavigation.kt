package com.example.fitness_app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

private const val ROUTE_HOME = "home"
private const val ROUTE_DIET = "diet"
private const val ROUTE_WATER = "water"
private const val ROUTE_FITNESS = "fitness"

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ROUTE_HOME
    ) {
        composable(ROUTE_HOME) {
            // HomeScreen kendi içinde bottom bar kullanıyorsa burada ekstra Scaffold gerekmez
            HomeScreen(navController = navController)
        }

        composable(ROUTE_DIET) {
            Scaffold(bottomBar = { BottomNavigationBar(navController) }) { padding ->
                Box(Modifier.padding(padding)) {
                    DietTrackerScreen()
                }
            }
        }

        composable(ROUTE_WATER) {
            Scaffold(bottomBar = { BottomNavigationBar(navController) }) { padding ->
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Water", style = MaterialTheme.typography.headlineSmall)
                }
            }
        }

        composable(ROUTE_FITNESS) {
            Scaffold(bottomBar = { BottomNavigationBar(navController) }) { padding ->
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Fitness", style = MaterialTheme.typography.headlineSmall)
                }
            }
        }
        composable(ROUTE_WATER) {
            Scaffold(bottomBar = { BottomNavigationBar(navController) }) { padding ->
                Box(Modifier.padding(padding)) { WaterTrackerScreen() }
            }
        }

        composable("fitness") {
            Scaffold(bottomBar = { BottomNavigationBar(navController) }) { padding ->
                Box(Modifier.padding(padding)) { FitnessTrackerScreen() }
            }
        }


    }
}

/** Güvenli navigate: aynı sayfaya tekrar gitme, state’i koru */
private fun NavController.safeNavigate(route: String) {
    val current = currentBackStackEntry?.destination?.route
    if (current == route) return
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
