package com.example.fitness_app

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.fitness_app.model.FoodItem

class DietViewModel : ViewModel() {
    val calorieGoal = 2000
    val foodLog = mutableStateListOf<FoodItem>()

    val totalCalories: Int
        get() = foodLog.sumOf { it.calories }

    fun addFood(item: FoodItem) {
        foodLog += item
    }

    fun removeAt(index: Int) {
        if (index in foodLog.indices) foodLog.removeAt(index)
    }
}
