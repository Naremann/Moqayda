package com.example.moqayda.ui.blockedUsers

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BlockedUsersVMFactory(private val ctx: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BlockedUsersViewModel::class.java)) {
            return BlockedUsersViewModel(ctx) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
