package com.example.moqayda.ui.swap_private_offers

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.DataUtils
import com.example.moqayda.api.RetrofitBuilder.retrofitService
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.*
import kotlinx.coroutines.launch

class SwapPrivateOffersViewModel:BaseViewModel<Navigator>() {
    val userId = DataUtils.USER?.id
    var isVisibleProgressBar=MutableLiveData<Boolean>()
    val swapPrivateOffers= MutableLiveData<List<PrivateProductOwnerByIdResponse?>?>()
    val toastMessage=MutableLiveData<String>()
    var navigator : Navigator?=null
    var privateProductId:Int=0
    var productId:Int=0
    init {
        getSwapOfferResponse()
    }


    private fun getSwapOfferResponse(){
        isVisibleProgressBar.value=true
        viewModelScope.launch {
            val response = retrofitService.getSwapOffersBuUserId(userId).userPrivateOffersViewModels
            try {
                val swapOffersOfPrivateItems:MutableList<PrivateProductOwnerByIdResponse> = mutableListOf()
                Log.e("getSwapOfferResponse","Success $response")

                response?.forEach {userPrivateOffer->
                   productId=userPrivateOffer?.productId!!
                    val userPrivateOffersResponse =
                        retrofitService.getPrivateProductOwnerByPrivateItemOwnerId(userPrivateOffer.privateItemOwnerId)
                    val privateItemId = userPrivateOffersResponse.privateItemId
                    privateProductId=privateItemId!!


                    try {
                       val swapOffers= retrofitService.getPrivateProductByItemId(privateItemId)
                        isVisibleProgressBar.value=false
                        try {
                            Log.e("privateItemAndOwner","Success")
                            swapOffersOfPrivateItems.add(swapOffers)
                        }
                        catch (ex:Exception){
                            toastMessage.value="Fail ${ex.localizedMessage}"
                            Log.e("swapOffers","Fail ${ex.localizedMessage}")
                        }


                    }
                    catch (ex:Exception){
                        toastMessage.value="something went wrong,Try again"
                        Log.e("privateItemAndOwner","Fail ${ex.localizedMessage}")
                    }

                }
                swapPrivateOffers.value=swapOffersOfPrivateItems

            }
            catch (ex:Exception){
                toastMessage.value="something went wrong,Try again"
                Log.e("getSwapOfferResponse","Fail ${ex.localizedMessage}")
            }
        }
    }
}