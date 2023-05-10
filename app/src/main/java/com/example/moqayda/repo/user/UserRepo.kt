package com.example.moqayda.repo.user

import android.content.Context
import android.net.Uri
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.convertBitmapToFile
import com.example.moqayda.repo.product.Resource
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class UserRepo(private val ctx: Context) {

    suspend fun updateUser(
        id:String,
        firstName:String,
        lastName:String,
        password:String,
        phoneNumber:String,
        country:String,
        city:String,
        address:String,
        email:String,
        imageUri: Uri?
    ):Resource<Unit> {
        val file = imageUri?.let { convertBitmapToFile(it,ctx) }

        val response = RetrofitBuilder.retrofitService.updateUser(
            id,
            id.toRequestBody("text/plain".toMediaTypeOrNull()),
            firstName.toRequestBody("text/plain".toMediaTypeOrNull()),
            lastName.toRequestBody("text/plain".toMediaTypeOrNull()),
            password.toRequestBody("text/plain".toMediaTypeOrNull()),
            phoneNumber.toRequestBody("text/plain".toMediaTypeOrNull()),
            country.toRequestBody("text/plain".toMediaTypeOrNull()),
            city.toRequestBody("text/plain".toMediaTypeOrNull()),
            address.toRequestBody("text/plain".toMediaTypeOrNull()),
            email.toRequestBody("text/plain".toMediaTypeOrNull()),
            MultipartBody.Part.createFormData("image", "image.jpg", file!!.asRequestBody())
        )
        return if (response.isSuccessful){
            Resource.Success(Unit)
        }else{
            Resource.Error(response.message())
        }
    }
}