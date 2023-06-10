package com.example.moqayda.ui.sentOffers

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.R
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.Product
import com.example.moqayda.models.SwapPublicItem
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class SentOffersViewModel(ctx: Context) : BaseViewModel<Navigator>() {


    private val ctxReference: WeakReference<Context> = WeakReference(ctx)

    private val currentUser = Firebase.auth.currentUser

    private val _sentOffers = MutableLiveData<List<SwapPublicItem>>()
    val sentOffers: LiveData<List<SwapPublicItem>>
        get() = _sentOffers


    private val dataList = mutableListOf<SwapPublicItem>()

    private val _progressBarStatus = MutableLiveData<Boolean>()
    val progressBarStatus: LiveData<Boolean>
        get() = _progressBarStatus


    private val _progressBareStatus = MutableLiveData<Boolean>()
    val progressBar: LiveData<Boolean>
        get() = _progressBareStatus

    init {
        getBartersOfUser()
    }


    private val _isEmpty = MutableLiveData<Boolean>(false)
    val isEmpty: LiveData<Boolean>
        get() = _isEmpty

    suspend fun getProduct(productId: Int): Product? {
        val productResponse = RetrofitBuilder.retrofitService.getProductById(productId)
        return try {
            if (productResponse.isSuccessful) {
                productResponse.body()
            } else {
                Product()
            }
        } catch (e: Exception) {
            Log.e("SentOffersViewModel", "Cannot Load product ${productResponse.message()}")
            e.printStackTrace()
            Product()
        }
    }


    suspend fun getProductUsingProductOwnerId(productOwnerId: Int): Product? {
        val response =
            RetrofitBuilder.retrofitService.getProductOwnerByProductOwnerId(productOwnerId)
        return try {
            if (response.isSuccessful) {
                val product = getProduct(response.body()?.productId!!)
                product
            } else {
                Log.e("SentOffersViewModel", "Cannot Load productOwner ${response.message()}")
                Product()
            }
        } catch (e: Exception) {
            Log.e("SentOffersViewModel", "Cannot Load productOwner ${response.message()}")
            e.printStackTrace()
            Product()
        }
    }


    private fun getBartersOfUser() {
        dataList.clear()
        viewModelScope.launch {
            _progressBarStatus.postValue(true)
            val response = RetrofitBuilder.retrofitService.getAllOffers()
            if (response.isSuccessful) {
                Log.e("SentOffersViewModel", "Barters Loaded Successfully")
                response.body()?.forEach { swapOffer ->
                    val productOwnerResponse =
                        RetrofitBuilder.retrofitService.getProductOwnerByProductOwnerId(
                            swapOffer.productOwnerId
                        )
                    if (productOwnerResponse.isSuccessful) {
                        Log.e("SentOffersViewModel", "productOwner Loaded Successfully")
                        if (productOwnerResponse.body()?.userId == currentUser?.uid) {
                            dataList.add(swapOffer)
                        }
                    } else {
                        Log.e(
                            "SentOffersViewModel",
                            "Failed to load product owner ${productOwnerResponse.message()}"
                        )
                    }
                }
            } else {
                Log.e(
                    "SentOffersViewModel",
                    "Failed to load Barters ${response.message()}"
                )
            }
            _sentOffers.postValue(dataList)
            _progressBarStatus.postValue(false)
            if (dataList.isEmpty()) {
                _isEmpty.postValue(true)
            }
        }

    }


    fun deleteOffer(offerId: Int){
        _progressBarStatus.postValue(true)
        viewModelScope.launch {
            val response = RetrofitBuilder.retrofitService.deletePublicOffer(offerId)
            if (response.isSuccessful){
                _progressBarStatus.postValue(false)
                messageLiveData.postValue(ctxReference.get()?.getString(R.string.request_canceled))
                getBartersOfUser()
            }else{
                _progressBarStatus.postValue(false)
                Log.e("SentOffersViewModel",response.message())
                messageLiveData.postValue(ctxReference.get()?.getString(R.string.failure_message))
            }
        }
    }

}