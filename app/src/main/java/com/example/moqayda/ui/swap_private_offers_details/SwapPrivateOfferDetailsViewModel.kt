package com.example.moqayda.ui.swap_private_offers_details

import androidx.databinding.ObservableField
import com.example.moqayda.base.BaseViewModel
import com.example.moqayda.models.AppUser
import com.example.moqayda.models.PrivateProductOwnerByIdResponse
import com.example.moqayda.models.Product

class SwapPrivateOfferDetailsViewModel:BaseViewModel<Navigator>() {
    var privateItemId:Int?=0
    var productId:Int?=0
    val privateItem =ObservableField<PrivateProductOwnerByIdResponse>()
    val product = ObservableField<Product>()
    val user = ObservableField<AppUser>()

}