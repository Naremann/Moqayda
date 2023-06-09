package com.example.moqayda.ui.products

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.DataUtils
import com.example.moqayda.R
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.AppUser
import com.example.moqayda.models.CategoryItem
import com.example.moqayda.models.FavoriteItem
import com.example.moqayda.models.Product
import com.example.moqayda.models.UserBlockage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class ProductViewModel(ctx: Context) : BaseViewModel<Navigator>() {
    lateinit var navigator: Navigator
    private val ctxReference: WeakReference<Context> = WeakReference(ctx)
    var isVisibleProgress = MutableLiveData<Boolean>()
    private val categoryId = MutableLiveData<Int>()
    val userBlockageList = MutableLiveData<List<UserBlockage>>()



    fun getProductsById(id: Int) {
        Log.e("ProductViewModelLog", "getProductsById: $id")
        categoryId.postValue(id)
        fetchProductsData(id)
    }

    var categoryItem = MutableLiveData<CategoryItem?>()

    init {
        getBlockedUsers()
    }

    fun addProductToFavorite(id: Int) {
        Log.e("ProductViewModelLog", "button pressed")
        viewModelScope.launch {
            addProductToFav(id)
        }
    }

    private suspend fun addProductToFav(productId: Int) {
        val response = RetrofitBuilder.retrofitService.addProductToFavorite(
            FavoriteItem(
                0,
                productId,
                Firebase.auth.currentUser?.uid!!
            )
        )
        if (response.isSuccessful) {
            Log.e("OtherUserProfileVM", "Product added to favorite")

            messageLiveData.postValue(
                ctxReference.get()?.getString(R.string.product_added_to_favorite)
            )
        } else {
            if (response.code() == 400) {
                messageLiveData.postValue(
                    ctxReference.get()?.getString(R.string.product_already_in_your_favorites)
                )
            }
            Log.e("OtherUserProfileVM", "failed to add product: ${response.code()}")
        }
    }


    private fun fetchProductsData(id: Int) {

        viewModelScope.launch {
            isVisibleProgress.value = true
            Log.e("ProductViewModelLog", "fetchProductsData: $id")
            val result = RetrofitBuilder.retrofitService.getProductsByCategoryId(id)
            isVisibleProgress.value = false
            try {
                categoryItem.postValue(result.body())
                Log.e("success", "response$result")
            } catch (ex: Exception) {
                messageLiveData.value = ex.localizedMessage
                Log.e("ex", "error" + ex.localizedMessage)
            }
        }
    }


    suspend fun getProductOwner(id: String): AppUser? {
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

    fun navigateToOwnerProfile(user: AppUser) {
        navigator.onNavigateToOwnerProfile(user)
        Log.e("ProductViewModelLog", "navigateToOwnerProfile called")
    }

    private fun getBlockedUsers() {
        viewModelScope.launch {
            val blockedUsersResponse = RetrofitBuilder.retrofitService.getBlockedUsers()
            if (blockedUsersResponse.isSuccessful) {
                userBlockageList.postValue(blockedUsersResponse.body())
            } else {
                Log.e(
                    "BlockedUsersViewModel",
                    "Failed to load blockList ${blockedUsersResponse.message()}"
                )
            }

        }
    }





}


