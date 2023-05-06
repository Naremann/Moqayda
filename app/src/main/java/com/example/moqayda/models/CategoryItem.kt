package com.example.moqayda.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity
data class CategoryItem(
    val categoryBackgroundColor: Int?=null,
    val categoryProductViewModels: List<Product?>?=null,
    @PrimaryKey
    val id: Int?=null,
    val isActive: Boolean?=null,
    val name: String?=null,
    val pathImage: String?=null
) : Parcelable