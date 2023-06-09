package com.example.moqayda.ui.swa_public_offers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.DataUtils
import com.example.moqayda.api.RetrofitBuilder.retrofitService
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.Product
import kotlinx.coroutines.launch

class SwapOffersOfPublicItemsViewModel:BaseViewModel<Navigator>() {
    val userId = DataUtils.USER?.id
    var isVisibleProgressBar= MutableLiveData<Boolean>()
    val swappublicOffers= MutableLiveData<List<Product?>?>()
    val toastMessage= MutableLiveData<String>()
    var navigator : Navigator?=null
    var senderProductId:Int=0
    var productId:Int=0

    private val _isEmpty = MutableLiveData(false)
    val isEmpty: LiveData<Boolean>
        get() = _isEmpty

    init {
        getSwapPublicOfferResponse()
    }


    private fun getSwapPublicOfferResponse(){
        isVisibleProgressBar.value=true
        viewModelScope.launch {
            val response = retrofitService.getSwapPublicOffersBuUserId(userId).body()?.userProdOffersViewModels
            try { val swapOffersOfPrivateItems:MutableList<Product> = mutableListOf()
                Log.e("getSwapOfferResponse","Success $response")
                response?.forEach {userProdOffersViewModels->
                    productId=userProdOffersViewModels?.productId!!
                    val userPublicOffersResponse =
                        retrofitService.getProductOwnerByProductOwnerId(userProdOffersViewModels.productOwnerId)
                    val senderItemId = userPublicOffersResponse.body()?.productId
                    senderProductId=senderItemId!!
                    try { val swapOffers= retrofitService.getProductById(senderItemId).body()
                        isVisibleProgressBar.value=false
                        try { Log.e("privateItemAndOwner","Success")
                            swapOffersOfPrivateItems.add(swapOffers!!) }
                        catch (ex:Exception){
                            toastMessage.value="Fail ${ex.localizedMessage}"
                            Log.e("swapOffers","Fail ${ex.localizedMessage}") } }
                    catch (ex:Exception){
                        toastMessage.value="something went wrong,Try again"
                        Log.e("privateItemAndOwner","Fail ${ex.localizedMessage}")
                    }
                }
                if (swapOffersOfPrivateItems.isEmpty()){
                    _isEmpty.postValue(true)
                    isVisibleProgressBar.value=false }
                swappublicOffers.value=swapOffersOfPrivateItems
            }
            catch (ex:Exception){
                toastMessage.value="something went wrong,Try again"
                Log.e("getSwapOfferResponse","Fail ${ex.localizedMessage}") } }
    }
}