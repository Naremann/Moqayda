package com.example.moqayda.ui.updateProduct

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UpdateProductViewModelFactory(private val ctx: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpdateProductViewModel::class.java)) {
            return UpdateProductViewModel(ctx) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
