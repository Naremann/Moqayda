package com.example.moqayda.ui.otherUserProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.AppUser

class OtherUserProfileViewModel: BaseViewModel<Navigator>() {

    lateinit var navigator : Navigator
    private val _appUser = MutableLiveData<AppUser>()
    val appUser: LiveData<AppUser>
    get() = _appUser

    fun startFullImageScreen() {
        navigator.onStartFullImageScreen()
    }
    fun navigateToUserPublicProducts(){
        navigator.onNavigateToUserPublicProducts()
    }

}