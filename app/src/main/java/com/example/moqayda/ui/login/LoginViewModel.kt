package com.example.moqayda.ui.login

import androidx.databinding.ObservableField
import com.example.moqayda.base.BaseViewModel

class LoginViewModel : BaseViewModel<Navigator>() {
    val email = ObservableField<String>()
    val password = ObservableField<String>()
    val emailError = ObservableField<String>()
    val passwordError = ObservableField<String>()
    lateinit var navigator : Navigator
    fun login(){
        if(validate()){
            showLoading.value = true
            checkUser()

        }
    }

    fun resetPassword(){
        navigator.navigateToResettingPassFragment()
    }

    private fun checkUser() {
    }

    fun navigateToRegisterActivity(){
        navigator.navigateToRegisterFragment()
    }
    private fun validate() : Boolean{
        var validate = true
        if(email.get().isNullOrBlank()){
            emailError.set("Please enter the email")
            validate = false
        }
        else
            emailError.set(null)
        if(password.get().isNullOrBlank()) {
            passwordError.set("Please enter your password")
            validate = false
        }
        else
            passwordError.set(null)
        return validate
    }
}