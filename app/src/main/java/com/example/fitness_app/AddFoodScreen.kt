package com.example.fitness_app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.fitness_app.model.FoodItem

@Composable
fun AddFoodScreen(
    onAdd: (FoodItem) -> Unit,
    onCancel: () -> Unit
) {
    var foodName by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .widthIn(min = 320.dp)
            .padding(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Add Food", style = MaterialTheme.typography.titleLarge)
            TextButton(onClick = onCancel) { Text("X") }
        }
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = foodName,
            onValueChange = { foodName = it },
            label = { Text("Food Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = calories,
            onValueChange = { input -> if (input.all { it.isDigit() }) calories = input },
            label = { Text("Calories") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = quantity,
            onValueChange = { quantity = it },
            label = { Text("Quantity (Pieces / Grams)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = onCancel) { Text("Cancel") }
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = {
                    val item = FoodItem(
                        name = foodName.ifBlank { "Unnamed" },
                        quantity = quantity.ifBlank { "1 piece" },
                        calories = calories.toIntOrNull() ?: 0
                    )
                    onAdd(item)
                },
                enabled = foodName.isNotBlank() && calories.isNotBlank()
            ) {
                Text("Add")
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}
