package com.example.moqayda.ui.home

import com.example.moqayda.models.test.CategoryItem


interface Navigator {
    fun navigateToProductListFragment(categoryItem: CategoryItem)
}