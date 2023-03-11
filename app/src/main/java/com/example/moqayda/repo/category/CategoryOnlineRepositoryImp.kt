package com.example.moqayda.repo.category

import android.util.Log
import com.example.moqayda.api.ApiInterface
import com.example.moqayda.models.test.CategoryItem

class CategoryOnlineRepositoryImp(val webServices: ApiInterface):CategoryOnlineRepository {
    override suspend fun getCategories(): List<CategoryItem?>? {
        try {
            Log.e("online","categories"+webServices.getAllCategories())
            return webServices.getAllCategories().body()

        }catch (ex:Exception){
            throw ex
        }
    }
}