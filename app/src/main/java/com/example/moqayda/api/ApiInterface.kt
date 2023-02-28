package com.example.moqayda.api

import com.example.moqayda.models.CategoryItem
import com.example.moqayda.models.CategoryResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("/api/Category")
    suspend fun getAllCategories(): Response<CategoryResponse>

    @GET("/api/Category")
    suspend fun getCategories(): List<CategoryItem>
}