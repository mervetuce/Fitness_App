package com.example.fitness_app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fitness_app.model.FoodItem
import com.example.fitness_app.ui.theme.FoodLogList

@Composable
fun DietTrackerScreen(
    navController: NavController,
    onAddFoodClick: () -> Unit,
    foodLog: List<FoodItem>,
    onRemoveFood: (FoodItem) -> Unit
) {
    val totalCalories = foodLog.sumOf { it.calories }
    val dailyGoal = 2000
    val progress = totalCalories / dailyGoal.toFloat()

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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Diet Tracker 🍎",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.Start)
            )

            CircularProgressIndicator(
                progress = progress.coerceIn(0f, 1f),
                modifier = Modifier.size(120.dp),
                color = Color.Green
            )
            Text("$totalCalories kcal", style = MaterialTheme.typography.headlineSmall)
            Text(
                "${((progress * 100).toInt())}% of daily goal",
                style = MaterialTheme.typography.bodyMedium
            )

            LinearProgressIndicator(
                progress = progress.coerceIn(0f, 1f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )

            Button(
                onClick = onAddFoodClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
            ) {
                Text("+ Add Food", color = Color.White)
            }

            FoodLogList(
                foodLog = foodLog,
                onRemoveFood = onRemoveFood
            )
        }
    }
}
