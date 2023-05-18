package com.example.moqayda.ui.profile_editting

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.DataUtils
import com.example.moqayda.R
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.convertBitmapToFile
import com.example.moqayda.database.*
import com.example.moqayda.models.AppUser
import com.example.moqayda.repo.product.Resource
import com.example.moqayda.repo.user.UserRepo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.lang.ref.WeakReference

class ProfileEditingViewModel(ctx: Context) : BaseViewModel<Navigator>() {
    private val ctxReference: WeakReference<Context> = WeakReference(ctx)
    lateinit var navigator: Navigator
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

    private val userRepo = UserRepo(ctx)

    private val _selectedImageUri = MutableLiveData<Uri?>(null)
    val selectedImageUri: MutableLiveData<Uri?>
        get() = _selectedImageUri

    private val _selectedImageUrl = MutableLiveData<String?>(null)
    val selectedImageUrl: MutableLiveData<String?>
        get() = _selectedImageUrl

    fun setSelectedImageUri(uri: Uri?){
        _selectedImageUri.postValue(uri)
    }


    fun setSelectedImageUrl(url: String?){
        _selectedImageUrl.postValue(url)
    }


    fun setImageUri(uri: Uri?) {
        _imageUri.postValue(uri)
    }


    fun update() {
        showLoading.value = true
        val appUser = AppUser(DataUtils.USER?.id, firstName.get(), lastName.get(), mobile.get(), country.get(), city, address.get())
//        deleteImage()
        getUser()
        if (validate()) {
            updateBackendUser()

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

    private fun updateBackendUser(){
        viewModelScope.launch {
            if (_selectedImageUri.value != null){
                val result = DataUtils.USER?.id?.let {
                    userRepo.updateUser(
                        it,
                        firstName.get()!!,
                        lastName.get()!!,
                        "password",
                        mobile.get()!!,
                        country.get()!!,
                        city,
                        address.get()!!,
                        DataUtils.USER?.email!!,
                        _selectedImageUri.value
                    )
                }
                when (result) {
                    is Resource.Success<*> -> {
                        Log.e("ProfileEditingViewModel", "user updated successfully")
                        messageLiveData.postValue(ctxReference.get()?.getString(R.string.user_updated_successfully))
                        navigateToProfileFragment()
                    }
                    is Resource.Error<*> -> {
                        Log.e("ProfileEditingViewModel", "error: ${result.message}")
                        messageLiveData.postValue(ctxReference.get()?.getString(R.string.failed_to_update_user))
                    }
                    else -> {
                    }
                }
            }else{
                val result = DataUtils.USER?.id?.let {
                    userRepo.updateUserWithCurrentImage(
                        it,
                        firstName.get()!!,
                        lastName.get()!!,
                        "password",
                        mobile.get()!!,
                        country.get()!!,
                        city,
                        address.get()!!,
                        DataUtils.USER?.email!!,
                        _selectedImageUrl.value
                    )
                }
                Log.e("ProfileEditingViewModel",_selectedImageUrl.value!!)
                when (result) {
                    is Resource.Success<*> -> {
                        Log.e("ProfileEditingViewModel", "user updated successfully")
                        messageLiveData.postValue(ctxReference.get()?.getString(R.string.user_updated_successfully))
                        navigateToProfileFragment()

                    }
                    is Resource.Error<*> -> {
                        Log.e("ProfileEditingViewModel", "error: ${result.message}")
                        messageLiveData.postValue(ctxReference.get()?.getString(R.string.failed_to_update_user))
                    }
                    else -> {
                    }
                }
            }

        }
    }


    private fun getUser(){
        getUserFromFirestore(DataUtils.USER?.id!!, { docSnapshot ->
            DataUtils.USER = docSnapshot.toObject(AppUser::class.java)
            showLoading.value = false

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

    fun navigateToProfileFragment(){
        navigator.onNavigateToProfileFragment()
    }

}