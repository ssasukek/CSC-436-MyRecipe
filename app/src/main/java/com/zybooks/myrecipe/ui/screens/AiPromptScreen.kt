package com.zybooks.myrecipe.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.zybooks.myrecipe.viewmodel.RecipeVM
import kotlinx.coroutines.launch
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiPromptScreen(navController: NavController, viewModel: RecipeVM = viewModel()) {
    var userPrompt by remember { mutableStateOf("") }
    var aiResponse by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Start by instantiating a GenerativeModel and specifying the model name:
    val model = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel("gemini-2.5-flash")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Make your Recipe With Ai") },
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .heightIn(min = 2000.dp)
        ) {
            OutlinedTextField(
                value = userPrompt,
                onValueChange = { userPrompt = it },
                label = { Text("Ask me to create any recipe you want!") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        aiResponse = try{
                            val result = model.generateContent(
                                "Generate a clear recipe with title, ingredients, and steps. Request: $userPrompt"
                            )
                            result.text ?: "No response from Gemini"
                        }catch (e: Exception){
                            "Error: ${e.message}"
                        }
                        isLoading = false
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Generating Recipe")
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            if (aiResponse.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                RichText {
                    Markdown(aiResponse)
                }
                Button(
                    onClick = {
                        val (title, ingredients, instructions) = viewModel.parseAiRecipe(aiResponse)

                        viewModel.addRecipe(
                            title = title,
                            ingredients = ingredients,
                            instructions = instructions
                        )
                        navController.navigate("recipes"){
                            popUpTo("ai_prompt"){
                                inclusive = true
                            }
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "Save Recipe",
                        style = TextStyle(fontSize = 16.sp, color = Color.DarkGray)
                    )
                }
            }
        }
    }
}

