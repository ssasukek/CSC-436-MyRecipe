package com.zybooks.myrecipe.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")

data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val ingredients: String,
    val steps: String,
    val imageUri: String? = null,
    val isFavorite: Boolean = false
)
