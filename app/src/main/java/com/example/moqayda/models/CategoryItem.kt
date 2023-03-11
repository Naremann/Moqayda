package com.example.moqayda.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.moqayda.models.test.CategoryItem
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

data class Category(

    @field:SerializedName("Category")
    val category: List<CategoryItem>? = null
)

/*@Entity
@Parcelize
data class CategoryItem(

    @ColumnInfo
    @field:SerializedName("pathImage")
    val pathImage: String? = null,

    @ColumnInfo
    @field:SerializedName("categoryBackgroundColor")
    val categoryBackgroundColor: Int? = null,

    @ColumnInfo
    @field:SerializedName("name")
    val name: String? = null,

    @ColumnInfo
    @PrimaryKey
    @field:SerializedName("id")
    val id: Int? = null,

    @ColumnInfo
    @field:SerializedName("categoryProductViewModels")
    val categoryProductViewModels:@RawValue Product? = null,

    @field:SerializedName("isActive")
    val isActive: Boolean? = null
) : Parcelable{
    @Ignore
    constructor() : this("") {
    }
}*/

/*@Parcelize
@Entity
data class CategoryItem(
    @PrimaryKey
    val id: Int?,
    val name: String,
    val isActive: Boolean,
    val pathImage: String?,
    val categoryBackgroundColor: Int,
    val categoryProductViewModels: List<Product>?
) : Parcelable*/