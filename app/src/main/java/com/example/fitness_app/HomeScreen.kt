package com.example.fitness_app

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    // Aynı Activity scope’undan VM'leri alıyoruz (ekranlar arası paylaşım için)
    val ctx = LocalContext.current as ComponentActivity
    val dietVm: DietViewModel = viewModel(ctx)
    val waterVm: WaterViewModel = viewModel(ctx)
    val fitnessVm: FitnessViewModel = viewModel(ctx)
    val fitnessProgress = (fitnessVm.completedCount / fitnessVm.dailyGoal.toFloat()).coerceIn(0f, 1f)
    val profileVm: ProfileViewModel = viewModel(ctx)


    // Diet verileri
    val totalCalories = dietVm.totalCalories
    val calorieGoal = dietVm.calorieGoal
    val dietProgress = (totalCalories / calorieGoal.toFloat()).coerceIn(0f, 1f)

    // Water verileri
    val totalWater = waterVm.total
    val waterGoal = waterVm.dailyGoal
    val waterProgress = (totalWater / waterGoal.toFloat()).coerceIn(0f, 1f)

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Hi,${profileVm.name}  👋",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "How are you today?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProgressItem("Water", waterProgress)
                ProgressItem("Diet", dietProgress)
                ProgressItem("Fitness", fitnessProgress)
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoCard("Water Intake", "$totalWater / $waterGoal ml")
                InfoCard("Calories", "$totalCalories / $calorieGoal kcal")
                // İstersen buraya Fitness kartını da eklersin
            }
        }
    }
}

/** Küçük yuvarlak progress + yüzde yazısı */
@Composable
private fun ProgressItem(label: String, progress: Float) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier.height(64.dp),
            strokeWidth = 6.dp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text("$label\n${(progress * 100).toInt()}%", textAlign = TextAlign.Center)
    }
}

/** Başlık + değer gösteren sade kart */
@Composable
private fun InfoCard(title: String, value: String) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            Text(value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
