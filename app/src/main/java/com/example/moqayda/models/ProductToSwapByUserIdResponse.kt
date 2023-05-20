package com.example.moqayda.models

import com.google.gson.annotations.SerializedName

data class ProductToSwapByUserIdResponse(

	@field:SerializedName("userProdOffersViewModels")
	val userProdOffersViewModels: List<UserProdOffersViewModelsItem?>? = null,

	@field:SerializedName("id")
	val id: String? = null
)

data class UserProdOffersViewModelsItem(

	@field:SerializedName("productId")
	val productId: Int? = null,

	@field:SerializedName("productOwnerId")
	val productOwnerId: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
