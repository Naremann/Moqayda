package com.example.moqayda

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController

fun androidx.appcompat.widget.Toolbar.initToolbar(
    toolbar: androidx.appcompat.widget.Toolbar,title:String?=null,fragment: Fragment) {
    toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
    toolbar.title = title
    toolbar.setNavigationOnClickListener {
        onBackPressed(fragment)
    }
}

fun onBackPressed(fragment: Fragment) {
    NavHostFragment.findNavController(fragment).popBackStack()
}
