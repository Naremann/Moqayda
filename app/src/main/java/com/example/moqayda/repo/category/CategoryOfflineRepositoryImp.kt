package com.example.moqayda.repo.category

import android.util.Log
import com.example.moqayda.database.local.MyDatabase
import com.example.moqayda.models.CategoryItem

class CategoryOfflineRepositoryImp(val database: MyDatabase):CategoryOfflineRepository {
    override suspend fun updateCategory(categoryItems: List<CategoryItem?>?) {
       database.categoryDao().updateCategory(categoryItems)
    }

    override suspend fun getCategories(): List<CategoryItem?>? {
        try {
            return database.categoryDao().getCategories()

        }catch (ex:Exception){
            Log.e("","error${ex.localizedMessage}")
            throw ex
        }
    }
}