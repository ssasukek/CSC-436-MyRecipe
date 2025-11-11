package com.zybooks.myrecipe.data.local

import com.zybooks.myrecipe.data.model.Recipe

object AppDatabase {
    val recipeList = mutableListOf<Recipe>()

    fun addRecipe(recipe: Recipe) {
        recipeList.add(recipe)
    }

    fun getAllRecipes(): List<Recipe> = recipeList

    fun getRecipeById(id: Int): Recipe? = recipeList.find { it.id == id }
}
