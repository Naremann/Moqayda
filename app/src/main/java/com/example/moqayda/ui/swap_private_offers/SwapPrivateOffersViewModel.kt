package com.example.moqayda.ui.swap_private_offers

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.DataUtils
import com.example.moqayda.api.RetrofitBuilder.retrofitService
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.PrivateProductOwnerByIdResponse
import kotlinx.coroutines.launch

class SwapPrivateOffersViewModel:BaseViewModel<Navigator>() {
    val userId = DataUtils.USER?.id
    val swapPrivateOffers= MutableLiveData<List<PrivateProductOwnerByIdResponse>>()
    init {
        getSwapOfferResponse()
    }
    private fun getSwapOfferResponse(){
        viewModelScope.launch {
            val response = retrofitService.getSwapOffersBuUserId(userId).userPrivateOffersViewModels
            try {
                val allSwapPrivateOffersResponse:MutableList<PrivateProductOwnerByIdResponse> = mutableListOf()

                response?.forEach {userPrivateOffers->
                    val swapPrivateOffersResponse =  retrofitService.
                    getPrivateProductOwnerByProductId(userPrivateOffers?.privateItemOwnerId)//This is supposed to be a PrivateItemId instead of  privateItemOwnerId
                    allSwapPrivateOffersResponse.add(swapPrivateOffersResponse)

                    try {
                        Log.e("getSwapPrivateOffers","Success $allSwapPrivateOffersResponse")
                    }
                    catch (ex:Exception){
                        Log.e("getSwapPrivateOffers","Fail $ex.localizedMessage")
                    }


                }
                swapPrivateOffers.value= allSwapPrivateOffersResponse
                Log.e("getSwapOfferResponse","Success $response")
            }
            catch (ex:Exception){
                Log.e("getSwapOfferResponse","Fail ${ex.localizedMessage}")
            }
        }
    }
}