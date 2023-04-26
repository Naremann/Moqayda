package com.example.moqayda.ui.profile_editting

import android.net.Uri
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.example.moqayda.DataUtils
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.database.*
import com.example.moqayda.models.AppUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileEditingViewModel : BaseViewModel<Navigator>() {
    val user = Firebase.auth.currentUser
    var message = MutableLiveData<String>()
    var toastMessage = MutableLiveData<String>()
    var isUploadedImage=MutableLiveData<Boolean>()
    val isDeletedImage = MutableLiveData<Boolean>()

    val firstName = ObservableField<String>(DataUtils.USER?.firstName)
    val lastName = ObservableField<String>(DataUtils.USER?.lastName)
    val mobile = ObservableField<String>(DataUtils.USER?.phoneNumber)
    val country = ObservableField("Egypt")
    val address = ObservableField<String>(DataUtils.USER?.address)
    val firstNameError = ObservableField<String>()
    val lastNameError = ObservableField<String>()
    val mobileError = ObservableField<String>()
    val addressError = ObservableField<String>()
    lateinit var city: String

    private val _imageUri = MutableLiveData<Uri?>(null)
    val imageUri: MutableLiveData<Uri?>
        get() = _imageUri

    fun setImageUri(uri: Uri?) {
        _imageUri.postValue(uri)
    }
     private fun deleteImage(){
            if(imageUri.value==null){
                DataUtils.USER?.id?.let { deleteImageFromFirebaseFirestore(it, {
                    getUser()
                }, { ex->
                    showLoading.value=false
                    toastMessage.value=ex.localizedMessage
                }) }
            }
    }

    fun update() {
        showLoading.value = true
        val appUser = AppUser(DataUtils.USER?.id, firstName.get(), lastName.get(), mobile.get(), country.get(), city, address.get())
        deleteImage()
        if (validate()) {
            DataUtils.USER?.id?.let {
                updateFirebaseUser(it,  { task ->
                    if (task.isSuccessful) {
                        imageUri.value?.let {
                            DataUtils.USER!!.id?.let { it1 ->
                                storeImageInFirebaseStore(it, it1, { _->
                                    isUploadedImage.value=true
                                   getUser()
                                },  { ex->
                                    showLoading.value = false
                                    isUploadedImage.value=false
                                    toastMessage.value=ex.localizedMessage

                                })
                            }
                        }
                    } else {
                        showLoading.value = false
                        message.value = task.exception?.localizedMessage
                    }
                }, appUser)
            }
        }

    }
    private fun getUser(){
        getUserFromFirestore(DataUtils.USER?.id!!, { docSnapshot ->
            DataUtils.USER = docSnapshot.toObject(AppUser::class.java)
            showLoading.value = false
            message.value = "User updated successfully"
        }, { ex ->
            showLoading.value = false
            message.value = ex.localizedMessage

        })
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
        return validate

    }

}
