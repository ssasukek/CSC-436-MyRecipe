package com.zybooks.myrecipe.ui.screens

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zybooks.myrecipe.data.repository.Recipe
import com.zybooks.myrecipe.viewmodel.ProfileVM
import com.zybooks.myrecipe.viewmodel.RecipeVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(navController: NavController, viewModel: RecipeVM = viewModel()) {
    val profileVM: ProfileVM = viewModel()

    val recipes by viewModel.recipes.collectAsState()
    val user by profileVM.user.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadRecipes()
        profileVM.loadUser()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Recipes")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("ai_prompt") }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "AI Recipe"
                        )
                    }
                    user?.username?.let {
                        Text(it, style = MaterialTheme.typography.bodyMedium)
                    }
                    IconButton(onClick = { navController.navigate("profile") }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_recipe") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Recipe"
                )
            }
        }
    ) { innerPadding ->
        if (recipes.isEmpty()) {
            Text(
                text = "No recipes found.",
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                items(recipes) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        onClick = { navController.navigate(
                            "recipe_detail/${recipe.id}"
                        ) }
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeCard(
    recipe: Recipe,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Text(
            text = recipe.title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeListScreenPreview() {
    RecipeListScreen(navController = rememberNavController())
}