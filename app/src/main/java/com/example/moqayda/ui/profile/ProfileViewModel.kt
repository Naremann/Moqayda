package com.example.moqayda.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.DataUtils
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.AppUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlin.math.log

class ProfileViewModel: BaseViewModel<Navigator>() {

    lateinit var navigator : Navigator
    val fullName = DataUtils.USER?.firstName+" "+DataUtils.USER?.lastName
    val email = DataUtils.USER?.email
    val phone = DataUtils.USER?.phoneNumber
    val address = DataUtils.USER?.city+"-"+DataUtils.USER?.address
    val userId = DataUtils.USER?.id
    private val _appUser = MutableLiveData<AppUser>()
    val appUser:LiveData<AppUser>
        get() = _appUser

    init {
        getCurrentUser()
    }



    fun navigateToSwapPublicOffersFragment(){
        navigator.navigateToSwapPublicOffersFragment()
    }

    fun navigateToSettingFragment(){
        navigator.navigateToSettingFragment()
    }
    fun navigateToPrivateProduct(){
        navigator.navigateToPrivateProducts()
    }
    fun navigateToSwapPrivateOffersFragment(){
        navigator.navigateToSwapPrivateOffersFragment()
    }

    fun navigateToUserPublicProducts(){
        navigator.navigateToUserPublicProducts()
    }
     fun isLoggedOut():Boolean{
        Firebase.auth.signOut()
        return true
    }
    fun navigateToProfileEditing(appUser:AppUser){
        navigator.navigateToProfileEditing(appUser)
    }

    fun navigateToLoginFragment(){
            navigator.navigateToLoginFragment()
    }
    fun startFullImageScreen() {
        navigator.startFullImageScreen()
    }

    fun navigateToCompletedBarter(){
        navigator.onNavigateToCompletedBarterFragment()
    }

    fun navigateToSentOffersFragment(){
        navigator.onNavigateToSentOffersFragment()
    }


    private fun getCurrentUser(){
        viewModelScope.launch {
            val response = RetrofitBuilder.retrofitService.getUserById(userId)
            if (response.isSuccessful){
                Log.e("ProfileViewModel","User loaded Successfully: ${response.body()?.email}")
                _appUser.postValue(response.body())
            }else{
                Log.e("ProfileViewModel","Failed to reload user: ${response.message()}")
                messageLiveData.postValue("Failed to reload user: ${response.message()}")
            }
        }
    }

}