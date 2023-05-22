package com.example.moqayda.ui.swap_public_item_request

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SWPItemReqVMFactory(private val ctx: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SwapPublicItemViewModel::class.java)) {
            return SwapPublicItemViewModel(ctx) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}