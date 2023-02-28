package com.example.moqayda.repo.category

import com.example.moqayda.NetworkHandler
import com.example.moqayda.models.CategoryItem

class CategoryRepositoryImp(
    private val networkHandler: NetworkHandler,
    private val categoryOnlineRepository: CategoryOnlineRepository,
    private val categoryOfflineRepository: CategoryOfflineRepository):CategoryRepository{
    override suspend fun getCategories(): List<CategoryItem?>? {
       try {
           if(networkHandler.isOnline()) {
               categoryOfflineRepository.updateCategory(categoryOnlineRepository.getCategories())
               return categoryOnlineRepository.getCategories()
           }
           return categoryOfflineRepository.getCategories()
       }catch (ex:Exception){
           return categoryOfflineRepository.getCategories()
       }
    }

}