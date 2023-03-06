package com.example.moqayda.ui.product

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.ProductItem
import kotlinx.coroutines.launch

class ProductViewModel: BaseViewModel<Navigator>() {
    var isVisibleProgress = MutableLiveData<Boolean>()
    var categoryId : Int=0
    var productList = MutableLiveData<List<ProductItem?>?>()
    init {
        fetchProductsData()
    }
    private fun fetchProductsData() {

        viewModelScope.launch {
            isVisibleProgress.value=true
            val result = RetrofitBuilder.retrofitService.getProductsByCategoryId(categoryId)
            isVisibleProgress.value=false
            try {
                productList.postValue(result.body())
                Log.e("success","response$result")
            }catch (ex:Exception){
                messageLiveData.value=ex.localizedMessage
                Log.e("ex","error"+ex.localizedMessage)
            }
        }

    }
}