package com.example.moqayda.ui.updateProduct

import com.example.moqayda.models.Product

interface Navigator {

    fun onNavigateToSelectCategoryFragment(product: Product,isUpdate:Boolean)
    fun navigateToUserPublicItems()
}