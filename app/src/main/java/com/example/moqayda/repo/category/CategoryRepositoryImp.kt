package com.example.moqayda.repo.category

import android.util.Log
import com.example.moqayda.NetworkHandler
import com.example.moqayda.models.CategoryItem

class CategoryRepositoryImp(
    private val networkHandler: NetworkHandler,
    private val categoryOnlineRepository: CategoryOnlineRepository,
    private val categoryOfflineRepository: CategoryOfflineRepository):CategoryRepository{
    override suspend fun getCategories(): List<CategoryItem?>? {
       try {
           if(networkHandler.isOnline()) {
               Log.e("isOnline","${networkHandler.isOnline()}")
               categoryOfflineRepository.updateCategory(categoryOnlineRepository.getCategories())
               Log.e("update","list${categoryOnlineRepository.getCategories()}")
               return categoryOnlineRepository.getCategories()
           }
           Log.e("offline ","list${categoryOfflineRepository.getCategories()}")
           return categoryOfflineRepository.getCategories()
       }catch (ex:Exception){
           Log.e("repo","error${ex.localizedMessage}")
           return categoryOfflineRepository.getCategories()
       }
    }

}