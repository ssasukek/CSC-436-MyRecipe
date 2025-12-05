package com.zybooks.myrecipe.data.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
object RecipeExtractor {
    private val client = OkHttpClient()

    suspend fun fetchHtml(url: String): String =
        withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url(url)
                .build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw Exception("Failed to fetch HTML")
                }
                response.body?.string() ?: throw Exception("Empty response body")
            }
        }
    fun htmlToText(html: String): String {
        val doc = Jsoup.parse(html)
        return doc.text()
    }
}