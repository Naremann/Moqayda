package com.example.moqayda.models

import com.google.gson.annotations.SerializedName

data class FavoriteResponse(
    @SerializedName("id")
    val userId: String,
    @SerializedName("userWishlistViewModels")
    val favoriteItems: List<FavoriteItem>
)