package com.example.fitness_app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.text.KeyboardOptions
import com.example.fitness_app.model.FoodItem

@Composable
fun DietTrackerScreen(
    vm: DietViewModel = viewModel(LocalContext.current as androidx.activity.ComponentActivity)
) {
    var showAdd by remember { mutableStateOf(false) }

    val total = vm.totalCalories
    val goal = vm.calorieGoal
    val progress = (total / goal.toFloat()).coerceIn(0f, 1f)

    Scaffold { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Diet Tracker 🍎", style = MaterialTheme.typography.headlineMedium)

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("$total kcal", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                    Text("${(progress * 100).toInt()}% of daily goal", style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(progress = progress)
                    Spacer(Modifier.height(8.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("0 kcal"); Text("$goal kcal")
                    }
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = { showAdd = true }, modifier = Modifier.fillMaxWidth()) {
                        Text("+ Add Food")
                    }
                }
            }

            if (vm.foodLog.isNotEmpty()) {
                Text("Today's Food Log", style = MaterialTheme.typography.titleMedium)
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    itemsIndexed(vm.foodLog) { index, item ->
                        Card(Modifier.fillMaxWidth()) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(item.name, style = MaterialTheme.typography.bodyLarge)
                                    Text(item.quantity, style = MaterialTheme.typography.bodySmall)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("${item.calories} kcal")
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

    if (showAdd) {
        Dialog(onDismissRequest = { showAdd = false }) {
            Surface(shape = MaterialTheme.shapes.medium, tonalElevation = 2.dp) {
                AddFoodForm(
                    onCancel = { showAdd = false },
                    onAdd = { item ->
                        vm.addFood(item)
                        showAdd = false
                    }
                )
            }
        }
    }
}

@Composable
private fun AddFoodForm(
    onAdd: (FoodItem) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var qty by remember { mutableStateOf("") }

    Column(Modifier.widthIn(min = 320.dp).padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Add Food", style = MaterialTheme.typography.titleLarge)
            TextButton(onClick = onCancel) { Text("X") }
        }
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Food Name") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = calories,
            onValueChange = { if (it.all(Char::isDigit)) calories = it },
            label = { Text("Calories") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = qty, onValueChange = { qty = it }, label = { Text("Quantity (Pieces / Grams)") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = onCancel) { Text("Cancel") }
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = {
                    onAdd(
                        FoodItem(
                            name = name.ifBlank { "Unnamed" },
                            quantity = qty.ifBlank { "1 piece" },
                            calories = calories.toIntOrNull() ?: 0
                        )
                    )
                },
                enabled = name.isNotBlank() && calories.isNotBlank()
            ) { Text("Add") }
        }
    }
}
