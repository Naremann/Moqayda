package com.example.moqayda.ui.selectCategory

import com.example.moqayda.models.CategoryItem
import com.example.moqayda.models.Product


interface Navigator {
    fun onNavigateToAddProductFragment(categoryItem: CategoryItem)
    fun onNavigateToUpdateProductFragment(categoryId: Int,product: Product)
}