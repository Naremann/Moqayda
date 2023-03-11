package com.example.moqayda.database

import androidx.room.*
import com.example.moqayda.models.test.CategoryItem

@Dao
interface CategoryDao {
   // @Insert(onConflict = OnConflictStrategy.REPLACE)
   @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateCategory(categoryItems: List<CategoryItem?>?)

   @Query("select * from CategoryItem")
    suspend fun getCategories():List<CategoryItem?>?
}