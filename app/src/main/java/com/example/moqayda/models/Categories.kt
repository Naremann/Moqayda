package com.example.moqayda.models

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
data class Categories(

	@field:SerializedName("Categories")
	val categories: List<CategoriesItem?>? = null
)

data class CategoriesItem(

	@field:SerializedName("pathImage")
	val pathImage: String? = null,

	@field:SerializedName("categoryBackgroundColor")
	val categoryBackgroundColor: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("categoryProductViewModels")
	val categoryProductViewModels: Any? = null,

	@field:SerializedName("isActive")
	val isActive: Boolean? = null
)
