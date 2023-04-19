package com.example.moqayda.ui.profile

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moqayda.DataUtils
import com.example.moqayda.R
import com.example.moqayda.base.BaseViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileViewModel: BaseViewModel<Navigator>() {

    lateinit var navigator : Navigator
    val fullName = DataUtils.USER?.firstName+" "+DataUtils.USER?.lastName
    val email = DataUtils.USER?.email
    val phone = DataUtils.USER?.phoneNumber
    val address = DataUtils.USER?.city+"-"+DataUtils.USER?.address
    val image = DataUtils.USER?.image
    private fun isLoggedOut():Boolean{
        Firebase.auth.signOut()
        return true
    }

    fun navigateToLoginFragment(){
        if(isLoggedOut()){
            navigator.navigateToLoginFragment()
        }
    }

}