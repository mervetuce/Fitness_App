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
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.fitness_app.data.Profile

@Composable
fun HomeScreen(navController: NavController) {
    // Activity scope VM’ler
    val ctx = LocalContext.current as ComponentActivity
    val dietVm: DietViewModel = viewModel(viewModelStoreOwner = ctx)
    val waterVm: WaterViewModel = viewModel(viewModelStoreOwner = ctx)
    val fitnessVm: FitnessViewModel = viewModel(viewModelStoreOwner = ctx)
    val profileVm: ProfileViewModel = viewModel(viewModelStoreOwner = ctx)

    // Profile StateFlow (null gelebilir)
    val profile by profileVm.profile.collectAsState(initial = null)
    val displayName = profile?.name?.takeIf { it.isNotBlank() } ?: "User"

    // Güvenli progress hesapları (0/0 -> 0f, NaN guard)
    val dietProgress = safeProgress(dietVm.totalCalories, dietVm.calorieGoal)
    val waterProgress = safeProgress(waterVm.total, waterVm.dailyGoal)
    val fitnessProgress = safeProgress(fitnessVm.completedCount, fitnessVm.dailyGoal)

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
                text = "Hi, $displayName 👋",
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
                InfoCard("Water Intake", "${waterVm.total} / ${waterVm.dailyGoal} ml")
                InfoCard("Calories", "${dietVm.totalCalories} / ${dietVm.calorieGoal} kcal")
            }
        }
    }
}

/** 0/0 ve NaN durumlarına karşı güvenli progress */
private fun safeProgress(total: Int, goal: Int): Float {
    val p = if (goal > 0) total.toFloat() / goal.toFloat() else 0f
    return if (p.isFinite()) p.coerceIn(0f, 1f) else 0f
}

/** Küçük yuvarlak progress + yüzde yazısı */
@Composable
private fun ProgressItem(label: String, progress: Float) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator(
            progress = { progress }, // her zaman 0..1
            modifier = Modifier.size(64.dp),
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
            Text(
                title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
