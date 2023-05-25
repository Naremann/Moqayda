package com.example.moqayda.ui.login

import android.util.Log
import androidx.databinding.ObservableField
import com.example.moqayda.DataUtils
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.database.getUserFromFirestore
import com.example.moqayda.database.updateFirebaseUserToken
import com.example.moqayda.models.AppUser
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

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
                        FirebaseMessaging.getInstance().token.addOnSuccessListener{ token->
                            updateFirebaseUserToken(task.result.user!!.uid, OnCompleteListener {
                                Log.e("updateFirebaseUserToken","Success")
                            },token)

                        }
                    }
                    else{
                        showLoading.value=false
                        messageLiveData.value=task.exception?.localizedMessage
                    }

                }

        }
    }
    private fun validateEmailField():Boolean{
        var isValidate=true
        if(email.get().isNullOrBlank()){
            emailError.set("Please Enter your email")
            isValidate=false
        }
        else
            emailError.set(null)
        return isValidate
    }

    fun resetPassword(){
        showLoading.value=true

        if(validateEmailField()){
            email.get().let {
                auth.sendPasswordResetEmail(it!!).addOnCompleteListener { task->
                    showLoading.value=false
                    if(task.isSuccessful){
                        messageLiveData.value="We have sent you a mail, check your email now !"
                    } else{
                        showLoading.value=false
                        messageLiveData.value=task.exception?.localizedMessage
                    }
                }
            }
        }
    }

    private fun checkUser(userId:String) {
        getUserFromFirestore(userId, { documentSnapshot->
            showLoading.value=false
            navigator.navigateToHomeActivity()
            val user = documentSnapshot.toObject(AppUser::class.java)
            DataUtils.USER=user
            Log.e("login", "user $user")
            Log.e("login", "USER ${DataUtils.USER}")


        }) { ex ->
            showLoading.value=false
            messageLiveData.value = ex.localizedMessage
        }

    }

    fun navigateToRegisterFragment(){
        navigator.navigateToRegisterFragment()
    }
    private fun validate() : Boolean{
        var validate = true
        if(email.get().isNullOrBlank()){
            emailError.set("Please enter your email")
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