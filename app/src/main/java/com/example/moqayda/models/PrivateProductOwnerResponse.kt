package com.example.moqayda.models

import com.google.gson.annotations.SerializedName

data class PrivateProductOwnerResponse(

	@field:SerializedName("privateItemId")
	val privateItemId: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("userId")
	val userId: String? = null
)
