package com.example.moqayda.ui.registeration

import android.util.Log
import androidx.databinding.ObservableField
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.database.addUserToFirestore
import com.example.moqayda.models.AppUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RegisterViewModel : BaseViewModel<Navigator>() {
    private var auth: FirebaseAuth = Firebase.auth
    private lateinit var firebaseReference:DatabaseReference

    val firstName = ObservableField<String>()
    val lastName = ObservableField<String>()
    val mobile = ObservableField<String>()
    val country = ObservableField<String>()
    val city = ObservableField<String>()
    val address = ObservableField<String>()
    val email = ObservableField<String>()
    val password = ObservableField<String>()


    val firstNameError = ObservableField<String>()
    val lastNameError = ObservableField<String>()
    val mobileError = ObservableField<String>()
    val countryError = ObservableField<String>()
    val cityError = ObservableField<String>()
    val addressError = ObservableField<String>()
    val emailError = ObservableField<String>()
    val passwordError = ObservableField<String>()
    lateinit var navigator: Navigator
    fun createAccount() {
        if (validate()) {
            addAccountToFirebase()
        }
    }
    fun navigateToLogin(){
        navigator.navigateToLoginFragment()
    }
    private fun addAccountToFirebase() {

        showLoading.value = true
        password.get()?.let {
            auth.createUserWithEmailAndPassword(email.get().toString(), it)
                .addOnCompleteListener { task ->
                    showLoading.value = false
                    if (task.isSuccessful) {
                        val currentUser = auth.currentUser
                        val userId = currentUser!!.uid
                        Log.e("addUser", "signInWithCustomToken:success${email.get().toString()}")
                        createFireStoreUser(task.result.user!!.uid)
                        addUserToFirebaseDatabase(userId)
                    } else {
                        messageLiveData.value = task.exception?.localizedMessage
                    }

                }
        }

    }

    private fun createFireStoreUser(id: String) {
        val user = AppUser(
            id,
            firstName.get(),
            lastName.get(),
            mobile.get(),
            country.get(),
            city.get(),
            address.get(),
            email.get()
        )
        addUserToFirestore(user, {
            navigator.navigateToLoginFragment()
        }, { ex ->
            messageLiveData.value = ex.localizedMessage
        })
    }

    private fun addUserToFirebaseDatabase(userId:String){
        firebaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId)
        val hashMap:HashMap<String,String> = HashMap()
        hashMap["userUid"] = userId
        hashMap["email"] = email.get()!!
        hashMap["profileImage"] = ""
        firebaseReference.setValue(hashMap).addOnCompleteListener {
            if (it.isSuccessful){
                Log.e("RegisterViewModel","userAdded")
            }else{
                Log.e("RegisterViewModel","Failed")
            }
        }
    }

    private fun validate(): Boolean {
        var validate = true
        if (firstName.get().isNullOrBlank()) {
            firstNameError.set("Please enter first name")
            validate = false
        } else
            firstNameError.set(null)
        if (lastName.get().isNullOrBlank()) {
            lastNameError.set("Please enter last name")
            validate = false
        } else
            lastNameError.set(null)
        if (mobile.get().isNullOrBlank()) {
            mobileError.set("Please enter mobile")
            validate = false
        } else
            mobileError.set(null)
        if (country.get().isNullOrBlank()) {
            countryError.set("Please enter country")
            validate = false
        } else
            countryError.set(null)
        if (city.get().isNullOrBlank()) {
            cityError.set("Please enter city")
            validate = false
        } else
            cityError.set(null)
        if (address.get().isNullOrBlank()) {
            addressError.set("Please enter address")
            validate = false
        } else
            addressError.set(null)
        if (email.get().isNullOrBlank()) {
            emailError.set("Please enter the email")
            validate = false
        } else
            emailError.set(null)
        if (password.get().isNullOrBlank()) {
            passwordError.set("Please enter your password")
            validate = false
        } else
            passwordError.set(null)
        return validate

    }
}