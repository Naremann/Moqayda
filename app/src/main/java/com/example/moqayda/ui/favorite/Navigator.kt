package com.example.moqayda.ui.favorite

import com.example.moqayda.models.Product

interface Navigator {
    fun onNavigateToProductDetails(product: Product)
}