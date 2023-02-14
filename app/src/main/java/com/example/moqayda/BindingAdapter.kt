package com.example.moqayda

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.models.CategoryItem
import com.example.moqayda.ui.home.CategoryItemAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("layout_error")
fun setError(inputLayout: TextInputLayout, error:String?){
    inputLayout.error = error
}

