package com.example.moqayda.ui.completedBarters

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.BarteredProduct
import com.example.moqayda.models.Product
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class CompletedBartersViewModel(ctx: Context) : BaseViewModel<Navigator>() {
    private val ctxReference: WeakReference<Context> = WeakReference(ctx)

    private val currentUser = Firebase.auth.currentUser

    private val _barteredProduct = MutableLiveData<List<BarteredProduct>>()
    val barteredProduct: LiveData<List<BarteredProduct>>
        get() = _barteredProduct


    private val dataList = mutableListOf<BarteredProduct>()

    private val _progressBarStatus = MutableLiveData<Boolean>()
    val progressBarStatus: LiveData<Boolean>
        get() = _progressBarStatus

    init {
        getBartersOfUser()
    }


    suspend fun getProduct(productId: Int): Product? {
        val productResponse = RetrofitBuilder.retrofitService.getProductById(productId)
        return try {
            if (productResponse.isSuccessful) {
                productResponse.body()
            } else {
                Product()
            }
        } catch (e: Exception) {
            Log.e("CompletedBartersVModel", "Cannot Load product ${productResponse.message()}")
            e.printStackTrace()
            Product()
        }
    }


    suspend fun getProductUsingProductOwnerId(productOwnerId:Int):Product?{
        val response = RetrofitBuilder.retrofitService.getProductOwnerByProductOwnerId(productOwnerId)
        return try {
            if (response.isSuccessful) {
                val product = getProduct(response.body()?.productId!!)
                product
            }else{
                Log.e("CompletedBartersVModel", "Cannot Load productOwner ${response.message()}")
                Product()
            }
        }catch (e:Exception){
            Log.e("CompletedBartersVModel", "Cannot Load productOwner ${response.message()}")
            e.printStackTrace()
            Product()
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
                            barteredProduct.productOwnerId
                        )
                    if (productOwnerResponse.isSuccessful) {
                        Log.e("CompletedBartersVModel", "productOwner Loaded Successfully")
                        if (barteredProduct.userId == currentUser?.uid || productOwnerResponse.body()?.userId == currentUser?.uid) {
                            dataList.add(barteredProduct)
                        }
                    } else {
                        Log.e(
                            "CompletedBartersVModel",
                            "Failed to load product owner ${productOwnerResponse.message()}"
                        )
                    }
                }
            } else {
                Log.e(
                    "CompletedBartersVModel",
                    "Failed to load Barters ${response.message()}"
                )
            }
            _barteredProduct.postValue(dataList)
            _progressBarStatus.postValue(false)
        }

    }
}