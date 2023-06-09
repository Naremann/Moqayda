package com.example.moqayda.ui.completedBarters

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.DataUtils
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.BarteredProduct
import com.example.moqayda.models.Product
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class CompletedBartersViewModel(ctx: Context) : BaseViewModel<Navigator>() {
    private val ctxReference: WeakReference<Context> = WeakReference(ctx)

//    private val currentUser = Firebase.auth.currentUser

    private var _barters = MutableLiveData<List<BarteredProduct>>()
    val barters: LiveData<List<BarteredProduct>>
        get() = _barters


    fun postBarters(data: List<BarteredProduct>){
        _barters.postValue(data)
    }


    fun initBarters(mLD: MutableLiveData<List<BarteredProduct>>){
        _barters = mLD
    }

    var dataList = mutableListOf<BarteredProduct>()

    private val _progressBarStatus = MutableLiveData<Boolean>()
    val progressBarStatus: LiveData<Boolean>
        get() = _progressBarStatus

    init {
        getBartersOfUser()
    }


    suspend fun getProduct(productId: Int?): Product? {
        if(productId != null) {
            val productResponse = RetrofitBuilder.retrofitService.getProductById(productId)
            return try {
                if (productResponse.isSuccessful) {
                    productResponse.body()
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e("CompletedBartersVModel", "Cannot Load product ${productResponse.message()}")
                e.printStackTrace()
                null
            }
        }else{
            return null
        }
    }


    suspend fun getProductUsingProductOwnerId(productOwnerId: Int?):Product?{
        if (productOwnerId != null) {

            val response =
                RetrofitBuilder.retrofitService.getProductOwnerByProductOwnerId(productOwnerId)
            return try {
                if (response.isSuccessful) {
                    val product = getProduct(response.body()?.productId!!)
                    product
                } else {
                    Log.e(
                        "CompletedBartersVModel",
                        "Cannot Load productOwner ${response.message()}"
                    )
                    Product()
                }
            } catch (e: Exception) {
                Log.e("CompletedBartersVModel", "Cannot Load productOwner ${response.message()}")
                e.printStackTrace()
                Product()
            }
        }else{
            return null
        }

    }


    private fun getBartersOfUser() {
        dataList.clear()
        viewModelScope.launch {
            _progressBarStatus.postValue(true)
            val response = RetrofitBuilder.retrofitService.getAllBarters()
            if (response.isSuccessful) {
                Log.e("CompletedBartersVModel", "Barters Loaded Successfully")
                response.body()?.forEach { barteredProduct ->
                    val productOwnerResponse =
                        RetrofitBuilder.retrofitService.getProductOwnerByProductOwnerId(
                            barteredProduct.productOwnerId)
                    if (productOwnerResponse.isSuccessful) {
                        Log.e("CompletedBartersVModel", "productOwner Loaded Successfully")
                        if (barteredProduct.userId == DataUtils.USER?.id
                            || productOwnerResponse.body()?.userId == DataUtils.USER?.id) {
                            dataList.add(barteredProduct) }
                    } else {
                        Log.e("CompletedBartersVModel",
                            "Failed to load product owner ${productOwnerResponse.message()}") }
                }
            } else {
                Log.e("CompletedBartersVModel",
                    "Failed to load Barters ${response.message()}")
            }
            _barters.postValue(dataList)
            _progressBarStatus.postValue(false)
        }
    }


}