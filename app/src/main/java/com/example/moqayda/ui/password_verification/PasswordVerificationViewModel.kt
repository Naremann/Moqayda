package com.example.moqayda.ui.password_verification

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.ui.password_verification.Navigator

class PasswordVerificationViewModel : BaseViewModel<Navigator>() {
    val isActiveButton = MutableLiveData<Boolean>()
    val code = ObservableField<String>()
    val codeError = ObservableField<String>()
    lateinit var navigator: Navigator



    fun resendCode(){
        if(isTimeOut()){
            isActiveButton.value=true
            showLoading.value=true
            //resend another code
        }

    }
    fun onTextChanged(text:CharSequence){
        if(isValidCode()){
            navigator.navigateToChangePassFragment()
        }
    }

    private fun isValidCode(): Boolean {
        var isValid = true
        return isValid

    }

    private fun isTimeOut(): Boolean {
        var isTimeOut = true
        return isTimeOut
    }

    fun validate():Boolean{
        var isValid=true
        if(code.get().isNullOrBlank()){
            codeError.set("Please enter the sent code")
            isValid = false
        }
        else{
            codeError.set(null)
        }
        return isValid
    }
}