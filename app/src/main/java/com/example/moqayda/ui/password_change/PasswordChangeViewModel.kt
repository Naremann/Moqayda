package com.example.moqayda.ui.password_change

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.example.moqayda.base.BaseViewModel

class PasswordChangeViewModel : BaseViewModel<Navigator>() {
    private val isActiveButton = MutableLiveData<Boolean>()
    val password = ObservableField<String>()
    val passwordError = ObservableField<String>()
    val confirmPass = ObservableField<String>()
    val confirmPassError = ObservableField<String>()

    fun saveNewPassword(){
        if(validate()){
            showLoading.value=true
            //save new password
        }
    }
    private fun validate() : Boolean{
        var validate = true
        if(confirmPass.get().isNullOrBlank()){
            confirmPassError.set("Please enter the password")
            validate = false
        }
        else
            confirmPassError.set(null)
        if(password.get().isNullOrBlank()) {
            passwordError.set("Please enter your password")
            validate = false
        }
        else
            passwordError.set(null)
        return validate
    }
}