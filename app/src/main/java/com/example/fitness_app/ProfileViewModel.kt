package com.example.fitness_app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {
    var name by mutableStateOf("Demo User")
        private set
    var email by mutableStateOf("demouser@gmail.com")
        private set
    var weightKg by mutableStateOf(70)
        private set

    fun update(name: String, email: String, weightStr: String) {
        this.name = name.ifBlank { "User" }
        this.email = email
        this.weightKg = weightStr.toIntOrNull() ?: weightKg
    }
}
