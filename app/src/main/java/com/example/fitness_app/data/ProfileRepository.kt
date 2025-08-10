package com.example.fitness_app.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/** Basit profil modeli */
data class Profile(
    val name: String = "",
    val email: String = "",
    val weightKg: Int = 70
)

class ProfileRepository {
    private val db = FirebaseFirestore.getInstance()
    private fun doc(uid: String) = db.collection("users").document(uid)

    /** Register sonrası varsayılan profil dokümanı oluşturur */
    suspend fun createDefault(
        uid: String,
        name: String,
        email: String,
        weightKg: Int
    ): Result<Unit> = runCatching {
        val profile = Profile(name = name, email = email, weightKg = weightKg)
        doc(uid).set(profile).await()
        Unit
    }

    /** Profili güncelle */
    suspend fun update(
        uid: String,
        name: String,
        email: String,
        weightKg: Int
    ): Result<Unit> = runCatching {
        val profile = Profile(name = name, email = email, weightKg = weightKg)
        doc(uid).set(profile).await()
        Unit
    }

    /** Profili dinle (opsiyonel – Home/Settings’te kullanışlı) */
    fun observeProfile(uid: String): Flow<Profile> = callbackFlow {
        val reg = doc(uid).addSnapshotListener { snap, err ->
            if (err != null) {
                close(err); return@addSnapshotListener
            }
            val p = snap?.toObject(Profile::class.java) ?: Profile()
            trySend(p)
        }
        awaitClose { reg.remove() }
    }
}
