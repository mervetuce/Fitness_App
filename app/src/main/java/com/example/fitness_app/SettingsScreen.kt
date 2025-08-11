package com.example.fitness_app

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    navController: NavController,
    dietVm: DietViewModel = viewModel(viewModelStoreOwner = LocalContext.current as ComponentActivity),
    waterVm: WaterViewModel = viewModel(viewModelStoreOwner = LocalContext.current as ComponentActivity),
    profileVm: ProfileViewModel = viewModel(viewModelStoreOwner = LocalContext.current as ComponentActivity)
) {
    val ctx = LocalContext.current as ComponentActivity
    val authVm: AuthViewModel = viewModel(viewModelStoreOwner = ctx)

    var name by remember { mutableStateOf(profileVm.name) }
    var email by remember { mutableStateOf(profileVm.email) }
    var weight by remember { mutableStateOf(profileVm.weightKg.toString()) }
    var waterGoalInput by remember { mutableStateOf(waterVm.dailyGoal.toString()) }
    var calorieGoalInput by remember { mutableStateOf(dietVm.calorieGoal.toString()) }

    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scroll = rememberScrollState()

    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scroll)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Settings ⚙️",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            // Profile
            SettingsCard(title = "Profile", subtitle = "Update your personal information") {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = weight,
                    onValueChange = { input -> weight = input.filter { c -> c.isDigit() } },
                    label = { Text("Weight (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        profileVm.update(name.trim(), email.trim(), weight.trim())
                        scope.launch { snackbar.showSnackbar("Profile saved") }
                    }
                ) { Text("Save Changes") }
            }

            // Water Goal
            SettingsCard(title = "Water Goal", subtitle = "Set your daily water intake goal") {
                OutlinedTextField(
                    value = waterGoalInput,
                    onValueChange = { input -> waterGoalInput = input.filter { c -> c.isDigit() } },
                    label = { Text("Daily Water Goal (ml)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        val v = waterGoalInput.toIntOrNull()
                        if (v != null && v > 0) {
                            waterVm.updateDailyGoal(v)
                            scope.launch { snackbar.showSnackbar("Water goal saved") }
                        } else {
                            scope.launch { snackbar.showSnackbar("Enter a valid ml value") }
                        }
                    }
                ) { Text("Save Water Goal") }
            }

            // Calorie Goal
            SettingsCard(title = "Calorie Goal", subtitle = "Set your daily calorie intake goal") {
                OutlinedTextField(
                    value = calorieGoalInput,
                    onValueChange = { input -> calorieGoalInput = input.filter { c -> c.isDigit() } },
                    label = { Text("Daily Calorie Goal (kcal)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        val v = calorieGoalInput.toIntOrNull()
                        if (v != null && v > 0) {
                            dietVm.updateCalorieGoal(v)
                            scope.launch { snackbar.showSnackbar("Calorie goal saved") }
                        } else {
                            scope.launch { snackbar.showSnackbar("Enter a valid kcal value") }
                        }
                    }
                ) { Text("Save Calorie Goal") }
            }

            // Account (Logout → backstack temizlenerek Login)
            SettingsCard(title = "Account") {
                Button(
                    onClick = {
                        authVm.signOut() // <<< düzeltildi
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                            restoreState = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) { Text("Logout") }
            }

            Spacer(Modifier.height(24.dp))
        }

        SnackbarHost(
            hostState = snackbar,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
        )
    }
}

@Composable
private fun SettingsCard(
    title: String,
    subtitle: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(Modifier.fillMaxWidth()) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            if (subtitle != null) {
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            content()
        }
    }
}
