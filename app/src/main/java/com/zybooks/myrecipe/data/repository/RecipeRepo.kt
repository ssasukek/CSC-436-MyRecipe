package com.zybooks.myrecipe.data.repository

import android.annotation.SuppressLint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class Recipe(
    val id: String = "",
    val title: String = "",
    val ingredients: String = "",
    val instructions: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

object RecipeRepo {
    @SuppressLint("StaticFieldLeak")
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun addRecipe(recipe: Recipe): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid ?: return Result.failure(Exception("Error"))
            db.collection("users").document(userId)
                .collection("recipes")
                .add(recipe)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRecipes(): List<Recipe> {
        val userId = auth.currentUser?.uid ?: return emptyList()
        val snapshot = db.collection("users").document(userId)
            .collection("recipes")
            .get()
            .await()
        return snapshot.documents.map { doc ->
            val data = doc.toObject(Recipe::class.java)
            data!!.copy(id = doc.id)
        }
    }

    suspend fun updateRecipe(recipeId: String, updatedData: Map<String, Any>): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid ?: return Result.failure(Exception("Error"))
            db.collection("users").document(userId)
                .collection("recipes")
                .document(recipeId)
                .update(updatedData)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteRecipe(recipeId: String) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId)
            .collection("recipes").document(recipeId).delete().await()
    }
}
