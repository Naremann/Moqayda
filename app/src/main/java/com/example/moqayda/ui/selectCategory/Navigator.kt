package com.example.moqayda.ui.selectCategory

import com.example.moqayda.models.test.CategoryItem


interface Navigator {
    fun onNavigateToAddProductFragment(categoryId:Int,categoryPathImage:String,categoryBackgroundColor:Int)
}