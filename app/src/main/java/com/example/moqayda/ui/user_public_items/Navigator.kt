package com.example.moqayda.ui.user_public_items

import com.example.moqayda.models.CategoryItem
import com.example.moqayda.models.Product

interface Navigator {

    fun onNavigateToProductDetails(product: Product)
    fun onNavigateTOUpdateProductFragment(product: Product,categoryId:Int)
}