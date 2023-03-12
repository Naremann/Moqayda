package com.example.moqayda.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class CategoryItem(
    val categoryBackgroundColor: Int?=null,
    val categoryProductViewModels: List<CategoryProductViewModel>?=null,
    @PrimaryKey
    val id: Int?=null,
    val isActive: Boolean?=null,
    val name: String?=null,
    val pathImage: String?=null
)