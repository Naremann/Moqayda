package com.example.moqayda.ui.swap_public_offers_details

import androidx.databinding.ObservableField
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.AppUser
import com.example.moqayda.models.Product

class SwapPublicOffersDetailsViewModel:BaseViewModel<Navigator>() {
    var senderItemId:Int?=0
    var receiverProductId:Int?=0
    val senderPublicItem = ObservableField<Product>()
    val receiverProduct = ObservableField<Product>()
    val user = ObservableField<AppUser>()
}