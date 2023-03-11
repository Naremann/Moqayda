package com.example.moqayda.repo.product

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.example.moqayda.api.RetrofitBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

@SuppressLint("StaticFieldLeak")
class AddProductRepository constructor(private val ctx:Context) {



    val connectionError = MutableLiveData("")
    val serverResponse = MutableLiveData("")



    fun restAddProductVariables() {
        connectionError.postValue("")
        serverResponse.postValue("")

    }

    suspend fun uploadProduct(
        productName: String,
        productDescription: String,
        categoryId: String,
        imageUri: Uri,
        fileRealPath: String,
    ) {
        val fileToSend = prepareFilePart("image", fileRealPath, imageUri)
        val productNameRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(),productName)
        val productDescriptionRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(),productDescription)
        val categoryIdRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(),categoryId)



        withContext(Dispatchers.IO){
            val response = RetrofitBuilder.retrofitService.addProduct(
                productNameRequestBody,
                productDescriptionRequestBody,
                categoryIdRequestBody,
                fileToSend
            )
            if(response.body() != null && response.isSuccessful) {
                try {
                    if (response.code() == 201) {
                        Log.e("AddProductRepository",response.message())
                        serverResponse.postValue("File uploaded")

                    } else {
                        connectionError.postValue("Failed to upload " + response.message().toString())
                        Log.e("AddProductRepository",response.message())
                    }
                } catch (e: Exception) {
                    connectionError.postValue(e.message.toString())
                    Log.e("AddProductRepository",response.message())
                }
            }else{
                connectionError.postValue("Failed to upload ")
            }

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