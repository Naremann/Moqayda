package com.example.moqayda.ui.otherUserProfile

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
import com.example.moqayda.models.FavoriteItem
import com.example.moqayda.models.Product
import com.example.moqayda.models.UserBlockage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class OtherUserProfileViewModel(ctx: Context) : BaseViewModel<Navigator>() {
    private val ctxReference: WeakReference<Context> = WeakReference(ctx)

    lateinit var navigator : Navigator
    private val _appUser = MutableLiveData<AppUser>()
    val appUser: LiveData<AppUser>
    get() = _appUser


    fun addProductToFavorite(id: Int) {
        Log.e("OtherUserProfileVM", "button pressed")
        viewModelScope.launch {
            addProductToFav(id)
        }
    }

    private suspend fun addProductToFav(productId: Int) {
        val response = RetrofitBuilder.retrofitService.addProductToFavorite(FavoriteItem(0,
            productId,
            Firebase.auth.currentUser?.uid!!))
        if (response.isSuccessful) {
            Log.e("OtherUserProfileVM", "Product added to favorite")
            messageLiveData.postValue(ctxReference.get()?.getString(R.string.product_added_to_favorite))

        } else {
            if (response.code() == 400){
                messageLiveData.postValue(ctxReference.get()?.getString(R.string.product_already_in_your_favorites))

            }
            Log.e("OtherUserProfileVM", "failed to add product: ${response.code()}")
        }
    }

     fun blockUser(blockedUserId:String){
        viewModelScope.launch {
            val userBlockage = UserBlockage(blockingUserId = DataUtils.USER?.id!! , blockedUserId = blockedUserId)
            val response = RetrofitBuilder.retrofitService.blockUser(userBlockage)
            if (response.isSuccessful){
                messageLiveData.postValue(ctxReference.get()?.getString(R.string.user_blocked))
                navigateToHomeFragment()
            }else{
                Log.e("OtherUserProfileVM","Fail: ${response.message()}")
                messageLiveData.postValue(ctxReference.get()?.getString(R.string.failure_message))
            }
        }
    }


    fun startFullImageScreen() {
        navigator.onStartFullImageScreen()
    }

    fun navigateToProductDetails(product: Product){
        navigator.onNavigateToProductDetails(product)
    }

    private fun navigateToHomeFragment(){
        navigator.onNavigateToHomeFragment()
    }
}