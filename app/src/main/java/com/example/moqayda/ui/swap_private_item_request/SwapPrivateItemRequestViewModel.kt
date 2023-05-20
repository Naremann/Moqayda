package com.example.moqayda.ui.swap_private_item_request

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.moqayda.DataUtils
import com.example.moqayda.api.RetrofitBuilder.retrofitService
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.Product
import com.example.moqayda.models.SwapPrivateItemResponse
import kotlinx.coroutines.launch

class SwapPrivateItemRequestViewModel:BaseViewModel<Navigator>() {
    var privateItemId:Int=-1
    var privateItemName:String?=null
    var product:Product?=null
    var privateItemImage:String?=null
    var productOwnerId :Int=0
    val userId = DataUtils.USER?.id

    fun sendRequestToSwap(){
        showLoading.value=true
        viewModelScope.launch {
            Log.e("sendRequestToSwap","privateProductId $privateItemId productId ${product?.id}  productOwnerId $productOwnerId")
            val swapPrivateItem = SwapPrivateItemResponse(productId = product?.id!!,id=0, userId = product?.userId, privateItemOwnerId = productOwnerId)
            val response = retrofitService.sendRequestToSwapPrivateItem(swapPrivateItem)
            showLoading.value=false
            try {
                if(response.isSuccessful){
                    messageLiveData.value="Request is sent"
                    Log.e("sendRequestToSwap","productId ${product?.id}  privateItemId $privateItemId")
                    Log.e("sendRequestToSwap()","Success")
                }
                else{
                    messageLiveData.value="Error, you have already sent a request for this item"
                }

            }catch (ex:Exception){
                messageLiveData.value="Error ,${ex.localizedMessage}"
                Log.e("sendRequestToSwap()","Fail ${ex.localizedMessage}")
            }
        }
    }
}