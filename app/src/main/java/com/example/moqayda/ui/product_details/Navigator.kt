package com.example.moqayda.ui.product_details

import com.example.moqayda.models.AppUser

interface Navigator {
    fun navigateToSwappingItemsFragment()
    fun onNavigateToUserProfile(appUser: AppUser)
}