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
    val favorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

object RecipeRepo {
    @SuppressLint("StaticFieldLeak")
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun userRecipesCollection() =
        db.collection("users")
            .document(auth.currentUser!!.uid)
            .collection("recipes")

    suspend fun addRecipe(recipe: Recipe): Result<Unit> {
        return try {
            userRecipesCollection().add(recipe).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRecipes(): List<Recipe> {
        val snapshot = userRecipesCollection().get().await()
        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(Recipe::class.java)?.copy(id = doc.id)
        }
    }

    suspend fun updateRecipeTitle(id: String, newTitle: String) {
        userRecipesCollection()
            .document(id)
            .update("title", newTitle, "updatedAt", System.currentTimeMillis())
            .await()
    }

    suspend fun deleteRecipe(id: String) {
        userRecipesCollection()
            .document(id)
            .delete()
            .await()
    }

    suspend fun toggleFavorite(id: String, newValue: Boolean) {
        userRecipesCollection()
            .document(id)
            .update("favorite", newValue)
            .await()
    }
}
