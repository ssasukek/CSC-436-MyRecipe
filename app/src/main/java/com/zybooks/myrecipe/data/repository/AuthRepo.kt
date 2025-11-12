package com.zybooks.myrecipe.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object AuthRepo {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    //Register a new user with email & password
    suspend fun registerUser(email: String, password: String, username: String): Result<Unit> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("No user ID found."))

            val userData = mapOf(
                "email" to email,
                "username" to username,
                "uid" to uid,
                "createdAt" to System.currentTimeMillis()
            )
            db.collection("users").document(uid).set(userData).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //Login existing user
    suspend fun loginUser(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //Logout the current user
    fun logout() {
        auth.signOut()
    }

    //Check if user is currently logged in
    fun getCurrentUser() = auth.currentUser
}
