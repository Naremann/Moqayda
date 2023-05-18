package com.example.moqayda.ui.otherUserProfile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class OtherUserProfileVMFactory(private val ctx: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OtherUserProfileViewModel::class.java)) {
            return OtherUserProfileViewModel(ctx) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}