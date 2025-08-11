package com.example.fitness_app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitness_app.data.AuthRepository
import com.example.fitness_app.data.Profile
import com.example.fitness_app.data.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val auth: AuthRepository = AuthRepository(),
    private val repo: ProfileRepository = ProfileRepository()
) : ViewModel() {

    // Settings gibi yerlerde başlangıç/varsayılan olarak kullanılıyor
    var name: String = "User";        private set
    var email: String = "";           private set
    var weightKg: Int = 70;           private set

    // HomeScreen gibi yerler için reaktif akış
    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile

    init {
        val uid = auth.currentUser()?.uid
        if (uid != null) {
            // Firestore’daki profil belgesini canlı dinle
            viewModelScope.launch {
                repo.observeProfile(uid).collect { p ->
                    // Repo Profile döndürüyorsa burası tetiklenir
                    name = p.name
                    email = p.email
                    weightKg = p.weightKg
                    _profile.value = p
                }
            }
        } else {
            // Giriş yoksa en azından defaults’u yayınla
            _profile.value = Profile(name, email, weightKg)
        }
    }

    /**
     * Settings’teki "Save Changes" için.
     * Repo API’ne göre `update(uid, ...)` kullanıyorum; eğer sende `updateProfile(...)`
     * gibi uid istemeyen bir sürüm varsa ona göre uyarlayabilirsin.
     */
    fun update(newName: String, newEmail: String, weightStr: String) {
        val uid = auth.currentUser()?.uid ?: return
        val w = weightStr.toIntOrNull() ?: weightKg

        viewModelScope.launch {
            repo.update(uid, newName, newEmail, w)
            // Optimistic update — snapshot listener hemen ardından gerçek değeri yazacak
            name = newName
            email = newEmail
            weightKg = w
            _profile.value = Profile(name, email, weightKg)
        }
    }
}
