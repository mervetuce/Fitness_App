package com.example.fitness_app.data

import com.example.fitness_app.model.WaterEntry
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WaterRepository {
    private val db = FirebaseFirestore.getInstance()
    private fun col(uid: String) = db.collection("users").document(uid).collection("waterLogs")

    private fun today(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    fun observeToday(uid: String): Flow<List<WaterEntry>> = callbackFlow {
        val reg = col(uid)
            .whereEqualTo("date", today())
            .orderBy("time") // HH:mm string sıralaması gün içinde doğru
            .addSnapshotListener { qs, err ->
                if (err != null) { close(err); return@addSnapshotListener }
                val list = qs?.documents?.map { doc ->
                    val item = doc.toObject(WaterEntry::class.java)
                    // modelinizde id alanı yoksa nullable bırakıyoruz
                    item?.copy(id = doc.id) ?: WaterEntry()
                }.orEmpty()
                trySend(list)
            }
        awaitClose { reg.remove() }
    }

    suspend fun add(uid: String, amount: Int, time: String): Result<Unit> = runCatching {
        val data = WaterEntry(amount = amount, time = time, date = today())
        col(uid).add(data).await()
        Unit
    }

    suspend fun delete(uid: String, id: String): Result<Unit> = runCatching {
        col(uid).document(id).delete().await()
        Unit
    }
}
