package com.example.moqayda

import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.ui.home.CategoryItemAdapter
import com.example.moqayda.ui.selectCategory.SelectCategoryAdapter
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso

@BindingAdapter("layout_error")
fun setError(inputLayout: TextInputLayout, error:String?){
    inputLayout.error = error
}
@BindingAdapter("photoUrl")
fun bindImage(imageView: ImageView, url: String?) {
    url?.let {
        val photoUri = url.toUri().buildUpon().scheme("http").build()
        Picasso.with(imageView.context).load(photoUri).into(imageView)
    }
}

@BindingAdapter("imgUri")
fun bindImageUri(imageView: ImageView, uri: Uri?) {
    uri?.let {
        imageView.setImageURI(it)
    }
}


