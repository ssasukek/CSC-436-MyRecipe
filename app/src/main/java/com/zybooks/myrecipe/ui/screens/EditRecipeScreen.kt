package com.zybooks.myrecipe.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zybooks.myrecipe.viewmodel.RecipeVM
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecipeScreen(
    navController: NavController,
    recipeId: String,
    viewModel: RecipeVM = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadRecipes()
    }

    val recipes by viewModel.recipes.collectAsState()

    if (recipes.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val recipe = recipes.find { it.id == recipeId } ?: return
    var newTitle by remember { mutableStateOf(recipe.title) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Recipe") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Title", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = newTitle,
                onValueChange = { newTitle = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.updateTitle(recipeId, newTitle)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    viewModel.deleteRecipe(recipeId)
                    navController.navigate("recipes") {
                        popUpTo("recipes") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete Recipe")
            }
        }
    }
}

