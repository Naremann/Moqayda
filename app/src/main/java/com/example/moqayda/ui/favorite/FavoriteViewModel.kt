package com.example.moqayda.ui.favorite

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.AppUser
import com.example.moqayda.models.FavoriteItem
import com.example.moqayda.models.FavoriteResponse
import com.example.moqayda.models.Product
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class FavoriteViewModel : BaseViewModel<Navigator>() {
    lateinit var navigator: Navigator
    private val auth = Firebase.auth
    var progressBarVisible = MutableLiveData<Boolean>()

    private val _wishlist = MutableLiveData<List<FavoriteItem>>()
    val wishlist: MutableLiveData<List<FavoriteItem>>
        get() = _wishlist


    private val _favoriteProducts = MutableLiveData<List<Product>>()
    val favoriteProducts: MutableLiveData<List<Product>>
        get() = _favoriteProducts

    private val dataList = mutableListOf<Product>()



    init {
        fetchDataFromWishlist()
    }


    suspend fun getProductOwner(id: String?) : AppUser? {
        return try {
            val result = RetrofitBuilder.retrofitService.getUserById(id)
            if (result.isSuccessful) {
                val user = result.body()
                user?.firstName?.let { Log.e("ProductViewModelLog", it) }
                user
            } else {
                result.message().let { Log.e("ProductViewModelLog", it) }
                null
            }
        } catch (e: Exception) {
            Log.e("ProductViewModelLog", e.message ?: "Unknown error")
            null
        }
    }


    private fun fetchDataFromWishlist() {
        dataList.clear()
        viewModelScope.launch {
            val result = RetrofitBuilder.retrofitService.getFavoriteItems(auth.currentUser?.uid)
            if (result.isSuccessful){
                Log.e("FavoriteViewModel", "success result$result")
                _wishlist.postValue(result.body()?.favoriteItems)
                result.body()?.favoriteItems?.forEach {
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
            _favoriteProducts.postValue(dataList)
        }
    }

     fun removeFavoriteProduct(favoriteItemId:Int){
        viewModelScope.launch {
            val response = RetrofitBuilder.retrofitService.deleteFavoriteProductById(favoriteItemId)
            if (response.isSuccessful){
                Log.e("FavoriteViewModel","deleted successfully")

                fetchDataFromWishlist()
            }else{
                Log.e("FavoriteViewModel","Delete Failed")
            }
        }

    }

    fun navigateToHome(product: Product){
        navigator.onNavigateToProductDetails(product)
    }

}