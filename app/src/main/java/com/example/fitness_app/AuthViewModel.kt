package com.example.fitness_app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitness_app.data.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    val isLoggedIn: Boolean get() = repo.isLoggedIn

    suspend fun signIn(email: String, password: String) =
        withContext(Dispatchers.IO) { repo.signIn(email, password) }

    suspend fun signUp(name: String, email: String, password: String, weightKg: Int) =
        withContext(Dispatchers.IO) { repo.signUp(name, email, password, weightKg) }

    suspend fun reset(email: String) =
        withContext(Dispatchers.IO) { repo.sendPasswordReset(email) }

    fun signOut() = repo.signOut()
}
