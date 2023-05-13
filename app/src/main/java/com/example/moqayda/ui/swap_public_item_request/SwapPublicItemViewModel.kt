package com.example.moqayda.ui.swap_public_item_request

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.moqayda.DataUtils
import com.example.moqayda.api.RetrofitBuilder.retrofitService
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.PrivateProduct
import com.example.moqayda.models.Product
import com.example.moqayda.models.SwapPublicItem
import kotlinx.coroutines.launch

class SwapPublicItemViewModel:BaseViewModel<Navigator>() {
    var requestSenderProduct : PrivateProduct?=null
    var product : Product?=null
    var productOwnerId : Int?=null

    fun sendSwapRequestOfPublicItem(){
        showLoading.value=true

        val userId = DataUtils.USER?.id
        val swapPublicItem = SwapPublicItem(id = 0,productId = product?.id!!,userId=userId!!,
            productOwnerId = productOwnerId!!)
        viewModelScope.launch {
            Log.e("sendRequestToSwap","productId ${product?.id}  requestSenderProduct $productOwnerId")

            val response =  retrofitService.sendSwapRequestOfPublicItem(swapPublicItem)
            showLoading.value=false

            try {
                if(response.isSuccessful) {

                    Log.e("sendRequestToSwap()", "Success")
                    messageLiveData.value = "Request is sent"
                }
                else{
                    Log.e("sendRequestToSwap()", "Error")
                }


        }catch (ex:Exception){
                messageLiveData.value="Error , Try again"
                Log.e("sendRequestToSwap()","Fail ${ex.localizedMessage}")
            }

        }
    }
}