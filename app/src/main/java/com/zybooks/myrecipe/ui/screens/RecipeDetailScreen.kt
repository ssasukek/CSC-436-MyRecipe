package com.zybooks.myrecipe.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.halilibo.richtext.ui.RichText
import com.zybooks.myrecipe.viewmodel.RecipeVM
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    navController: NavController,
    recipeId: String,
    viewModel: RecipeVM = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadRecipes()
    }

    val recipeList by viewModel.recipes.collectAsState()
    val recipe = recipeList.find { it.id == recipeId }
    if (recipe == null) {
        Text("Recipe not found")
        return
    }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(recipe.title) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            type = "text/plain"
                            putExtra(
                                Intent.EXTRA_TEXT,
                                recipe.title
                            )
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Share"
                        )
                    }
                    IconButton(onClick = {
                        viewModel.toggleFavorite(recipe.id, recipe.favorite)
                    }) {
                        Icon(
                            imageVector = if (recipe.favorite){
                                Icons.Filled.Favorite
                            } else {
                                Icons.Outlined.FavoriteBorder
                            },
                            contentDescription = "Favorite",
                            tint = if (recipe.favorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = {
                        navController.navigate("edit_recipe/${recipe.id}")
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit"
                        )
                    }
                }
            )
        }
    ) {
    innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
        ){
            item {
                Text(
                    recipe.title,
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(Modifier.height(16.dp))
            }

            item {
                RichText {
                    Markdown(recipe.markdown)
                }
            }

        item {
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Back To Recipes")
            }
        }
    }
}
}

//@Composable
//fun IngredientsCard(ingredientsText: String) {
//    var servings by remember { mutableIntStateOf(4) }
//    val scale = servings / 4f
//
//    val lines = ingredientsText
//        .lines()
//        .filter { it.isNotBlank() }
//
//    fun scaleNum(line: String): String {
//        val regex = Regex("""(\d+(\.\d+)?)""")
//        return line.replace(regex) { match ->
//            val num = match.value.toFloatOrNull() ?: return@replace match.value
//            val scaled = num * scale
//            if (scaled % 1 == 0f) scaled.toInt().toString() else "%.1f".format(scaled)
//        }
//    }
//
//    Card(
//        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant),
//    ) {
//        Column(Modifier.padding(18.dp)) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Text("Servings:", style = MaterialTheme.typography.titleMedium)
//
//                IconButton(onClick = { if (servings > 1) servings-- }) {
//                    Text("-", style = MaterialTheme.typography.headlineMedium)
//                }
//                Text("$servings", style = MaterialTheme.typography.titleLarge)
//                IconButton(onClick = { servings++ }) {
//                    Text("+", style = MaterialTheme.typography.headlineSmall)
//                }
//            }
//            Spacer(Modifier.height(5.dp))
//
//            lines.forEach { raw ->
//                val line = raw.trim()
//
//                when {
//                    //Header
//                    line.startsWith("###") -> {
//                        Text(
//                            text = line.removePrefix("###").removeSuffix(":").trim(),
//                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
//                        )
//                        Spacer(Modifier.height(8.dp))
//                    }
//
//                    //Bullet
//                    line.startsWith("* ") -> {
//                        Text("• " + scaleNum(line.removePrefix("* ")), style = MaterialTheme.typography.bodyLarge)
//                        Spacer(Modifier.height(6.dp))
//                    }
//
//                    else -> {
//                        Text(line, style = MaterialTheme.typography.bodyLarge)
//                        Spacer(Modifier.height(6.dp))
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun InstructionsCard(instructionsText: String) {
//
//    val lines = instructionsText
//        .lines()
//        .filter { it.isNotBlank() }
//
//    Card(colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)) {
//        Column(Modifier.padding(16.dp)) {
//
//            lines.forEach { line ->
//                val trimmed = line.trim()
//
//                when {
//                    trimmed.startsWith("###") -> {
//                        Text(
//                            trimmed.removePrefix("###").removeSuffix(":").trim(),
//                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
//                        )
//                        Spacer(Modifier.height(8.dp))
//                    }
//
//                    trimmed.matches(Regex("""^\d+\..*""")) -> {
//                        Text(trimmed, style = MaterialTheme.typography.bodyLarge)
//                        Spacer(Modifier.height(8.dp))
//                    }
//
//                    trimmed.startsWith("* ") -> {
//                        Text("• " + trimmed.removePrefix("* "), style = MaterialTheme.typography.bodyLarge)
//                        Spacer(Modifier.height(6.dp))
//                    }
//
//                    trimmed.startsWith("** ") -> {
//                        Text(trimmed.removePrefix("** "), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
//                        Spacer(Modifier.height(6.dp))
//                    }
//                }
//            }
//        }
//    }
//}

@Preview(showBackground = true)
@Composable
fun RecipeDetailScreenPreview() {
    val navController = rememberNavController()
    RecipeDetailScreen(navController = navController, recipeId = "previewRecipeId")
}