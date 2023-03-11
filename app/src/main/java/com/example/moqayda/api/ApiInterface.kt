package com.example.moqayda.api

import com.example.moqayda.models.*
import com.example.moqayda.models.test.CategoryItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @GET("/api/Category")
    suspend fun getAllCategories(): Response<CategoryResponse>

    @Multipart
    @POST("/api/Product")
    suspend fun addProduct(
        @Part("Name") productName: RequestBody?,
        @Part("Descriptions") productDescription: RequestBody?,
        @Part("CategoryId") categoryId: RequestBody?,
        @Part file: MultipartBody.Part?
    ): Response<ResponseBody>

    @GET("/api/Category/{id}")
    suspend fun getProductsByCategoryId(@Path("id") categoryId:Int?):Response<CategoryItem>

//    @GET("/api/Category/3")
//    suspend fun getProductsByCategoryId(): Response<CategoryItemTest>



}