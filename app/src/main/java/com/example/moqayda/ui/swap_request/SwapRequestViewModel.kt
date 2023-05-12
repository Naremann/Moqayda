package com.example.moqayda.ui.swap_request

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.moqayda.api.RetrofitBuilder.retrofitService
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.Product
import com.example.moqayda.models.SwapItem
import kotlinx.coroutines.launch

class SwapRequestViewModel:BaseViewModel<Navigator>() {
    var privateItemId:Int=0
    var privateItemName:String?=null
    var product:Product?=null
    var privateItemImage:String?=null

    fun sendRequestToSwap(){
        showLoading.value=true
        viewModelScope.launch {
            Log.e("sendRequestToSwap","productId ${product?.id}  privateItemId $privateItemId")
            var swapItem = SwapItem(0,product?.id!!,privateItemId)
            val response = retrofitService.sendRequestToSwap(swapItem)
            showLoading.value=false
            try {
                if(response.isSuccessful){

                    Log.e("sendRequestToSwap()","Success")}
            }catch (ex:Exception){
                Log.e("sendRequestToSwap()","Fail ${ex.localizedMessage}")
            }
        }
    }
}