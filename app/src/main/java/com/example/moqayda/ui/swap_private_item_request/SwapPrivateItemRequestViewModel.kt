package com.example.moqayda.ui.swap_private_item_request

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.moqayda.api.RetrofitBuilder.retrofitService
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.Product
import com.example.moqayda.models.SwapItem
import kotlinx.coroutines.launch

class SwapPrivateItemRequestViewModel:BaseViewModel<Navigator>() {
    var privateItemId:Int=0
    var privateItemName:String?=null
    var product:Product?=null
    var privateItemImage:String?=null

    fun sendRequestToSwap(){
        showLoading.value=true
        viewModelScope.launch {
            Log.e("sendRequestToSwap","productId ${product?.id}  privateItemId $privateItemId")
            val swapItem = SwapItem(0,product?.id!!,privateItemId)
            val response = retrofitService.sendRequestToSwapPrivateItem(swapItem)
            showLoading.value=false
            try {
                if(response.isSuccessful){
                    messageLiveData.value="Request is sent"
                    Log.e("sendRequestToSwap()","Success")
                }

            }catch (ex:Exception){
                messageLiveData.value="Error , Try again"
                Log.e("sendRequestToSwap()","Fail ${ex.localizedMessage}")
            }
        }
    }
}