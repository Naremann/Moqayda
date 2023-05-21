package com.example.moqayda.ui.chatRequests

import com.example.moqayda.models.AppUser

interface Navigator {
    fun onNavigateToUserProfile(user:AppUser)
}