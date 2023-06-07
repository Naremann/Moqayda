package com.example.moqayda.ui.setting

import android.util.Log
import com.example.moqayda.DataUtils
import com.example.moqayda.base.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingViewModel:BaseViewModel<Navigator>() {
    var navigator : Navigator?=null
    private var auth: FirebaseAuth = Firebase.auth
    val user = Firebase.auth.currentUser!!

    fun navigateToRegisterFragment(){
        if(deleteAccount()){
            navigator?.navigateToRegisterFragment()
        }

    }

    fun navigateToAppTermsFragment(){
        navigator?.navigateToAppTermsFragment()
    }

    fun navigateToBlockedUsersFragment(){
        navigator?.onNavigateToBlockedUsersFragment()
    }

    private fun deleteAccount():Boolean{
        var isDeleted = true
        showLoading.value=true
        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showLoading.value=false
                    Log.e("TAG", "User account deleted")
                }
                else{
                    showLoading.value=false
                    messageLiveData.value=task.exception?.localizedMessage
                    isDeleted=false
                }
            }
        return isDeleted

    }

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