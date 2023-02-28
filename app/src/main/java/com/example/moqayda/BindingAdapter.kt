package com.example.moqayda

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("layout_error")
fun setError(inputLayout: TextInputLayout, error:String?){
    inputLayout.error = error
}