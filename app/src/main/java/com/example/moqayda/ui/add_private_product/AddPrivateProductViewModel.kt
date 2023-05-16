package com.example.moqayda.ui.add_private_product

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.DataUtils
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.repo.product.AddPrivateProductRepository
import com.example.moqayda.repo.product.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPrivateProductViewModel  @Inject constructor(
    var addPrivateProductRepository: AddPrivateProductRepository): BaseViewModel<Navigator>() {
    val productName = ObservableField<String>()
    val productDescription = ObservableField<String>()
    val productNameError = ObservableField<String>()
    val productDescriptionError = ObservableField<String>()
    var toastMessage=MutableLiveData<String>()
    var navigator: Navigator?=null
    val userId = DataUtils.USER?.id

    private val _imageUri = MutableLiveData<Uri>(null)
    val imageUri: LiveData<Uri>
        get() = _imageUri


    fun setImageUri(uri: Uri) {
        _imageUri.postValue(uri)
    }



    @RequiresApi(Build.VERSION_CODES.Q)
    fun uploadPrivateProduct() {
        viewModelScope.launch {
            showLoading.value=true
                val result = addPrivateProductRepository.uploadPrivateProduct(
                    name = productName.get()!!,
                    descriptions = productDescription.get()!!,
                    userId = userId!!,
                    imageUri = _imageUri.value


                )


            when (result) {
                is Resource.Success<*> -> {
                    toastMessage.value="Product created successfully"
                    Log.e("TAG", "addProduct: product created Successfully")
                    navigator?.navigateToPrivateProductFragment()
                }
                is Resource.Error<*> -> {
                    toastMessage.value=result.message.toString()
                    Log.e("TAG", "addProduct: ${result.message}")
                }
            }

            showLoading.value=false
        }
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    fun createPrivateProduct(){
        if(validate()){
            uploadPrivateProduct()
        }

    }

    private fun validate() : Boolean {
        var isValidate=true

        if (imageUri.value == null) {
            isValidate=false
            toastMessage.value="No image picked, please pick an photo"
            Log.e("TAG", "addProduct: No image picked, please pick an photo")
        }
        if(productName.get().isNullOrBlank() || productName.get()!!.length<=5){
            productNameError.set("Please enter the product name with a minimum of five characters")
            isValidate=false
        }else{
            productNameError.set(null)
        }
        if(productDescription.get().isNullOrBlank()){
            productDescriptionError.set("Please enter the product description")
            isValidate=false
        }
        else{
            productDescriptionError.set(null)
        }
        return isValidate
    }

}