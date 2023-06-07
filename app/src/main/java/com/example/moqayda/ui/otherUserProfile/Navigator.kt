package com.example.moqayda.ui.otherUserProfile

import com.example.moqayda.models.Product

interface Navigator {
    fun onStartFullImageScreen()
    fun onNavigateToProductDetails(product: Product)
    fun onNavigateToHomeFragment()
}