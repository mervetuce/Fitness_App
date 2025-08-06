package com.example.fitness_app

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fitness_app.model.FoodItem
import com.example.fitness_app.ui.theme.FoodLogList // ✅ Gerekli import

@Composable
fun HomeScreen(
    navController: NavController,
    foodLog: List<FoodItem>,
    onRemoveFood: (FoodItem) -> Unit,
    onNavigateToDiet: () -> Unit,
    onNavigateToWater: () -> Unit,
    onNavigateToFitness: () -> Unit,
    onNavigateToHome: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    // ✅ calories zaten Int ise doğrudan toplanabilir
    val totalCalories = foodLog.sumOf { it.calories }
    val calorieGoal = 2000
    val calorieProgress = totalCalories.toFloat() / calorieGoal

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Hi, Demo User 👋",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "How are you today?",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProgressItem("Water", 0f, Color.Blue)
                ProgressItem("Diet", calorieProgress.coerceIn(0f, 1f), Color.Green)
                ProgressItem("Fitness", 0f, Color.Red)
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoCard("Water Intake", "0 / 2500 ml", Color.Blue)
                InfoCard("Calories", "$totalCalories / $calorieGoal kcal", Color.Green)
                InfoCard("Fitness", "0 / 9 exercises", Color.Red)
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (foodLog.isNotEmpty()) {
                Text("Recent Foods", style = MaterialTheme.typography.titleMedium)
                FoodLogList(foodLog = foodLog, onRemoveFood = onRemoveFood)
            }
        }
    }
}

@Composable
fun ProgressItem(label: String, progress: Float, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator(
            progress = progress,
            modifier = Modifier.size(64.dp),
            color = color,
            strokeWidth = 6.dp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text("$label\n${(progress * 100).toInt()}%", textAlign = TextAlign.Center)
    }
}

@Composable
fun InfoCard(text: String, value: String, color: Color) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text, style = MaterialTheme.typography.labelMedium, color = Color.DarkGray)
            Text(value, style = MaterialTheme.typography.bodyLarge, color = color)
        }
    }
}
