package com.example.moqayda.ui.product_details

import com.example.moqayda.base.BaseViewModel

class ProductDetailsViewModel:BaseViewModel<Navigator>(){
    var description:String?=null
    var name:String?=null
    var productToSwapWithName:String?=null
    var navigator:Navigator?=null
    fun navigateToSwapItemsFragment(){
        navigator?.navigateToSwappingItemsFragment()
    }
}