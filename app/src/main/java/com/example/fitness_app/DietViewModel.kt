package com.example.fitness_app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.fitness_app.model.FoodItem

class DietViewModel : ViewModel() {
    var calorieGoal by mutableStateOf(2000)     // <— ARTIK değişebilir
        private set

    val foodLog = mutableStateListOf<FoodItem>()

    val totalCalories: Int
        get() = foodLog.sumOf { it.calories }

    fun addFood(item: FoodItem) { foodLog += item }
    fun removeAt(index: Int) { if (index in foodLog.indices) foodLog.removeAt(index) }

    fun updateCalorieGoal(newGoal: Int) {       // <— Settings’ten çağrılacak
        if (newGoal > 0) calorieGoal = newGoal
    }
}
