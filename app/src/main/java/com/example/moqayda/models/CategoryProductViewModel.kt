package com.example.moqayda.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryProductViewModel(
    val availableSince: String?=null,
    val categoryId: Int?=null,
    val descriptions: String?=null,
    val id: Int?=null,
    val isActive: Boolean?=null,
    val isFavourite: Boolean?=null,
    val name: String?=null,
    val pathImage: String?=null,
    val productToSwap: String?=null
) : Parcelable