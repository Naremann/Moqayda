package com.example.moqayda.ui.registeration

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.database.addUserToFirestore
import com.example.moqayda.database.updateFirebaseUserToken
import com.example.moqayda.models.AppUser
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RegisterViewModel : BaseViewModel<Navigator>() {
    private var auth: FirebaseAuth = Firebase.auth
    private lateinit var firebaseReference:DatabaseReference
    var filePart: MultipartBody.Part? = null
    val firstName = ObservableField<String>()
    val lastName = ObservableField<String>()
    val mobile = ObservableField<String>()
    val country = ObservableField("Egypt")
    lateinit var city :String
    val address = ObservableField<String>()
    val email = ObservableField<String>()
    val password = ObservableField<String>()


    val firstNameError = ObservableField<String>()
    val lastNameError = ObservableField<String>()
    val mobileError = ObservableField<String>()
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
                        val user = auth.currentUser

                    if (task.isSuccessful) {
                       /* FirebaseMessaging.getInstance().token.addOnSuccessListener{token->
                            updateFirebaseUserToken(user?.uid!!, OnCompleteListener {
                                Log.e("updateFirebaseUserToken","Success")
                            },token)

                        }*/
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName("${firstName.get()} ${lastName.get()}")
                            .build()

                        user?.updateProfile(profileUpdates)
                            ?.addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    Log.e("RegisterViewModel","display name Updated")
                                } else {
                                    Log.e("RegisterViewModel","failed to update name")
                                }
                            }

                        Log.e("addUser", "signInWithCustomToken:success${email.get().toString()}")
                        createFireStoreUser(task.result.user!!.uid)
                        addUser(task.result.user!!.uid)
                        addUserToFirebaseDatabase(AppUser("",
                            firstName.get(),
                            lastName.get(),
                            mobile.get(),
                            country.get(),
                            city,
                            address.get(),
                            email.get()))
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
            city,
            address.get(),
            email.get()
        )
        addUserToFirestore(user, {
            navigator.navigateToLoginFragment()
        }, { ex ->
            messageLiveData.value = ex.localizedMessage
        })
    }

    private fun addUserToFirebaseDatabase(user: AppUser) {

        val userId = auth.currentUser?.uid
        firebaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId!!)


        firebaseReference.setValue(user).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.e("RegisterViewModel", "userAdded")
            } else {
                Log.e("RegisterViewModel", "Failed")
            }
        }
    }



    private fun addUser(id: String) {
        viewModelScope.launch {
            val response = RetrofitBuilder.retrofitService.addUser(
                RequestBody.create("text/plain".toMediaTypeOrNull(), id),
                RequestBody.create("text/plain".toMediaTypeOrNull(), firstName.get()!!),
                RequestBody.create("text/plain".toMediaTypeOrNull(), lastName.get()!!),
                RequestBody.create("text/plain".toMediaTypeOrNull(), password.get()!!),
                RequestBody.create("text/plain".toMediaTypeOrNull(), mobile.get()!!),
                RequestBody.create("text/plain".toMediaTypeOrNull(), country.get()!!),
                RequestBody.create("text/plain".toMediaTypeOrNull(), city),
                RequestBody.create("text/plain".toMediaTypeOrNull(), address.get()!!),
                RequestBody.create("text/plain".toMediaTypeOrNull(), email.get()!!),
                filePart
            )
            if (response.isSuccessful) {
                Log.e("RegisterViewModel", "Backend user added")
            } else {
                // Handle error response
                Log.e("RegisterViewModel", "Backend user Failure: ${response.message()}")
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