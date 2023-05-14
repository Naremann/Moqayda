package com.example.moqayda.ui.product_details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.AppUser
import kotlinx.coroutines.launch

class ProductDetailsViewModel:BaseViewModel<Navigator>() {
    lateinit var navigator: Navigator
    private val _appUser = MutableLiveData<AppUser>()
    val appUser: LiveData<AppUser>
        get() = _appUser


    fun navigateToSwapItemsFragment() {
        navigator.navigateToSwappingItemsFragment()
    }

    fun navigateToUserProfile(appUser: AppUser){
        navigator.onNavigateToUserProfile(appUser)
    }


     fun getProductOwner(id: String) {
         viewModelScope.launch {
             val result = RetrofitBuilder.retrofitService.getUserById(id)
             if (result.isSuccessful) {
                 result.body()?.firstName?.let { Log.e("ProductViewModelLog", it) }
                 _appUser.postValue(result.body())
             } else {
                 result.message().let { Log.e("ProductViewModelLog", it) }
                 messageLiveData.postValue("couldn't loud user data")

             }
         }

    }
}

