package com.example.fitness_app.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FoodBank
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fitness_app.model.FoodItem

@Composable
fun FoodLogList(
    foodLog: List<FoodItem>,
    onRemoveFood: (FoodItem) -> Unit
) {
    if (foodLog.isEmpty()) {
        Text("No food added yet.")
    } else {
        Column {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxHeight()
            ) {
                items(foodLog.size) { index ->
                    val item = foodLog[index]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF1F1F1))
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Row {
                                    Icon(
                                        imageVector = Icons.Filled.FoodBank,
                                        contentDescription = "Food"
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(item.name)
                                }
                                Text(item.quantity, style = MaterialTheme.typography.bodySmall)
                            }

                            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                                Text(
                                    "${item.calories} kcal",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                IconButton(onClick = { onRemoveFood(item) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Food",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
