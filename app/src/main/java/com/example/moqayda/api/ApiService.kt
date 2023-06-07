package com.example.moqayda.api

import com.example.moqayda.models.*
import com.example.moqayda.ui.swa_public_offers.SwapOffersOfPublicItemsFragment
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
        @Part("ProductToSwap") productToSwap: RequestBody,
        @Part file: MultipartBody.Part?,

        ): Response<ResponseBody>

    @GET("/api/Category/{id}")
    suspend fun getProductsByCategoryId(@Path("id") categoryId:Int?):Response<CategoryItem>

    @GET("/api/Product/{id}")
    suspend fun getProductById(@Path("id") productId:Int?):Response<Product>

    @GET("/api/PrivateItem")
    suspend fun getAllPrivateProducts():Response<PrivateItemsResponse>



    @DELETE("api/Wishlist/{id}")
    suspend fun deleteFavoriteProductById(@Path("id") favoriteItemId: Int) : Response<Unit>

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
    suspend fun getPrivateProductByUserId(@Path("id") userId:String?):PrivateProductByIdtResponse

    @GET("/api/Wishlist/{id}")
    suspend fun getFavoriteItems(@Path("id") userId:String?):Response<FavoriteResponse>

    @POST("/api/Wishlist")
    suspend fun addProductToFavorite(@Body favoriteItem: FavoriteItem) : Response<ResponseBody>

    @POST("/api/PrivToSwap")
    suspend fun sendRequestToSwapPrivateItem(
        @Body swapPrivateItem: SwapPrivateItemResponse
    ):Response<ResponseBody>



    @POST("/api/ProdToSwap")
    suspend fun sendSwapRequestOfPublicItem(
        @Body swapPublicItem: SwapPublicItem
    ):Response<ResponseBody>

    @POST("/api/ProductOwner")
    suspend fun addProductOwner(
        @Body productOwnerItem: ProductOwnerItem
    ):Response<ResponseBody>

    @GET("/api/PrivToSwap/{id}")
    suspend fun getSwapOffersBuUserId(
        @Path("id") userId:String?
    ):SwapResponse

    @GET("/api/ProdToSwap/{id}")
    suspend fun getSwapPublicOffersBuUserId(
        @Path("id") userId:String?
    ):Response<ProductToSwapByUserIdResponse>

    @GET("/api/PrivateItem/{id}")
    suspend fun getPrivateProductByItemId(
        @Path("id") privateItemId:Int?
    ):PrivateProductOwnerByIdResponse

    @DELETE("/api/PrivateItem/{id}")
    suspend fun deletePrivateItemById(@Path("id") privateItemId:Int?):Response<ResponseBody>


    @DELETE("/api/PrivateItemOwner/{id}")
    suspend fun deletePrivateItemOwnerById(@Path("id") privateItemId:Int?):Response<ResponseBody>

    @GET("/api/PrivateItemOwner/{id}")
    suspend fun getPrivateProductOwnerByPrivateItemOwnerId(
        @Path("id") productId:Int?
    ):PrivateItemOwnerByIDResponse

    @GET("/api/ProductOwner/{id}")
    suspend fun getProductOwnerByProductOwnerId(
        @Path("id") productOwner:Int?
    ):Response<ProductOwnerItem>


    @POST("/api/PrivateItemOwner")
    suspend fun addPrivateItemOwner(
        @Body productOwnerItem: PrivateProductOwnerResponse
    ):Response<ResponseBody>

    @DELETE("/api/Product/{id}")
    suspend fun deleteProduct(@Path("id") id:Int) : Response<ResponseBody>

    @DELETE("/api/ProductOwner/{id}")
    suspend fun deleteProductOwner(@Path("id") id:Int) : Response<ResponseBody>




    @Multipart
    @PUT("/api/Product/{id}")
    suspend fun updateProduct(
        @Path("id") id: String,
        @Part("Id") Id: RequestBody?,
        @Part("Name") productName: RequestBody?,
        @Part("Descriptions") productDescription: RequestBody?,
        @Part("IsActive") isActive:RequestBody?,
        @Part("CategoryId") categoryId: RequestBody?,
        @Part("ProductBackgroundColor") ProductBackgroundColor:RequestBody?,
        @Part("ProductToSwap") productToSwap: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Response<ResponseBody>


    @POST("/api/BarteredProduct")
    suspend fun addPublicBarteredProduct(@Body swapOffer:SwapPublicItem):Response<ResponseBody>


    @DELETE("/api/ProdToSwap/{id}")
    suspend fun deletePublicOffer(@Path("id") id:Int) : Response<ResponseBody>


    @GET("/api/BarteredProduct/all")
    suspend fun getAllBarters() : Response<List<BarteredProduct>>

    @GET("/api/ProdToSwap/all")
    suspend fun getAllOffers(): Response<List<SwapPublicItem>>

    @GET("/api/Wishlist/all")
    suspend fun getAllFavoriteItems():Response<List<FavoriteItem>>

    @POST("/api/Block")
    suspend fun blockUser(@Body userBlockage: UserBlockage) : Response<ResponseBody>
    @GET("/api/Block")
    suspend fun getBlockedUsers():Response<List<UserBlockage>>
    @DELETE("/api/Block/{id}")
    suspend fun unBlockUser(@Path("id") id:Int):Response<ResponseBody>

}