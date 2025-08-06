package com.example.fitness_app.ui.theme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import com.example.fitness_app.model.WaterEntry
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun WaterLogList(waterLog: List<WaterEntry>, onRemoveWater: (WaterEntry) -> Unit) {
    if (waterLog.isEmpty()) {
        Text("No water added yet.")
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Today's Log", style = MaterialTheme.typography.titleMedium)

            LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items(waterLog) { entry ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("${entry.amount} ml")
                                Text(entry.time, style = MaterialTheme.typography.bodySmall)
                            }
                            IconButton(onClick = { onRemoveWater(entry) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// 💡 Örnek zaman oluşturucu
@RequiresApi(Build.VERSION_CODES.O)
val currentTime: String = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
