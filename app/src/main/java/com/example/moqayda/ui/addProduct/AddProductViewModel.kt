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


    fun setFileName(name:String) {
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

        selectedCategory: CategoryItem,
        imageUri: Uri,
        fileRealPath:String
    ) {
        if (productName.get().isNullOrBlank() || productDescription.get().isNullOrBlank()) {
            _toastMessage.postValue("Please fill all information")
        } else {
            if (productDescription.get()?.length!! < 100){
                _descriptionHelperText.postValue("This field requires at least 100 characters")
            }else{
                _descriptionHelperText.postValue("")

                viewModelScope.launch {
                    addProductRepository.uploadProduct(
                        productName.get()!!,
                        productDescription.get()!!,
                        selectedCategory.id.toString(),
                        imageUri,
                        fileRealPath
                    )
                }
                Log.e("AddProductViewModel", productName.get().toString())
                Log.e("AddProductViewModel", productDescription.get().toString())
                Log.e("AddProductViewModel", selectedCategory.id.toString())
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