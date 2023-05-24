package com.example.moqayda.ui.completedBarters

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CompletedBartersViewModelFactory(private val ctx: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CompletedBartersViewModel::class.java)) {
            return CompletedBartersViewModel(ctx) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}