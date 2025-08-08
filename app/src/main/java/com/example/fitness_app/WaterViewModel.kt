package com.example.fitness_app

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.fitness_app.model.WaterEntry   // <-- DOĞRU IMPORT

class WaterViewModel : ViewModel() {
    val dailyGoal: Int = 2500  // ml
    // Günlük su kayıtları (UI otomatik güncellenir)
    val log = mutableStateListOf<WaterEntry>()

    val total: Int
        get() = log.sumOf { entry -> entry.amount }

    fun add(amount: Int, time: String) {
        // += yerine add kullanıyoruz
        log.add(WaterEntry(amount = amount, time = time))
    }

    fun removeAt(index: Int) {
        if (index in log.indices) {
            log.removeAt(index)
        }
    }
}
