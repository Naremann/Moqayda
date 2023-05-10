package com.example.moqayda.api

import com.example.moqayda.models.*
import com.example.moqayda.models.CategoryItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("/api/Category")
    suspend fun getAllCategories(): Response<CategoryResponse>

    @Multipart
    @POST("/api/Product")
    suspend fun addProduct(
        @Part("Name") productName: RequestBody?,
        @Part("Descriptions") productDescription: RequestBody?,
        @Part("CategoryId") categoryId: RequestBody?,
        @Part("UserId") userId: RequestBody,
        @Part file: MultipartBody.Part?

    ): Response<ResponseBody>

    @GET("/api/Category/{id}")
    suspend fun getProductsByCategoryId(@Path("id") categoryId:Int?):Response<CategoryItem>

    @GET("/api/Product/{id}")
    suspend fun getProductById(@Path("id") productId:Int?):Response<Product>

    @Multipart
    @POST("/api/Wishlist")
    suspend fun addProductToFavorite(
        @Part productId:MultipartBody.Part?,
        @Part ownerADObjectId:MultipartBody.Part?
    ):BasicApiResponse<Unit>

    @GET("/api/Wishlist/all")
    suspend fun getWishlist(): Response<WishlistResponse>

    @DELETE("api/Wishlist/{id}")
    suspend fun deleteFavoriteProductById(@Path("id") productId:Int) : Response<Unit>

    @GET("/api/User/{id}")
    suspend fun getUserById(@Path("id") userId: String?):Response<AppUser>

    @Multipart
    @POST("/api/User")
    suspend fun addUser(
        @Part("Id") id: RequestBody?,
        @Part("firstName") firstName: RequestBody?,
        @Part("lastName") lastName: RequestBody?,
        @Part("password") password: RequestBody?,
        @Part("phoneNumber") phoneNumber: RequestBody?,
        @Part("country") country: RequestBody,
        @Part("city") city: RequestBody,
        @Part("address") address: RequestBody,
        @Part("email") email: RequestBody,
        @Part image: MultipartBody.Part?

    ): Response<ResponseBody>



    @Multipart
    @POST("/api/PrivateItem")
    suspend fun addPrivateProduct(
        @Part("Name") productName: RequestBody?,
        @Part("Descriptions") productDescription: RequestBody?,
        @Part("UserId") userId: RequestBody,
        @Part productImage: MultipartBody.Part
    ):  BasicApiResponse<Unit>


    @Multipart
    @PUT("/api/User/{id}")
    suspend fun updateUser(
        @Path("id") id: String,
        @Part("Id") Id: RequestBody?,
        @Part("firstName") firstName: RequestBody?,
        @Part("lastName") lastName: RequestBody?,
        @Part("password") password: RequestBody?,
        @Part("phoneNumber") phoneNumber: RequestBody?,
        @Part("country") country: RequestBody,
        @Part("city") city: RequestBody,
        @Part("address") address: RequestBody,
        @Part("email") email: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<ResponseBody>

    @GET("/api/PrivateItem/{id}")
    suspend fun getPrivateProductByUserId(@Path("id") userId:String?):PrivateItemResponse



}