//package com.zybooks.myrecipe.ui.screens
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material3.Button
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import androidx.navigation.compose.rememberNavController
//import com.zybooks.myrecipe.data.local.AppDatabase
//import com.zybooks.myrecipe.data.model.Recipe
//import com.zybooks.myrecipe.viewmodel.RecipeVM
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AddRecipeScreen(navController: NavController, viewModel: RecipeVM = viewModel()) {
//    var title by remember { mutableStateOf("") }
//    var ingredients by remember { mutableStateOf("") }
//    var instructions by remember { mutableStateOf("") }
//    val isLoading by viewModel.isLoading.collectAsState()
//    val errorMessage by viewModel.errorMessage.collectAsState()
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Add New Recipe") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(
//                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                            contentDescription = "Back"
//                        )
//                    }
//                }
//            )
//        }
//    ) {
//        innerPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .padding(16.dp),
//            verticalArrangement = Arrangement.Top,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ){
//            Text(
//                "Paste a recipe URL:",
//                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//
//            OutlinedTextField(
//                value = title,
//                onValueChange = { title = it },
//                label = { Text("Recipe Title") },
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            Button(
//                onClick = {
//                    if (recipeUrl.isNotBlank()) {
//                        isLoading = true
//                        showSuccess = false
//
//                        // replace after backend
//                        val extractedTitle = extractTitleFromUrl(recipeUrl)
//                        val extractedInstructions = extractedInstructionsFromUrl(recipeUrl)
//
//                        AppDatabase.addRecipe(
//                            Recipe(
//                                id = AppDatabase.recipeList.size + 1,
//                                title = extractedTitle,
//                                instruction = extractedInstructions
//                            )
//                        )
//                        isLoading = false
//                        showSuccess = true
//
//                        navController.navigate("recipes") {
//                            popUpTo("add_recipe") {
//                                inclusive = true
//                            }
//                        }
//                    }
//                },
//            ){
//                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Recipe")
//                Text("Add Recipe")
//            }
//            if (isLoading) {
//                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
//            }
//
//            if (showSuccess) {
//                Text(
//                    "Recipe added successfully!",
//                    color = MaterialTheme.colorScheme.primary)
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun AddRecipeScreenPreview() {
//    val navController = rememberNavController()
//    AddRecipeScreen(navController)
//}
//
//// Temporary logic — replace later with your backend parsing
//private fun extractTitleFromUrl(url: String): String {
//    val domain = url.substringAfter("://").substringBefore("/")
//    return "Recipe from $domain"
//}
//
//private fun extractedInstructionsFromUrl(url: String): String {
//    return "Instructions extracted from: $url"
//}
