package com.example.moqayda.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class CategoryItem(
    @PrimaryKey
    val id: Int?,
    val name: String,
    val isActive: Boolean,
    val pathImage: String?,
    val categoryBackgroundColor: Int,
    val categoryProductViewModels: List<Product>?
) : Parcelable