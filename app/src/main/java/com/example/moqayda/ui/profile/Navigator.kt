package com.example.moqayda.ui.profile

import com.example.moqayda.models.AppUser

interface Navigator {
    fun navigateToLoginFragment()
    fun navigateToProfileEditing(currentUser: AppUser)
    fun startFullImageScreen()
    fun navigateToSettingFragment()
    fun navigateToPrivateProducts()
    fun navigateToUserPublicProducts()
    fun navigateToSwapPrivateOffersFragment()
    fun navigateToSwapPublicOffersFragment()

    fun onNavigateToCompletedBarterFragment()
    fun onNavigateToSentOffersFragment()
}