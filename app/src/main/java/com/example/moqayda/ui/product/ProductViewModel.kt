package com.example.moqayda.ui.product

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.test.CategoryItem
import kotlinx.coroutines.launch

class ProductViewModel: BaseViewModel<Navigator>() {
    var isVisibleProgress = MutableLiveData<Boolean>()
//    var categoryId: Int = 0
//    var productList = MutableLiveData<List<ProductItem?>?>()

    private val categoryId = MutableLiveData<Int>()
    fun getProductsById(id: Int){
        Log.e("ProductViewModelTest", "getProductsById: $id")
        categoryId.postValue(id)
        fetchProductsData(id)
    }
    private val _categoryItem = MutableLiveData<CategoryItem>()
    val categoryItem: LiveData<CategoryItem>
        get() = _categoryItem

    init {

    }

    private fun fetchProductsData(id: Int) {

        viewModelScope.launch {
            isVisibleProgress.value = true
//            val result = RetrofitBuilder.retrofitService.getProductsByCategoryId(categoryId)
            Log.e("ProductViewModelTest", "fetchProductsData: $id")
            val result = RetrofitBuilder.retrofitService.getProductsByCategoryId(id)
            isVisibleProgress.value = false
            try {
//                productList.postValue(result.body())
                _categoryItem.postValue(result.body())
                Log.e("success","response$result")
            }catch (ex:Exception){
                messageLiveData.value=ex.localizedMessage
                Log.e("ex","error"+ex.localizedMessage)
            }
        }

    }
}