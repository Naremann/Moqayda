package com.example.moqayda.repo.user

import android.content.Context
import android.net.Uri
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.convertBitmapToFile
import com.example.moqayda.repo.product.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class UserRepo(private val ctx: Context) {

    suspend fun updateUser(
        id: String,
        firstName: String,
        lastName: String,
        password: String,
        phoneNumber: String,
        country: String,
        city: String,
        address: String,
        email: String,
        imageUri: Uri?,
    ): Resource<Unit> {
        val file = imageUri?.let { convertBitmapToFile(it, ctx) }

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
        return if (response.isSuccessful) {
            Resource.Success(Unit)
        } else {
            Resource.Error(response.message())
        }
    }

    suspend fun updateUserWithCurrentImage(
        id: String,
        firstName: String,
        lastName: String,
        password: String,
        phoneNumber: String,
        country: String,
        city: String,
        address: String,
        email: String,
        imageUrl: String?,
    ): Resource<Unit> {
        val file = File(ctx.cacheDir, "image.jpg")
        val inputStream = withContext(Dispatchers.IO) {
            URL(imageUrl).openStream()
        }
        val outputStream = withContext(Dispatchers.IO) {
            FileOutputStream(file)
        }
        inputStream.copyTo(outputStream)
        withContext(Dispatchers.IO) {
            outputStream.close()
            inputStream.close()
        }

        val requestFile: RequestBody =
            RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
        val imagePart: MultipartBody.Part =
            MultipartBody.Part.createFormData("image", file.name, requestFile)
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
            imagePart
        )
        return if (response.isSuccessful) {
            Resource.Success(Unit)
        } else {
            Resource.Error(response.message())
        }
    }

}