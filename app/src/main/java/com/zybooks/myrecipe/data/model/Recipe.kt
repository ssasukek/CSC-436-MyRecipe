package com.zybooks.myrecipe.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")

data class Recipe(
    val id: String = "",
    val title: String = "",
    val ingredients: String = "",
    val instructions: String = "",
    val favorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)