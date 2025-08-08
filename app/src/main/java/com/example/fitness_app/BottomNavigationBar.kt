package com.example.fitness_app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
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
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = navController.currentDestination?.route == "home",
            onClick = { navController.navigate("home") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Restaurant, contentDescription = "Diet") },
            label = { Text("Diet") },
            selected = navController.currentDestination?.route == "diet",
            onClick = { navController.navigate("diet") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.WaterDrop, contentDescription = "Water") },
            label = { Text("Water") },
            selected = navController.currentDestination?.route == "water",
            onClick = { navController.navigate("water") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.FitnessCenter, contentDescription = "Fitness") },
            label = { Text("Fitness") },
            selected = navController.currentDestination?.route == "fitness",
            onClick = { navController.navigate("fitness") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = navController.currentDestination?.route == "settings",
            onClick = { navController.navigate("settings") }
        )
    }
}
