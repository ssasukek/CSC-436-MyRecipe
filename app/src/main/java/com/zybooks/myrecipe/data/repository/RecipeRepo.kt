package com.zybooks.myrecipe.data.repository

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
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun userRecipesCollection() =
        db.collection("users")
            .document(auth.currentUser?.uid ?: throw Exception("User not logged in"))
            .collection("recipes")

    suspend fun addRecipe(recipe: Recipe): Result<Unit> {
        return try {
            val docRef = userRecipesCollection().document()
            val newRecipe = recipe.copy(id = docRef.id)
            docRef.set(newRecipe).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRecipes(): Result<List<Recipe>> {
        return try {
            val snapshot = userRecipesCollection().get().await()
            val recipes = snapshot.toObjects(Recipe::class.java)
            Result.success(recipes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateRecipe(recipe: Recipe): Result<Unit> {
        return try {
            userRecipesCollection()
                .document(recipe.id)
                .set(recipe.copy(updatedAt = System.currentTimeMillis()))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteRecipe(recipeId: String): Result<Unit> {
        return try {
            userRecipesCollection().document(recipeId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
