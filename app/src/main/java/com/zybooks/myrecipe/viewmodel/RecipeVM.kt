package com.zybooks.myrecipe.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.zybooks.myrecipe.data.repository.Recipe
import com.zybooks.myrecipe.data.repository.RecipeRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecipeVM : ViewModel() {
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes = _recipes.asStateFlow()

    fun loadRecipes() {
        viewModelScope.launch {
            _recipes.value = RecipeRepo.getRecipes()
        }
    }

    fun addRecipe(title: String, ingredients: String, instructions: String) {
        viewModelScope.launch {
            val recipe = Recipe(
                title = title,
                ingredients = ingredients,
                instructions = instructions
            )
            val result = RecipeRepo.addRecipe(recipe)
            if (result.isSuccess) {
                loadRecipes()
            }
        }
    }

    fun updateRecipe(recipeId: String, title: String, ingredients: String, instructions: String) {
        viewModelScope.launch {
            val updatedRecipe = mapOf(
                "title" to title,
                "ingredients" to ingredients,
                "instructions" to instructions,
                "updatedAt" to System.currentTimeMillis()
            )
            val result = RecipeRepo.updateRecipe(recipeId, updatedRecipe)
            if (result.isSuccess) {
                loadRecipes()
            }
        }
    }

    fun deleteRecipe(recipeId: String) {
        viewModelScope.launch {
            RecipeRepo.deleteRecipe(recipeId)
            loadRecipes()
        }
    }

    fun toggleFavorite(recipeId: String, currentValue: Boolean) {
        val newValue = !currentValue

        FirebaseFirestore.getInstance()
            .collection("recipes")
            .document(recipeId)
            .update("favorite", newValue)
            .addOnSuccessListener {
                loadRecipes()   // refresh UI
            }
            .addOnFailureListener { e ->
                Log.e("RecipeVM", "Failed to update favorite", e)
            }
    }



    fun parseAiRecipe(aiText: String): Triple<String, String, String> {
        val lines = aiText.lines()

        val title = lines.firstOrNull()?.replace("#", "")?.trim().orEmpty()

        val ingredientsStart = lines.indexOfFirst { it.contains("Ingredients", ignoreCase = true) }
        val instructionsStart = lines.indexOfFirst { it.contains("Instructions", ignoreCase = true) }

        val ingredients = if (ingredientsStart != -1 && instructionsStart != -1) {
            lines.subList(ingredientsStart + 1, instructionsStart)
                .joinToString("\n")
                .trim()
        } else "No ingredients found"

        val instructions = if (instructionsStart != -1) {
            lines.subList(instructionsStart + 1, lines.size)
                .joinToString("\n")
                .trim()
        } else "No instructions found"

        return Triple(title, ingredients, instructions)
    }
}


