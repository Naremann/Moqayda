package com.example.moqayda.ui.private_product

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.DataUtils
import com.example.moqayda.api.RetrofitBuilder.retrofitService
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.PrivateProduct
import kotlinx.coroutines.launch

class PrivateProductViewModel : BaseViewModel<Navigator>() {
    val userId = DataUtils.USER?.id

    var isVisibleProgress = MutableLiveData<Boolean>()

    var navigator: Navigator? = null
    var privateProduct = MutableLiveData<List<PrivateProduct?>?>()

    init {
        fetchUserPrivateProducts()
    }

    fun navigateToAddPrivateProduct() {
        navigator?.navigateToAddPrivateProduct()
    }

    private fun deletePrivateProduct(privateProductId: Int) {
        viewModelScope.launch {
            val response = retrofitService.deletePrivateItemById(privateProductId)
            try {
                if (response.isSuccessful) {
                    messageLiveData.value = "Item deleted successfully"
                    fetchUserPrivateProducts()
                    Log.e("navigateToAddPrivate", "Success")
                } else {
                    Log.e("navigateToAddPrivate", "Unknown Error ${response.errorBody()}")
                }
            } catch (ex: Exception) {
                Log.e("navigateToAddPrivate", "Fail ${ex.localizedMessage}")
            }
        }

    }


    fun deletePrivateItemOwnerById(privateItemId: Int) {
        showLoading.value = true
        viewModelScope.launch {
            val response =
                retrofitService.getPrivateProductByItemId(privateItemId).privateItemAndOwnerViewModels
            try {
                if (response.isNullOrEmpty()) {
                    deletePrivateProduct(privateItemId)
                    showLoading.value = false
                    Log.e("deletePrivateItemOwner", " isEmpty Success")

                } else {
                    showLoading.value = false
                    response.forEach { privateItemAndOwnerViewModels ->
                        val response =
                            retrofitService.deletePrivateItemOwnerById(privateItemAndOwnerViewModels?.id)
                        try {
                            if (response.isSuccessful) {
                                deletePrivateProduct(privateItemId)
                                Log.e("deletePrivateItemOwner", "Success")
                            }
                        } catch (ex: Exception) {
                            messageLiveData.value = ex.localizedMessage
                            Log.e("deletePrivateItemOwner", "Fail ${ex.localizedMessage}")
                        }
                    }
                }
            } catch (ex: Exception) {
                showLoading.value = false
                messageLiveData.value = ex.localizedMessage
                Log.e("deletePrivateItemOwner", "Fail ${ex.localizedMessage}")
            }
        }
    }

    private fun fetchUserPrivateProducts() {
        isVisibleProgress.value = true
        val allPrivateItems: MutableList<PrivateProduct> = mutableListOf()
        viewModelScope.launch {
            val response = retrofitService.getAllPrivateProducts().body()
            isVisibleProgress.value = false
            try {
                Log.e("fetchUserPrivate", "Success")
                response?.forEach { privateItemResponse ->
                    if (privateItemResponse.userId == userId) {
                        allPrivateItems.add(privateItemResponse)
                    }

                }
                privateProduct.value = allPrivateItems

            } catch (ex: Exception) {
                Log.e("fetchUserPrivate", "Fail ${ex.localizedMessage}")
            }


        }
    }


}