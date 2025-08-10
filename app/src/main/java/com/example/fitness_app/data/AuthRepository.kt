package com.example.fitness_app.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

/**
 * FirebaseAuth tabanlı kimlik doğrulama katmanı.
 * Tüm public fonksiyonlar Result<Unit> döner: success/failure kolay yönetilir.
 */
class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    val isLoggedIn: Boolean
        get() = auth.currentUser != null

    fun currentUser(): FirebaseUser? = auth.currentUser

    /** Email/Şifre ile giriş */
    suspend fun signIn(email: String, password: String): Result<Unit> = runCatching {
        auth.signInWithEmailAndPassword(email.trim(), password).await()
        Unit
    }

    /**
     * Kayıt ol.
     * Başarılı olursa ileride yazacağımız ProfileRepository ile default profil oluşturacağız.
     * Şu an createDefault burada çağrılıyor — bir sonraki adımda ProfileRepository’yi ekleyeceğiz.
     */
    suspend fun signUp(
        name: String,
        email: String,
        password: String,
        weightKg: Int
    ): Result<Unit> = runCatching {
        auth.createUserWithEmailAndPassword(email.trim(), password).await()
        // Profil belgesini oluştur (bir sonraki adımda bu metoda gerçek gövdeyi yazacağız)
        val uid = auth.currentUser?.uid ?: error("User not available after signUp")
        try {
            ProfileRepository().createDefault(uid, name.trim(), email.trim(), weightKg).getOrThrow()
        } catch (t: Throwable) {
            // Profil oluşturma başarısızsa kullanıcıyı yine de login bırakıyoruz,
            // ama hatayı üst katmana iletelim (isteğe göre burada signOut() yapılabilir)
            throw t
        }
        Unit
    }

    /** Şifre sıfırlama e-postası gönder */
    suspend fun sendPasswordReset(email: String): Result<Unit> = runCatching {
        auth.sendPasswordResetEmail(email.trim()).await()
        Unit
    }

    /** Çıkış yap */
    fun signOut() {
        auth.signOut()
    }
}
