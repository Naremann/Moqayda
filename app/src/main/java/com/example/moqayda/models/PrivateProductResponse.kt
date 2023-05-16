package com.example.moqayda.models

import com.google.gson.annotations.SerializedName

data class PrivateItemResponse(

	@field:SerializedName("PrivateProductResponse")
	val privateProductResponse: List<PrivateItem?>? = null
)

data class PrivateItem(

	@field:SerializedName("pathImage")
	val pathImage: String? = null,

	@field:SerializedName("privateItemAndOwnerViewModels")
	val privateItemAndOwnerViewModels: List<PrivateItemAndOwnerViewModelsItem?>? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("descriptions")
	val descriptions: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null
)

data class PrivateItemAndOwnerViewModelsItem(

	@field:SerializedName("privateItemOwnerId")
	val privateItemOwnerId: Int? = null
)
