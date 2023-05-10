package com.example.moqayda.models

import com.google.gson.annotations.SerializedName

data class PrivateItemResponse(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("userPrivateItemViewModels")
	val userPrivateItemViewModels: List<PrivateItem?>? = null
)

data class PrivateItem(

	@field:SerializedName("privateItempathImage")
	val privateItempathImage: String? = null,

	@field:SerializedName("privateItemDescriptions")
	val privateItemDescriptions: String? = null,

	@field:SerializedName("privateItemName")
	val privateItemName: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
