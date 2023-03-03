package com.example.moqayda.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Products(
	val id: Int,
	val name: String,
	val descriptions: String,
	val pathImage: String,
	val availableSince: String,
	val isActive: Boolean,
	val categoryId: Int
) : Parcelable
/*data class Products(

	@field:SerializedName("Products")
	val products: List<ProductsItem?>? = null
)

data class ProductsItem(

	@field:SerializedName("pathImage")
	val pathImage: String? = null,

	@field:SerializedName("availableSince")
	val availableSince: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("isActive")
	val isActive: Boolean? = null,

	@field:SerializedName("descriptions")
	val descriptions: String? = null,

	@field:SerializedName("categoryId")
	val categoryId: Int? = null,

	@field:SerializedName("productBackgroundColor")
	val productBackgroundColor: Int? = null
)*/
