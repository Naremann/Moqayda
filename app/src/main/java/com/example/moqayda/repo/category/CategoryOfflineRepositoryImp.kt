package com.example.moqayda.repo.category

import com.example.moqayda.database.MyDatabase
import com.example.moqayda.models.CategoryItem
import com.example.moqayda.models.CategoryResponse
import retrofit2.Response

class CategoryOfflineRepositoryImp(val database: MyDatabase?):CategoryOfflineRepository {
    override suspend fun updateCategory(categoryItems: List<CategoryItem?>?) {
       database?.categoryDao()?.updateCategory(categoryItems)
    }

    override suspend fun getCategories(): List<CategoryItem?>? {
        return database?.categoryDao()!!.getCategories()
    }
}