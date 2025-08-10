package com.example.fitness_app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitness_app.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(repo.isLoggedIn)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun login(email: String, password: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _loading.value = true
            repo.signIn(email, password)
                .onSuccess {
                    _isLoggedIn.value = true
                    onSuccess()
                }
                .onFailure { _error.value = it.localizedMessage }
            _loading.value = false
        }
    }

    fun register(name: String, email: String, password: String, weightKg: Int, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _loading.value = true
            repo.signUp(name, email, password, weightKg)
                .onSuccess {
                    _isLoggedIn.value = true
                    onSuccess()
                }
                .onFailure { _error.value = it.localizedMessage }
            _loading.value = false
        }
    }

    fun sendReset(email: String) {
        viewModelScope.launch {
            _loading.value = true
            repo.sendPasswordReset(email)
                .onFailure { _error.value = it.localizedMessage }
            _loading.value = false
        }
    }

    fun logout() {
        repo.signOut()
        _isLoggedIn.value = false
    }

    fun consumeError() { _error.value = null }
}
