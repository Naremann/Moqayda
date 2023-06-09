package com.example.moqayda.repo.product

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.convertBitmapToFile
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

@SuppressLint("StaticFieldLeak")
class ProductRepository constructor(private val ctx: Context) {


    val connectionError = MutableLiveData("")
    val serverResponse = MutableLiveData("")


    fun restAddProductVariables() {
        connectionError.postValue("")
        serverResponse.postValue("")

    }

    suspend fun uploadProduct(
        productName: String,
        productDescription: String,
        productToSwap: String,
        categoryId: String,
        imageUri: Uri,
        fileRealPath: String,
        userId: String,
    ) {
        val fileToSend = prepareFilePart("image", fileRealPath, imageUri)
        val productNameRequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), productName)
        val productDescriptionRequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), productDescription)
        val categoryIdRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), categoryId)
        val userIdRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), userId)
        val productToSwapRequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), productToSwap)


        withContext(Dispatchers.IO) {
            val response = RetrofitBuilder.retrofitService.addProduct(
                productNameRequestBody,
                productDescriptionRequestBody,
                categoryIdRequestBody,
                userIdRequestBody,
                productToSwapRequestBody,
                fileToSend
            )
            if (response.body() != null && response.isSuccessful) {
                try {
                    if (response.code() == 201) {
                        Log.e("AddProductRepository", response.message())
                        serverResponse.postValue("File uploaded")

                    } else {
                        connectionError.postValue("Failed to upload " + response.message()
                            .toString())
                        Log.e("AddProductRepository", response.message())
                    }
                } catch (e: Exception) {
                    connectionError.postValue(e.message.toString())
                    Log.e("AddProductRepository", response.message())
                }
            } else {
                connectionError.postValue("Failed to upload ")
            }

        }

    }


    suspend fun updateProduct(
        id: String,
        productName: String?,
        productDescription: String,
        IsActive:String? = "true",
        categoryId: String,
        ProductBackgroundColor: String,
        ProductToSwap: String,
        imageUri: Uri?,
    ): Resource<Unit> {
        val file = imageUri?.let { convertBitmapToFile(it, ctx) }

        val response = RetrofitBuilder.retrofitService.updateProduct(
            id,
            id.toRequestBody("text/plain".toMediaTypeOrNull()),
            productName?.toRequestBody("text/plain".toMediaTypeOrNull()),
            productDescription.toRequestBody("text/plain".toMediaTypeOrNull()),
            IsActive?.toRequestBody("text/plain".toMediaTypeOrNull()),
            categoryId.toRequestBody("text/plain".toMediaTypeOrNull()),
            ProductBackgroundColor.toRequestBody("text/plain".toMediaTypeOrNull()),
            ProductToSwap.toRequestBody("text/plain".toMediaTypeOrNull()),
            MultipartBody.Part.createFormData("image", "image.jpg", file!!.asRequestBody())
        )
        return if (response.isSuccessful) {
            Resource.Success(Unit)
        } else {
            Resource.Error(response.message())
        }
    }

    suspend fun updateProductWithCurrentImage(
        id: String,
        productName: String,
        productDescription: String,
        IsActive:String? = "true",
        categoryId: String,
        ProductBackgroundColor: String,
        ProductToSwap: String,
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
        val response = RetrofitBuilder.retrofitService.updateProduct(
            id,
            id.toRequestBody("text/plain".toMediaTypeOrNull()),
            productName.toRequestBody("text/plain".toMediaTypeOrNull()),
            productDescription.toRequestBody("text/plain".toMediaTypeOrNull()),
            IsActive?.toRequestBody("text/plain".toMediaTypeOrNull()),
            categoryId.toRequestBody("text/plain".toMediaTypeOrNull()),
            ProductBackgroundColor.toRequestBody("text/plain".toMediaTypeOrNull()),
            ProductToSwap.toRequestBody("text/plain".toMediaTypeOrNull()),
            imagePart
        )
        return if (response.isSuccessful) {
            Resource.Success(Unit)
        } else {
            Resource.Error(response.message())
        }

    }


    private fun prepareFilePart(
        partName: String,
        fileRealPath: String,
        fileUri: Uri,
    ): MultipartBody.Part {
        val file = File(fileRealPath)
        val requestFile: RequestBody = RequestBody.create(
            ctx.contentResolver.getType(fileUri)!!.toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }



}