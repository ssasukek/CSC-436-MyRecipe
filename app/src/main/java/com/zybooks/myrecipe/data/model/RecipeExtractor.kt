package com.zybooks.myrecipe.data.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
object RecipeExtractor {
    private val client = OkHttpClient()

    suspend fun extractRecipe(url: String): Triple<String, String, String> =
        withContext(Dispatchers.IO){
            val html = fetchHtml(url)
            parseRecipe(html)
        }

    private fun fetchHtml(url: String): String {
        val request = Request.Builder().url(url).build()

        client.newCall(request).execute().use{ response ->
            return response.body?.string() ?: throw Exception("Failed to fetch HTML")
        }
    }

    private fun parseRecipe(html: String): Triple<String, String, String> {
        val doc = Jsoup.parse(html)

        val title = doc.select("h1").first()?.text()
            ?: doc.title()
            ?: "Untitled"

        val ingredientSelectors = listOf(
            "[itemprop=recipeIngredient]",
            ".ingredient",
            ".ingredients li",
            ".recipe-ingredients li",
            "li.ingredient"
        )

        val ingredientElements = ingredientSelectors
            .flatMap { selector -> doc.select(selector) }
            .ifEmpty { doc.select("ul li") }

        val ingredientsText = ingredientElements
            .map { "- " + it.text().trim() }
            .distinct()
            .joinToString("\n")
            .ifBlank { "No ingredients found." }

        val instructionSelectors = listOf(
            "[itemprop=recipeInstructions] li",
            "[itemprop=recipeInstructions] p",
            ".instructions li",
            ".directions li",
            ".steps li",
            "ol li"
        )

        val instructionElements = instructionSelectors
            .flatMap { selector -> doc.select(selector) }
            .ifEmpty { doc.select("p") } // fallback

        val instructionsText = instructionElements
            .mapIndexed { index, el -> "${index + 1}. ${el.text().trim()}" }
            .joinToString("\n")
            .ifBlank { "No instructions found." }

        return Triple(title, ingredientsText, instructionsText)
    }
}