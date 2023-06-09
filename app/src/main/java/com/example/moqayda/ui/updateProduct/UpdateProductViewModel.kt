package com.example.moqayda.ui.updateProduct

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.R
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.CategoryItem
import com.example.moqayda.models.Product
import com.example.moqayda.repo.product.ProductRepository
import com.example.moqayda.repo.product.Resource
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class UpdateProductViewModel(ctx: Context) : BaseViewModel<Navigator>() {

    private val ctxReference: WeakReference<Context> = WeakReference(ctx)
    private val productRepository = ProductRepository(ctx)
    lateinit var navigator: Navigator
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    //    private val addProductRepository = AddProductRepository(ctx)
    private val _category = MutableLiveData<CategoryItem>()
    val category: LiveData<CategoryItem>
        get() = _category

    val productId = ObservableField<Int>()
    val productName = ObservableField<String>()
    val productDescription = ObservableField<String>()
    val productToSwap = ObservableField<String>()


    private val _descriptionHelperText = MutableLiveData<String>()
    val descriptionHelperText: LiveData<String>
        get() = _descriptionHelperText

    private val _selectedImageUrl = MutableLiveData<String?>(null)

    private val _imageUri = MutableLiveData<Uri>(null)
    val imageUri: LiveData<Uri>
        get() = _imageUri

    fun setImageUri(uri: Uri) {
        _imageUri.postValue(uri)
    }

    fun setSelectedImageUrl(imageUrl: String?) {
        _selectedImageUrl.postValue(imageUrl)
    }


    fun setProductDetails(product: Product) {
        productId.set(product.id)
        productName.set(product.name)
        productDescription.set(product.descriptions)
        productToSwap.set(product.productToSwap)
        Log.e("UpdateProductViewModel", productName.get()!!)
    }


    fun getCategory(categoryId: Int) {
        viewModelScope.launch {
            val response = RetrofitBuilder.retrofitService.getProductsByCategoryId(categoryId)
            if (response.isSuccessful) {
                _category.postValue(response.body())
            } else {
                Log.e("UpdateProductVM", "Failed to load category")
            }
        }
    }

    fun updateProduct() {
        if (productName.get().isNullOrBlank() || productDescription.get()
                .isNullOrBlank() || productToSwap.get().isNullOrBlank()
        ) {
            _toastMessage.postValue(ctxReference.get()?.getString(R.string.fill_all_fields))
            messageLiveData.postValue(ctxReference.get()?.getString(R.string.fill_all_fields))
        } else {
            if (productDescription.get()?.length!! > 100) {
                _descriptionHelperText.postValue(ctxReference.get()
                    ?.getString(R.string.maximum_100_characters))
            } else {

                val builder = AlertDialog.Builder(ctxReference.get()!!)
                builder.setTitle(ctxReference.get()!!.getString(R.string.confirmation))
                builder.setMessage(ctxReference.get()!!
                    .getString(R.string.update_product_confirmation))
                builder.setPositiveButton("OK") { dialog, which ->
                    onUpdateProduct()
                }

                builder.setNegativeButton("Cancel") { _, _ ->
                    // Cancel button clicked
                }
                val dialog = builder.create()
                dialog.show()
            }
        }
    }


    private fun onUpdateProduct() {
        _descriptionHelperText.postValue("")
        viewModelScope.launch {
            if (_imageUri.value != null) {
                val result = productRepository.updateProduct(
                    id = productId.get().toString(),
                    productName = productName.get()!!,
                    productDescription = productDescription.get()!!,
                    categoryId = _category.value?.id.toString(),
                    ProductBackgroundColor = "0",
                    ProductToSwap = productToSwap.get()!!,
                    imageUri = _imageUri.value)
                when (result) {
                    is Resource.Success<*> -> {
                        Log.e("UpdateProductViewModel", "Product updated successfully")
                        messageLiveData.postValue(ctxReference.get()
                            ?.getString(R.string.product_updated_successfully))
                        navigateToUserPublicItemsFragment()
                    }
                    is Resource.Error<*> -> {
                        Log.e("UpdateProductViewModel", "error: ${result.message}")
                        messageLiveData.postValue(ctxReference.get()
                            ?.getString(R.string.failed_to_update_product))
                    }
                    else -> {
                    }
                }
            } else {
                val result =
                    productRepository.updateProductWithCurrentImage(
                        id = productId.get().toString(),
                        productName = productName.get()!!,
                        productDescription = productDescription.get()!!,
                        categoryId = _category.value?.id.toString(),
                        ProductBackgroundColor = "0",
                        ProductToSwap = productToSwap.get()!!,
                        imageUrl = _selectedImageUrl.value)
                Log.e("UpdateProductViewModel", _selectedImageUrl.value!!)
                when (result) {
                    is Resource.Success<*> -> {
                        messageLiveData.postValue(ctxReference.get()
                            ?.getString(R.string.product_updated_successfully))
                        Log.e("UpdateProductViewModel", "Product updated successfully")
                        navigateToUserPublicItemsFragment()
                    }
                    is Resource.Error<*> -> {
                        Log.e("UpdateProductFragment", "error: ${result.message}")
                        messageLiveData.postValue(ctxReference.get()
                            ?.getString(R.string.failed_to_update_product))
                    }
                }
            }
        }

    }


    fun navigateToSelectCategory(product: Product, isUpdate: Boolean) {
        navigator.onNavigateToSelectCategoryFragment(product, isUpdate)
    }


    fun navigateToUserPublicItemsFragment() {
        navigator.navigateToUserPublicItems()
    }

}