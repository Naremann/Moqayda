package com.example.moqayda.ui.setting

import com.example.moqayda.DataUtils
import com.example.moqayda.base.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingViewModel:BaseViewModel<Navigator>() {
    private var auth: FirebaseAuth = Firebase.auth

    fun changePassword(){
        showLoading.value=true
        DataUtils.USER?.email?.let {email->
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task->
                if(task.isSuccessful){
                    showLoading.value=false
                    messageLiveData.value="We have sent you a mail, check your email now !"
                } else{
                    showLoading.value=false
                    messageLiveData.value=task.exception?.localizedMessage
                }
            }
        }
    }
}