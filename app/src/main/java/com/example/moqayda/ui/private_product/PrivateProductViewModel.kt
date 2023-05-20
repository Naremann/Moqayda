package com.example.moqayda.ui.private_product

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.DataUtils
import com.example.moqayda.api.RetrofitBuilder.retrofitService
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.*
import kotlinx.coroutines.launch

class PrivateProductViewModel: BaseViewModel<Navigator>() {
    val userId=DataUtils.USER?.id

    var isVisibleProgress = MutableLiveData<Boolean>()

    var navigator:Navigator?=null
    var privateProduct = MutableLiveData<List<PrivateProduct?>?>()
    init {
        fetchUserPrivateProducts()
    }

    fun navigateToAddPrivateProduct(){
        navigator?.navigateToAddPrivateProduct()
    }

    private fun fetchUserPrivateProducts(){
        isVisibleProgress.value=true
        val allPrivateItems:MutableList<PrivateProduct> = mutableListOf()
        viewModelScope.launch {
            val response = retrofitService.getAllPrivateProducts().body()
            isVisibleProgress.value=false
            try {
                Log.e("fetchUserPrivate","Success")
                response?.forEach { privateItemResponse->
                    if(privateItemResponse.userId==userId){
                        allPrivateItems.add(privateItemResponse)
                    }

                }
                privateProduct.value=allPrivateItems

            }
            catch (ex:Exception){
                Log.e("fetchUserPrivate","Fail ${ex.localizedMessage}")
            }


        }
    }






}