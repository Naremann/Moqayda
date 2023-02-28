package com.example.moqayda.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moqayda.models.CategoryItem

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCategory(categoryItems: List<CategoryItem?>?)

    @Query("select * from CategoryItem")
    suspend fun getCategories():List<CategoryItem?>?
}