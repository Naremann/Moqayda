package com.example.moqayda.models

import com.google.gson.annotations.SerializedName

data class SwapResponse(

	@field:SerializedName("userPrivateOffersViewModels")
	val userPrivateOffersViewModels: List<UserPrivateOffersViewModelsItem?>? = null,

	@field:SerializedName("id")
	val id: String? = null
)

data class UserPrivateOffersViewModelsItem(

	@field:SerializedName("productId")
	val productId: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("privateItemOwnerId")
	val privateItemOwnerId: Int? = null
)
