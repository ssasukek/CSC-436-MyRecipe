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

    // Loading and error states
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun loadRecipes() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = RecipeRepo.getRecipes()
            _isLoading.value = false

            if (result.isSuccess) {
                _recipes.value = result.getOrNull() ?: emptyList()
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun addRecipe(title: String, ingredients: String, instructions: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val newRecipe = Recipe(title = title, ingredients = ingredients, instructions = instructions)
            val result = RecipeRepo.addRecipe(newRecipe)
            _isLoading.value = false

            if (result.isSuccess) {
                loadRecipes() // Refresh after adding
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun updateRecipe(recipe: Recipe) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = RecipeRepo.updateRecipe(recipe)
            _isLoading.value = false

            if (result.isSuccess) {
                loadRecipes()
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun deleteRecipe(recipeId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = RecipeRepo.deleteRecipe(recipeId)
            _isLoading.value = false

            if (result.isSuccess) {
                loadRecipes()
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }
}
