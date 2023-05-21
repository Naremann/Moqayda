package com.example.moqayda.ui.swap_public_offers_details

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SwapPublicOffersDetailsVMFactory(private val ctx: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SwapPublicOffersDetailsViewModel::class.java)) {
            return SwapPublicOffersDetailsViewModel(ctx) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}