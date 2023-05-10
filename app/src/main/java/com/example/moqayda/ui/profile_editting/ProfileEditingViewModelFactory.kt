package com.example.moqayda.ui.profile_editting

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProfileEditingViewModelFactory(private val ctx: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileEditingViewModel::class.java)) {
            return ProfileEditingViewModel(ctx) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}