package com.example.moqayda.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Int?=null,
    val name: String?=null,
    val descriptions: String?=null,
    val pathImage: String?=null,
    val availableSince: String?=null,
    val isActive: Boolean?=null,
    val categoryId: Int?=null,
    val isFavourite: Boolean?=null,
    @SerializedName("productToSwap")
    val productToSwap: String?=null,
    val userId: String?=null,
    val productAndOwnerViewModels:List<ProductOwner?>?=null
) : Parcelable