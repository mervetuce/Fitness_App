package com.example.fitness_app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*

import com.example.fitness_app.model.FoodItem
import com.example.fitness_app.model.WaterEntry

/** Route sabitleri */
private const val ROUTE_HOME = "home"
private const val ROUTE_DIET = "diet"
private const val ROUTE_WATER = "water"
private const val ROUTE_FITNESS = "fitness"

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val dummyFoodLog = listOf<FoodItem>()
    val dummyWaterLog = listOf<WaterEntry>()

    NavHost(
        navController = navController,
        startDestination = ROUTE_HOME
    ) {
        composable(ROUTE_HOME) {
            HomeScreen(
                navController = navController,
                foodLog = dummyFoodLog,
                onRemoveFood = {},
                // waterLog = dummyWaterLog, ❌ HomeScreen'de bu yoksa yoruma al
                // onRemoveWater = {},       ❌ HomeScreen'de bu yoksa yoruma al
                onNavigateToDiet = { navController.safeNavigate(ROUTE_DIET) },
                onNavigateToWater = { navController.safeNavigate(ROUTE_WATER) },
                onNavigateToFitness = { navController.safeNavigate(ROUTE_FITNESS) }
            )
        }

        composable(ROUTE_DIET)    { SimpleScaffold(navController, "Diet") }
        composable(ROUTE_WATER)   { SimpleScaffold(navController, "Water") }
        composable(ROUTE_FITNESS) { SimpleScaffold(navController, "Fitness") }
    }
}

@Composable
private fun SimpleScaffold(navController: NavController, title: String) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text(text = title, style = MaterialTheme.typography.headlineSmall)
        }
    }
}

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
