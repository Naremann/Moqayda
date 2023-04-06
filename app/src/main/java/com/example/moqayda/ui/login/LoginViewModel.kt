package com.example.moqayda.ui.login

import android.util.Log
import androidx.databinding.ObservableField
import com.example.moqayda.DataUtils
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.database.getUserFromFirestore
import com.example.moqayda.models.AppUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginViewModel : BaseViewModel<Navigator>() {
    private var auth: FirebaseAuth=Firebase.auth

    val email = ObservableField<String>()
    val password = ObservableField<String>()
    val emailError = ObservableField<String>()
    val passwordError = ObservableField<String>()
    lateinit var navigator : Navigator
    fun login(){
        if(validate()){
            showLoading.value = true
            auth.signInWithEmailAndPassword(email.get().toString(),password.get().toString())
                .addOnCompleteListener { task->
                    if(task.isSuccessful){
                        checkUser(task.result.user!!.uid)
                    }
                    else{
                        showLoading.value=false
                        messageLiveData.value=task.exception?.localizedMessage
                    }

                }

        }
    }

    fun resetPassword(){
        navigator.navigateToResettingPassFragment()
    }

    private fun checkUser(userId:String) {
        showLoading.value=false
        getUserFromFirestore(userId, { documentSnapshot->
            navigator.navigateToHomeActivity()
            val user = documentSnapshot.toObject(AppUser::class.java)
            DataUtils.USER=user
            Log.e("login", "user $user")
            Log.e("login", "USER ${DataUtils.USER}")


        }) { ex ->
            messageLiveData.value = ex.localizedMessage
        }

    }

    fun navigateToRegisterFragment(){
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