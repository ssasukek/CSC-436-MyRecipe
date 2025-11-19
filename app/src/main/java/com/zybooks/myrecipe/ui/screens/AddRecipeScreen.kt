package com.zybooks.myrecipe.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.zybooks.myrecipe.viewmodel.RecipeVM
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(
    navController: NavController,
    viewModel: RecipeVM = viewModel()
) {
    var recipeUrl by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Recipe from URL") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Paste a recipe URL below:",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = recipeUrl,
                onValueChange = { recipeUrl = it },
                label = { Text("Recipe URL") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    if (recipeUrl.isNotBlank()) {
                        scope.launch {
                            isLoading = true
                            showSuccess = false

                            // Temporary logic until AI or backend scraper
                            val extractedTitle = extractTitleFromUrl(recipeUrl)
                            val extractedIngredients = extractIngredientsFromUrl(recipeUrl)
                            val extractedInstructions = extractInstructionsFromUrl(recipeUrl)

//                            viewModel.addRecipe(
//                                title = extractedTitle,
//                                ingredients = extractedIngredients,
//                                instructions = extractedInstructions
//                            )

                            isLoading = false
                            showSuccess = true

                            // Go back to recipe list
                            navController.navigate("recipes") {
                                popUpTo("add_recipe") { inclusive = true }
                            }
                        }
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Recipe")
                Spacer(Modifier.width(8.dp))
                Text("Add Recipe")
            }

            if (isLoading) {
                Spacer(Modifier.height(16.dp))
                CircularProgressIndicator()
            }

            if (showSuccess) {
                Spacer(Modifier.height(8.dp))
                Text("Recipe added successfully!", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddRecipeScreenPreview() {
    AddRecipeScreen(navController = rememberNavController())
}

private fun extractTitleFromUrl(url: String): String {
    val domain = url.substringAfter("://").substringBefore("/")
    return "Recipe from $domain"
}

private fun extractIngredientsFromUrl(url: String): String {
    return "Ingredients parsed from: $url"
}

private fun extractInstructionsFromUrl(url: String): String {
    return "Instructions parsed and simplified from: $url"
}
