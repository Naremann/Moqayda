package com.example.moqayda.repo.category

import com.example.moqayda.api.ApiInterface
import com.example.moqayda.models.CategoryItem

class CategoryOnlineRepositoryImp(val webServices: ApiInterface):CategoryOnlineRepository {
    override suspend fun getCategories(): List<CategoryItem?>? {
        try {
            return webServices.getCategories().category
            //return webServices.getCategories()

        }catch (ex:Exception){
            throw ex
        }
    }
}