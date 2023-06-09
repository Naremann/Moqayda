package com.example.moqayda.ui.addProduct

import android.content.Context
import android.net.Uri
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.R
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.CategoryItem
import com.example.moqayda.repo.product.ProductRepository
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class AddProductViewModel(ctx: Context) : BaseViewModel<Navigator>() {

    private val ctxReference: WeakReference<Context> = WeakReference(ctx)

    lateinit var navigator: Navigator
    private val productRepository = ProductRepository(ctx)

    val productName = ObservableField<String>()
    val productDescription = ObservableField<String>()
    val productToSwap = ObservableField<String>()

    private val _fileName = MutableLiveData("")
    val fileName: LiveData<String>
        get() = _fileName


    val _descriptionHelperText = MutableLiveData<String>()
    val descriptionHelperText: LiveData<String>
        get() = _descriptionHelperText


    private val _categoryList = MutableLiveData<List<CategoryItem>>()
    val categoryList: LiveData<List<CategoryItem>>
        get() = _categoryList

    val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    val connectionError: LiveData<String>
        get() = productRepository.connectionError

    val response: LiveData<String>
        get() = productRepository.serverResponse

    private val _imageUri = MutableLiveData<Uri>(null)
    val imageUri: LiveData<Uri>
        get() = _imageUri

    fun setImageUri(uri: Uri) {
        _imageUri.postValue(uri)
    }


    fun setFileName(name: String) {
        _fileName.value = name
    }

    fun reset() {
        productRepository.restAddProductVariables()
    }


    fun navigateToHome(){
        navigator.onNavigateToHomeFragment()
    }
    fun navigateToSelectCategory(){
        navigator.onNavigateToSelectCategoryFragment()
    }

    fun uploadProduct(
        selectedCategory: String,
        imageUri: Uri,
        fileRealPath: String,
        userId: String
    ) {
        if (
            productName.get().isNullOrBlank() ||
            productDescription.get().isNullOrBlank() ||
            productToSwap.get().isNullOrBlank()
        ) {
            _toastMessage.postValue(ctxReference.get()?.getString(R.string.fill_all_fields))
        } else {
            if (productDescription.get()?.length!! > 100) {
                _descriptionHelperText.postValue(ctxReference.get()?.getString(R.string.maximum_100_characters))
            } else {
                _descriptionHelperText.postValue("")
                viewModelScope.launch {
                    productRepository.uploadProduct(
                        productName.get()!!,
                        productDescription.get()!!,
                        productToSwap.get()!!,
                        selectedCategory,
                        imageUri,
                        fileRealPath,
                        userId
                    )
                }
            }
        }
    }

}