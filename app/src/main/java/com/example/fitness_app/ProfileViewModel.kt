package com.example.fitness_app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitness_app.data.AuthRepository
import com.example.fitness_app.data.Profile
import com.example.fitness_app.data.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val auth: AuthRepository = com.example.fitness_app.data.AuthRepository(),
    private val repo: ProfileRepository = ProfileRepository()
) : ViewModel() {

    // UI’de doğrudan kullanıyorsun: Home/Settings
    var name: String = "Demo User"; private set
    var email: String = "demouser@gmail.com"; private set
    var weightKg: Int = 70; private set

    // İsteyen ekranlar için stateflow da sunalım
    private val _profile = MutableStateFlow(Profile(name, email, weightKg))
    val profile: StateFlow<Profile> = _profile

    init {
        val uid = auth.currentUser()?.uid
        if (uid != null) {
            // Firestore’u dinle ve alanları güncelle
            viewModelScope.launch {
                repo.observeProfile(uid).collect { p ->
                    name = p.name
                    email = p.email
                    weightKg = p.weightKg
                    _profile.update { p }
                }
            }
        }
    }

    fun update(newName: String, newEmail: String, weightStr: String) {
        val w = weightStr.toIntOrNull() ?: weightKg
        val uid = auth.currentUser()?.uid ?: return
        viewModelScope.launch {
            repo.update(uid, newName, newEmail, w)
            // Optimistic update (listener zaten geri yazacak)
            name = newName; email = newEmail; weightKg = w
            _profile.update { Profile(name, email, weightKg) }
        }
    }
}
