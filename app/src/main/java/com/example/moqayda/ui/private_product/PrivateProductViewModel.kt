package com.example.moqayda.ui.private_product

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.DataUtils
import com.example.moqayda.api.RetrofitBuilder.retrofitService
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.PrivateItem
import com.example.moqayda.models.UserPrivateItemViewModelsItem
import kotlinx.coroutines.launch

class PrivateProductViewModel: BaseViewModel<Navigator>() {

    var isVisibleProgress = MutableLiveData<Boolean>()

    var navigator:Navigator?=null
    var privateProduct = MutableLiveData<List<UserPrivateItemViewModelsItem?>?>()
    init {
        fetchPrivateProducts()
    }

    fun navigateToAddPrivateProduct(){
        navigator?.navigateToAddPrivateProduct()
    }

    private fun fetchPrivateProducts() {

        viewModelScope.launch {
            isVisibleProgress.value = true

            val userId = DataUtils.USER?.id
            Log.e("ProductViewModelLog", "fetchProductsData: $userId")
            val result=retrofitService.getPrivateProductByUserId(userId)
            isVisibleProgress.value = false
            try {
                privateProduct.value=result.userPrivateItemViewModels
                Log.e("success","response$result")
            }catch (ex:Exception){
                messageLiveData.value=ex.localizedMessage
                Log.e("ex","error"+ex.localizedMessage)
            }
        }
    }





}