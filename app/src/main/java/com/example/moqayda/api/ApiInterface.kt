package com.example.moqayda.api

import com.example.moqayda.models.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("/api/Category")
    suspend fun getAllCategories(): Response<CategoryResponse>

    @GET("/api/Product")
    suspend fun getProductsByCategoryId(@Query("categoryId") categoryId:Int?): Response<ProductResponse>

}