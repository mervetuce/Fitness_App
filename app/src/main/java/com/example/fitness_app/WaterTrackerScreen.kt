package com.example.fitness_app

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitness_app.model.WaterEntry
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WaterTrackerScreen(
    vm: WaterViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    var isAdding by remember { mutableStateOf(false) }

    val total = vm.total
    val goal = vm.dailyGoal
    val progress = (total / goal.toFloat()).coerceIn(0f, 1f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Title
        Text(
            text = "Water Tracker 💧",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        // Progress Card
        Card(Modifier.fillMaxWidth()) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Dairesel gösterge + ortada ml
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(progress = { progress }, strokeWidth = 8.dp)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("$total ml", style = MaterialTheme.typography.titleMedium)
                        Text("${(progress * 100).toInt()}% of daily goal", style = MaterialTheme.typography.bodySmall)
                    }
                }

                // Linear progress + uç etiketler
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("0 ml", style = MaterialTheme.typography.bodySmall)
                    Text("$goal ml", style = MaterialTheme.typography.bodySmall)
                }

                // Add Water
                Button(
                    onClick = { isAdding = !isAdding },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(text = "+ Add Water")
                }

                // Hızlı ekleme butonları
                if (isAdding) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf(100, 200, 500).forEach { amount ->
                            OutlinedButton(onClick = {
                                val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                                vm.add(amount, time)
                                isAdding = false
                            }) {
                                Text("+ ${amount} ml")
                            }
                        }
                    }
                    OutlinedButton(onClick = { isAdding = false }) {
                        Text("Cancel")
                    }
                }
            }
        }

        // Daily Requirement
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Daily Requirement", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("💧 $goal ml")
                Text("Based on your weight (70 kg)", style = MaterialTheme.typography.bodySmall)
            }
        }

        // Today's Log
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Today's Log", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    if (vm.log.isNotEmpty()) {
                        IconButton(onClick = {
                            // tümünü temizle
                            while (vm.log.isNotEmpty()) vm.removeAt(0)
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Clear all")
                        }
                    }
                }

                if (vm.log.isEmpty()) {
                    Text("No entries yet.", style = MaterialTheme.typography.bodySmall)
                } else {
                    Spacer(Modifier.height(8.dp))
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 8.dp)
                    ) {
                        itemsIndexed(vm.log) { index, entry: WaterEntry ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("💧 ${entry.amount} ml")
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(entry.time)
                                    IconButton(onClick = { vm.removeAt(index) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
