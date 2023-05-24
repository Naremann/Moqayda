package com.example.moqayda.ui.sentOffers

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SentOffersVmFactory(private val ctx: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SentOffersViewModel::class.java)) {
            return SentOffersViewModel(ctx) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
