package com.zybooks.myrecipe.data.local

import androidx.room.Dao
import androidx.room.Query

@Dao
interface RecipeDatabase {

    @Query("UPDATE recipes SET title = :title WHERE id = :id")
    suspend fun updateTitle(id: Int, title: String)

    @Query("DELETE FROM recipes WHERE id = :id")
    suspend fun delete(id: Int)
}
