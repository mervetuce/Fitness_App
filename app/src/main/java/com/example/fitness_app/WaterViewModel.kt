package com.example.fitness_app

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitness_app.data.AuthRepository
import com.example.fitness_app.data.WaterRepository
import com.example.fitness_app.model.WaterEntry
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WaterViewModel(
    private val auth: AuthRepository = com.example.fitness_app.data.AuthRepository(),
    private val repo: WaterRepository = WaterRepository()
) : ViewModel() {

    // UI’nin doğrudan kullandığı liste
    val log = mutableStateListOf<WaterEntry>()

    // hedef (Settings’ten değiştiriyorsun)
    var dailyGoal: Int = 2500
        private set

    val total: Int
        get() = log.sumOf { it.amount }

    private var listenJob: Job? = null

    init {
        startListen()
    }

    private fun startListen() {
        val uid = auth.currentUser()?.uid ?: return
        listenJob?.cancel()
        listenJob = viewModelScope.launch {
            repo.observeToday(uid).collect { list ->
                log.clear()
                log.addAll(list)
            }
        }
    }

    /** UI bu imzayı çağırıyor: time parametresi veriyor; repo’ya geçiriyoruz */
    fun add(amount: Int, time: String) {
        val uid = auth.currentUser()?.uid ?: return
        viewModelScope.launch {
            // repo time'ı da alacak şekilde yazıldı
            repo.add(uid, amount, time)
        }
    }

    /** UI index bazlı siliyor; Firestore için id’ye çeviriyoruz */
    fun removeAt(index: Int) {
        val uid = auth.currentUser()?.uid ?: return
        if (index !in log.indices) return
        val id = log[index].id
        viewModelScope.launch {
            if (id != null) {
                repo.delete(uid, id)
            } else {
                // id yoksa (lokal fallback) sadece listeden temizle
                log.removeAt(index)
            }
        }
    }

    fun updateDailyGoal(v: Int) {
        dailyGoal = v
        // istersen burada Firestore’a da kalıcı yazım ekleyebiliriz (users/{uid}/settings)
    }

    /** Kolay kullanım: zaman üretip ekle */
    fun addQuick(amount: Int) {
        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        add(amount, time)
    }
}
