package com.example.moqayda.ui.home

import com.example.moqayda.models.CategoryItem


interface Navigator {
    fun navigateToProductListFragment(categoryItem: CategoryItem)
}