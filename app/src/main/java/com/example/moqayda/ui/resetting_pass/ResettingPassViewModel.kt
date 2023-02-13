package com.example.moqayda.ui.resetting_pass

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.example.moqayda.base.BaseViewModel

class ResettingPassViewModel : BaseViewModel<Navigator>() {
    val emailError = ObservableField<String>()
    val email = ObservableField<String>()
    var navigator : Navigator?=null
    fun resetPassword(){
        if(validate()){
           if(checkEmail()){
               navigator!!.navigateToPassVerificationFragment()
           }
        }

    }

    private fun checkEmail() : Boolean{
        //check email is exist
        showLoading.value=true
        return true
    }

    private fun validate():Boolean{
        var isValid=true
        if(email.get().isNullOrBlank()){
            emailError.set("Please enter your email")
            isValid=false
        }
        else{
            emailError.set(null)
        }

        return isValid
    }
}