package com.example.fitness_app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

enum class Difficulty { EASY, MEDIUM, HARD }

/** Liste elemanları property değişince recomposition olsun diye state ile tutulur */
class Exercise(
    val id: Int,
    val title: String,
    val description: String,
    val difficulty: Difficulty,
    done: Boolean = false,
    expanded: Boolean = false
) {
    var done by mutableStateOf(done)
    var expanded by mutableStateOf(expanded)
}

class FitnessViewModel : ViewModel() {
    val dailyGoal: Int = 9
    /** Günün tüm egzersizleri (Easy/Medium/Hard karışık) */
    val exercises = mutableStateListOf<Exercise>()

    init {
        // Basit örnek set (3x3 = 9)
        exercises.addAll(
            listOf(
                Exercise(1, "Light Walking", "Walk at a comfortable pace for 15–20 minutes.", Difficulty.EASY),
                Exercise(2, "Basic Stretching", "Full body stretches, 20–30 seconds each.", Difficulty.EASY),
                Exercise(3, "Gentle Yoga", "Breathing & flexibility focused light yoga.", Difficulty.EASY),

                Exercise(4, "20 Min Cardio", "Moderate intensity cardio (jog/cycle/elliptical).", Difficulty.MEDIUM),
                Exercise(5, "Bodyweight Squats", "3 x 15 reps with proper form.", Difficulty.MEDIUM),
                Exercise(6, "Push-ups", "3 x 10 reps; do knee push-ups if needed.", Difficulty.MEDIUM),

                Exercise(7, "HIIT Workout", "30s max effort / 30s rest; repeat 15–20m.", Difficulty.HARD),
                Exercise(8, "Weight Training", "Full-body session; choose challenging weights.", Difficulty.HARD),
                Exercise(9, "Advanced Circuit", "Full-body circuit with minimal rest.", Difficulty.HARD),
            )
        )
    }

    val completedCount: Int
        get() = exercises.count { it.done }

    fun byDifficulty(d: Difficulty) = exercises.filter { it.difficulty == d }

    fun toggleDone(id: Int) {
        exercises.firstOrNull { it.id == id }?.let { it.done = !it.done }
    }

    fun toggleExpanded(id: Int) {
        exercises.firstOrNull { it.id == id }?.let { it.expanded = !it.expanded }
    }
}
