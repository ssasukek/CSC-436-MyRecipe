package com.zybooks.myrecipe.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zybooks.myrecipe.ui.screens.LoadingScreen
import com.zybooks.myrecipe.ui.screens.LoginScreen
import com.zybooks.myrecipe.ui.screens.RecipeListScreen
import com.zybooks.myrecipe.ui.screens.RegisterScreen

//import com.zybooks.myrecipe.ui.screens.*

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
//        composable("recipe_detail") {
//            RecipeDetailScreen(navController)
//        }
//        composable("add_recipe") {
//            AddRecipeScreen(navController)
//        }
//        composable("ai_prompt") {
//            AiPromptScreen(navController)
//        }
    }
}

@Composable
fun RegisterScreen(x0: NavHostController) {
    TODO("Not yet implemented")
}
