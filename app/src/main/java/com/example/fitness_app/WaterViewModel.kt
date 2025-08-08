package com.example.fitness_app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.fitness_app.model.WaterEntry

class WaterViewModel : ViewModel() {
    var dailyGoal by mutableStateOf(2500)      // <— ARTIK değişebilir
        private set

    val log = mutableStateListOf<WaterEntry>()

    val total: Int
        get() = log.sumOf { it.amount }

    fun add(amount: Int, time: String) {
        log.add(WaterEntry(amount = amount, time = time))
    }

    fun removeAt(index: Int) {
        if (index in log.indices) log.removeAt(index)
    }

    fun updateDailyGoal(newGoal: Int) {        // <— Settings’ten çağrılacak
        if (newGoal > 0) dailyGoal = newGoal
    }
}
