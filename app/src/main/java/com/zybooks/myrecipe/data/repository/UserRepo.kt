package com.zybooks.myrecipe.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.zybooks.myrecipe.data.model.AppUserData
import kotlinx.coroutines.tasks.await

object UserRepo {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun getUser(): AppUserData? {
        val uid = auth.currentUser?.uid ?: return null
        val doc = db.collection("users").document(uid).get().await()
        return doc.toObject(AppUserData::class.java)
    }

    suspend fun updateUsername(newUsername: String) {
        val uid = auth.currentUser?.uid ?: return

        db.collection("users")
            .document(uid)
            .update("username", newUsername)
            .await()
    }

    suspend fun changePassword(newPassword: String): Boolean {
        return try {
            auth.currentUser?.updatePassword(newPassword)?.await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
