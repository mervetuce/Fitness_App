package com.example.fitness_app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.time.LocalTime
import java.time.format.DateTimeFormatter

// Data model for water intake log
data class WaterLog(val amount: Int, val time: String = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")))

@Composable
fun WaterTrackerScreen(navController: NavController) {
    val dailyGoal = 2500 // ml
    var totalIntake by remember { mutableStateOf(0) }
    var showButtons by remember { mutableStateOf(false) }
    val waterLog = remember { mutableStateListOf<WaterLog>() }

    val progress = totalIntake / dailyGoal.toFloat()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Water Tracker 💧", style = MaterialTheme.typography.headlineMedium)

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("${totalIntake} ml", fontSize = 24.sp, color = MaterialTheme.colorScheme.primary)
                    Text("${((progress * 100).toInt())}% of daily goal", fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(progress = progress.coerceIn(0f, 1f), modifier = Modifier.fillMaxWidth())

                    Spacer(modifier = Modifier.height(8.dp))

                    if (!showButtons) {
                        Button(
                            onClick = { showButtons = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("+ Add Water")
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            listOf(100, 200, 500).forEach { amount ->
                                Button(onClick = {
                                    totalIntake += amount
                                    waterLog.add(WaterLog(amount))
                                    showButtons = false
                                }) {
                                    Text("+ $amount ml")
                                }
                            }
                        }
                        TextButton(onClick = { showButtons = false }) {
                            Text("Cancel")
                        }
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Daily Requirement", fontSize = 16.sp, color = Color.Gray)
                    Text("$dailyGoal ml\nBased on your weight (70 kg)", fontSize = 14.sp)
                }
            }

            if (waterLog.isNotEmpty()) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Today's Log", fontSize = 16.sp)
                        LazyColumn {
                            items(waterLog) { log ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("${log.amount} ml")
                                    Text(log.time)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
