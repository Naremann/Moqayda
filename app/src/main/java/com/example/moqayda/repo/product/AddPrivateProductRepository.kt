package com.example.moqayda.repo.product

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.bumptech.glide.load.HttpException
import com.example.moqayda.api.RetrofitBuilder.retrofitService
import com.example.moqayda.convertBitmapToFile
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import javax.inject.Inject


@SuppressLint("StaticFieldLeak")
class AddPrivateProductRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    @RequiresApi(Build.VERSION_CODES.Q)
    suspend fun uploadPrivateProduct(
        name: String,
        descriptions: String,
        userId: String,
        imageUri: Uri?,
    ): Resource<Unit> {

        val file = imageUri?.let { convertBitmapToFile(it, context) }

        return try {
            val response = retrofitService.addPrivateProduct(
                name.toRequestBody("text/plain".toMediaTypeOrNull()),
                descriptions.toRequestBody("text/plain".toMediaTypeOrNull()),
                userId.toRequestBody("text/plain".toMediaTypeOrNull()),

                productImage =
                MultipartBody.Part.createFormData("image", "image.jpg",
                    file?.asRequestBody()!!)

            )

            if (response.successful) {
                Resource.Success(Unit)
            } else {

                Resource.Success(Unit)
            }

        } catch (e: IOException) {
            Resource.Error(e.localizedMessage)

        } catch (e: HttpException) {
            Resource.Error(
                "Oops! something went wrong. Please try again : ${e.localizedMessage}"
            )
        }
    }

}

