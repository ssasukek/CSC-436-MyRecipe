package com.zybooks.myrecipe.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zybooks.myrecipe.ui.screens.AddRecipeScreen
import com.zybooks.myrecipe.ui.screens.AiPromptScreen
import com.zybooks.myrecipe.ui.screens.EditRecipeScreen
import com.zybooks.myrecipe.ui.screens.LoadingScreen
import com.zybooks.myrecipe.ui.screens.LoginScreen
import com.zybooks.myrecipe.ui.screens.ProfileScreen
import com.zybooks.myrecipe.ui.screens.RecipeDetailScreen
import com.zybooks.myrecipe.ui.screens.RecipeListScreen
import com.zybooks.myrecipe.ui.screens.RegisterScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "loading" // First screen to show
    ) {
        composable("loading") {
            LoadingScreen(navController)
        }

        composable("login") {
            LoginScreen(navController)
        }

        composable("register") {
            RegisterScreen(navController)
        }
        composable("recipes") {
            RecipeListScreen(navController)
        }
        composable("add_recipe") {
            AddRecipeScreen(navController)
        }
        composable("ai_prompt") {
            AiPromptScreen(navController)
        }

        composable("edit_recipe/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            EditRecipeScreen(navController, id)
        }
        composable("recipe_detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            RecipeDetailScreen(navController, id)
        }

        composable("profile"){
            ProfileScreen(navController)
        }
    }
}
