package com.example.fitness_app

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun FitnessTrackerScreen(
    vm: FitnessViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    var selectedTab by remember { mutableStateOf(Difficulty.EASY) }

    val goal = vm.dailyGoal
    val done = vm.completedCount
    val progress = (done / goal.toFloat()).coerceIn(0f, 1f)

    Scaffold { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Fitness Tracker💪", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

            // Progress Card
            Card(Modifier.fillMaxWidth()) {
                Column(
                    Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("$done / $goal", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                    Text("${(progress * 100).toInt()}% of daily goal", style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth())
                }
            }

            // Tabs: Easy / Medium / Hard
            val tabs = listOf(Difficulty.EASY, Difficulty.MEDIUM, Difficulty.HARD)
            val titles = mapOf(
                Difficulty.EASY to "Easy",
                Difficulty.MEDIUM to "Medium",
                Difficulty.HARD to "Hard"
            )
            TabRow(selectedTabIndex = tabs.indexOf(selectedTab)) {
                tabs.forEachIndexed { index, diff ->
                    Tab(
                        selected = selectedTab == diff,
                        onClick = { selectedTab = diff },
                        text = { Text(titles[diff]!!) }
                    )
                }
            }

            // Section title
            Text(
                text = "${titles[selectedTab]} Exercises",
                style = MaterialTheme.typography.titleMedium
            )

            // Exercise list for current tab
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(vm.byDifficulty(selectedTab)) { ex ->
                    ExerciseItem(
                        ex = ex,
                        onToggleDone = { vm.toggleDone(ex.id) },
                        onToggleExpand = { vm.toggleExpanded(ex.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ExerciseItem(
    ex: Exercise,
    onToggleDone: () -> Unit,
    onToggleExpand: () -> Unit
) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onToggleDone) {
                        Icon(
                            if (ex.done) Icons.Filled.CheckBox else Icons.Filled.CheckBoxOutlineBlank,
                            contentDescription = if (ex.done) "Done" else "Not done",
                        )
                    }
                    Spacer(Modifier.width(4.dp))
                    Text(ex.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                }
                IconButton(onClick = onToggleExpand) {
                    Icon(if (ex.expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore, contentDescription = "Expand")
                }
            }

            AnimatedVisibility(ex.expanded) {
                Text(
                    text = ex.description,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 48.dp, top = 8.dp, end = 8.dp, bottom = 4.dp)
                )
            }
        }
    }
}
