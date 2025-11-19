package com.zybooks.myrecipe.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

//    fun addRecipe(title: String?, ingredients: String, instructions: String) {
//        viewModelScope.launch {
//            val recipe = Recipe(
//                title = title,
//                ingredients = ingredients,
//                instructions = instructions
//            )
//            val result = RecipeRepo.addRecipe(recipe)
//            if (result.isSuccess) {
//                loadRecipes()
//            }
//        }
//    }

    fun addRecipe(title: String, markdown: String) {
        viewModelScope.launch {
            val recipe = Recipe(
                title = title,
                markdown = markdown
            )
            RecipeRepo.addRecipe(recipe)
            loadRecipes()
        }
    }

    fun updateTitle(recipeId: String, newTitle: String) {
        viewModelScope.launch {
            RecipeRepo.updateRecipeTitle(recipeId, newTitle)
            loadRecipes()
        }
    }

    fun deleteRecipe(recipeId: String) {
        viewModelScope.launch {
            RecipeRepo.deleteRecipe(recipeId)
            loadRecipes()
        }
    }


    fun toggleFavorite(recipeId: String, currentValue: Boolean) {
        viewModelScope.launch {
            RecipeRepo.toggleFavorite(recipeId, !currentValue)
            loadRecipes()
        }
    }

//    fun parseAiRecipe(aiText: String): Triple<String, String, String> {
//        val lines = aiText.lines()
//
//        val title = lines.firstOrNull{it.startsWith("#")}
//            ?.replace("#", "")
//            ?.trim()
//            .orEmpty()
//
//        val ingredientsStart = lines.indexOfFirst {
//            it.contains("ingredient", ignoreCase = true)
//        }
//        val instructionsStart = lines.indexOfFirst {
//            it.contains("instruction", ignoreCase = true)
//        }
//
//        val ingredients = if (ingredientsStart != -1 && instructionsStart != -1) {
//            lines.subList(ingredientsStart + 1, instructionsStart)
//                .joinToString("\n")
//        } else "No ingredients found"
//
//        val instructions = if (instructionsStart != -1) {
//            lines.subList(instructionsStart + 1, lines.size)
//                .joinToString("\n")
//        } else "No instructions found"
//
//        return Triple(title, ingredients, instructions)
//    }
}
