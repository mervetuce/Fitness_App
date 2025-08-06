package com.example.fitness_app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

/** Alt bar elemanı için veri sınıfı */
data class BottomItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)

/** Tek tanımlı BottomNavigationBar */
@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomItem("Home", "home", Icons.Filled.Home),
        BottomItem("Diet", "diet", Icons.Filled.Restaurant),
        BottomItem("Water", "water", Icons.Filled.WaterDrop),
        BottomItem("Fitness", "fitness", Icons.Filled.FitnessCenter),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination: NavDestination? = navBackStackEntry?.destination

    NavigationBar {
        items.forEach { item ->
            val selected = currentDestination?.route == item.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}
