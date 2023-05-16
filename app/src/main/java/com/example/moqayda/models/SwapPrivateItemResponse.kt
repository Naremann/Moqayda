package com.example.moqayda.models

import com.google.gson.annotations.SerializedName

data class SwapPrivateItemResponse(

	@field:SerializedName("productId")
	val productId: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("privateItemOwnerId")
	val privateItemOwnerId: Int? = null
)
