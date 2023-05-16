package com.example.moqayda.models

import com.google.gson.annotations.SerializedName

data class PrivateProductOwnerByIdResponse(

	@field:SerializedName("pathImage")
	val pathImage: String? = null,

	@field:SerializedName("privateItemAndOwnerViewModels")
	val privateItemAndOwnerViewModels: List<PrivateItemAndOwnerViewModelsItems?>? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("descriptions")
	val descriptions: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null
)

data class PrivateItemAndOwnerViewModelsItems(

	@field:SerializedName("privateItemId")
	val privateItemId: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("userId")
	val userId: String? = null
)
