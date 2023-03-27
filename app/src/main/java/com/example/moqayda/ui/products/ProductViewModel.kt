package com.example.moqayda.ui.products

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.CategoryItem
import com.example.moqayda.repo.product.AddItemToFavoriteRepository
import kotlinx.coroutines.launch

class ProductViewModel(): BaseViewModel<Navigator>() {
    private val addFavoriteItemRepository: AddItemToFavoriteRepository=AddItemToFavoriteRepository()
    var isVisibleProgress = MutableLiveData<Boolean>()
    val connectionError = addFavoriteItemRepository.connectionError
    private val categoryId = MutableLiveData<Int>()

    fun getProductsById(id: Int){
        Log.e("ProductViewModelTest", "getProductsById: $id")
        categoryId.postValue(id)
        fetchProductsData(id)
    }
     var categoryItem = MutableLiveData<CategoryItem?>()

    init {

    }


    private fun fetchProductsData(id: Int) {

        viewModelScope.launch {
            isVisibleProgress.value = true
            Log.e("ProductViewModelTest", "fetchProductsData: $id")
            val result = RetrofitBuilder.retrofitService.getProductsByCategoryId(id)
            isVisibleProgress.value = false
            try {
                categoryItem.postValue(result.body())
                Log.e("success","response$result")
            }catch (ex:Exception){
                messageLiveData.value=ex.localizedMessage
                Log.e("ex","error"+ex.localizedMessage)
            }
        }

    }
    fun addItemToFavorite(itemId:Int,itemOwner:String){
        viewModelScope.launch {
             addFavoriteItemRepository.addItemToFavorite(itemId, itemOwner)
        }
    }
}