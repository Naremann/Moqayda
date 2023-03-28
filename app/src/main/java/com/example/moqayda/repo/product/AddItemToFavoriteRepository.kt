package com.example.moqayda.repo.product

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.moqayda.api.RetrofitBuilder
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody

class AddItemToFavoriteRepository {
    val connectionError = MutableLiveData("")
    suspend fun addItemToFavorite(productId:Int,ownerADObjectId:String){
            val response = RetrofitBuilder.retrofitService.addProductToFavorite(MultipartBody.Part.createFormData(
                "text/plain".toMediaTypeOrNull().toString(), productId.toString()
            ),ownerADObjectId=MultipartBody.Part.createFormData("text/plain".toMediaTypeOrNull().toString(), ownerADObjectId))
            if(response.successful){
                Log.e("addItemToFavorite",""+response.successful)
        }
        else{
            connectionError.value=response.message
            }
    }
}