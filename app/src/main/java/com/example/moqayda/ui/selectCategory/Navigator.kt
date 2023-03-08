package com.example.moqayda.ui.selectCategory

import com.example.moqayda.models.CategoryItem

interface Navigator {
    fun onNavigateToAddProductFragment(category: CategoryItem)
}