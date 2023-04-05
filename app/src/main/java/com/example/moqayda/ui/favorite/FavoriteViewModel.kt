package com.example.moqayda.ui.favorite

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.CategoryItem
import com.example.moqayda.models.CategoryProductViewModel
import com.example.moqayda.models.WishlistResponse
import kotlinx.coroutines.launch

class FavoriteViewModel : BaseViewModel<Navigator>() {

    var progressBarVisible = MutableLiveData<Boolean>()
    private val dataList = mutableListOf<CategoryProductViewModel>()

    private val _productsWishlist = MutableLiveData<List<CategoryProductViewModel>?>()
    val productsWishlist: MutableLiveData<List<CategoryProductViewModel>?>
        get() = _productsWishlist

    init {
        fetchDataFromWishlist()
    }


    private fun fetchDataFromWishlist() {
        viewModelScope.launch {
            val result = RetrofitBuilder.retrofitService.getWishlist()
            if (result.isSuccessful){
                Log.e("FavoriteViewModel", "success result$result")
                result.body()?.forEach {
                    val response = RetrofitBuilder.retrofitService.getProductById(it.productId)
                    if(response.isSuccessful){
                        Log.e("FavoriteViewModel", "success response$response")
                        response.body()?.name?.let { it1 -> Log.e("FavoriteViewModel", it1) }
                        dataList.add(response.body()!!)
                    }else{
                        Log.e("FavoriteViewModel","Failed response")
                    }
                }
            }else{
                Log.e("FavoriteViewModel","Failed result")
            }
            _productsWishlist.postValue(dataList)
        }
    }

}