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
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.zybooks.myrecipe.viewmodel.RecipeVM
import kotlinx.coroutines.launch
import com.zybooks.myrecipe.data.model.RecipeExtractor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(
    navController: NavController,
    viewModel: RecipeVM = viewModel()
) {
    var recipeUrl by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    // Start by instantiating a GenerativeModel and specifying the model name:
    val model = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel("gemini-2.5-flash")

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
                            errorMessage = null
                            try {
                                val html = RecipeExtractor.fetchHtml(recipeUrl)
                                val text = RecipeExtractor.htmlToText(html)

                                val prompt = """
                                    You are a recipe extractor. 
                                    
                                    From the text below, extract ONE recipe only.
                                    Ignore everything unrelated.
                                    
                                    ONly return the result as markdown in this structure:
                                    
                                    # Recipe Title
                                    ## Ingredients
                                    - item 1
                                    - item 2
                                    ## Instructions
                                    1. step 1
                                    2. step 2
                                    Here is the page text:
                                    $text
                                """.trimIndent()

                                val result = model.generateContent(prompt)
                                val markdown = result.text ?: throw Exception("No response from Gemini")

                                val title = markdown
                                    .lineSequence()
                                    .firstOrNull { it.startsWith("#") }
                                    ?.removePrefix("#")
                                    ?.trim()
                                    .takeUnless { it.isNullOrEmpty() }
                                    ?: "Untitled"

                                viewModel.addRecipe(
                                    title = title,
                                    markdown = markdown
                                )

                                navController.navigate("recipes") {
                                    popUpTo("add_recipe") { inclusive = true }
                                }
                            } catch (e: Exception) {
                                errorMessage = e.message ?: "Something went wrong"
                            } finally {
                                isLoading = false
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

            errorMessage?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddRecipeScreenPreview() {
    AddRecipeScreen(navController = rememberNavController())
}
