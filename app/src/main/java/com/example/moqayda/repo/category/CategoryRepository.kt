package com.example.moqayda.repo.category

import com.example.moqayda.api.ApiInterface
import com.example.moqayda.models.CategoryItem
import com.example.moqayda.models.CategoryResponse
import retrofit2.Response

interface CategoryRepository {
    suspend fun getCategories(): List<CategoryItem?>?
}
interface CategoryOnlineRepository{
    suspend fun getCategories():List<CategoryItem?>?
}
interface CategoryOfflineRepository{
    suspend fun updateCategory(categoryItems:List<CategoryItem?>?)
    suspend fun getCategories(): List<CategoryItem?>?
}