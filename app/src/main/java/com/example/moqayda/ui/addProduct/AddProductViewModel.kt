package com.example.moqayda.ui.addProduct

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.CategoryItem
import com.example.moqayda.repo.product.AddProductRepository
import kotlinx.coroutines.launch

class AddProductViewModel(ctx: Context) : BaseViewModel<Navigator>() {


    lateinit var navigator: Navigator

    private val addProductRepository = AddProductRepository(ctx)

    val productName = ObservableField<String>()
    val productDescription = ObservableField<String>()
    val productNameError = ObservableField<String>()
    val productDescriptionError = ObservableField<String>()
    val userLocation = ObservableField<String>()
    val productToSwap = ObservableField<String>()

    private val _fileName = MutableLiveData("")
    val fileName: LiveData<String>
        get() = _fileName


    private val _descriptionHelperText = MutableLiveData<String>()
    val descriptionHelperText: LiveData<String>
        get() = _descriptionHelperText


    private val _categoryList = MutableLiveData<List<CategoryItem>>()
    val categoryList: LiveData<List<CategoryItem>>
        get() = _categoryList

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    val connectionError: LiveData<String>
        get() = addProductRepository.connectionError

    val response: LiveData<String>
        get() = addProductRepository.serverResponse

    private val _imageUri = MutableLiveData<Uri>(null)
    val imageUri: LiveData<Uri>
        get() = _imageUri
     fun sendRequest(){
         if(validateProductNameAndDescFields()){

         }
     }

    private fun validateProductNameAndDescFields(): Boolean {
        var isValidate=true
        if(productName.get().isNullOrBlank()){
            productNameError.set("Please enter the product name")
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

    fun setImageUri(uri: Uri) {
        _imageUri.postValue(uri)
    }


    fun setFileName(name: String) {
        _fileName.value = name
    }

    fun reset() {
        addProductRepository.restAddProductVariables()
    }


    fun navigateToHome(){
        navigator.onNavigateToHomeFragment()
    }
    fun navigateToSelectCategory(){
        navigator.onNavigateToSelectCategoryFragment()
    }

    fun upload(

        selectedCategory: String,
        imageUri: Uri,
        fileRealPath: String
    ) {
        if (
            productName.get().isNullOrBlank() ||
            productDescription.get().isNullOrBlank() ||
            userLocation.get().isNullOrBlank() ||
            productToSwap.get().isNullOrBlank()
        ) {
            _toastMessage.postValue("Please fill all fields")
        } else {
            if (productDescription.get()?.length!! < 100) {
                _descriptionHelperText.postValue("This field requires at least 100 characters")
            } else {
                _descriptionHelperText.postValue("")

                viewModelScope.launch {
                    addProductRepository.uploadProduct(
                        productName.get()!!,
                        productDescription.get()!!,
                        selectedCategory,
                        imageUri,
                        fileRealPath
                    )
                }

            }
        }
    }

    private fun fetchCategoryList() {

        viewModelScope.launch {

            val response = RetrofitBuilder.retrofitService.getAllCategories()
            try {
                if (response.isSuccessful) {
                    _categoryList.postValue(response.body())

                }
            } catch (ex: Exception) {
                Log.e("ex", "error" + ex.localizedMessage)
            }
        }

    }

    init {
        fetchCategoryList()
    }


}