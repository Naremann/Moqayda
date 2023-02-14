package com.example.moqayda.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moqayda.R
import com.example.moqayda.models.CategoryItem
import com.example.moqayda.models.CategoryResponse

class HomViewModel : ViewModel() {


    private val _categoryList = MutableLiveData<List<CategoryItem>>()
    val categoryList: LiveData<List<CategoryItem>>
        get() = _categoryList


    private val data = listOf(
        CategoryItem(
            null,
            null,
            true,
            "Electronics",
            R.drawable.img_electronics,
            R.color.electronics_category_color
        ),
        CategoryItem(
            null,
            null,
            true,
            "Furniture",
            R.drawable.img_furniture,
            R.color.category_furniture_color
        ),
        CategoryItem(
            null,
            null,
            true,
            "Fashion",
            R.drawable.img_fashion,
            R.color.category_fashion_color
        ),
        CategoryItem(
            null,
            null,
            true,
            "Pets",
            R.drawable.img_pets,
            R.color.category_pets_color
        ),
        CategoryItem(
            null,
            null,
            true,
            "Books",
            R.drawable.img_books,
            R.color.category_books_color
        ),
        CategoryItem(
            null,
            null,
            true,
            "Other",
            R.drawable.img_other,
            R.color.category_other_color
        )
    )

    init {

        _categoryList.postValue(data)

    }

}