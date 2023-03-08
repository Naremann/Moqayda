package com.example.moqayda.ui.addProduct

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.moqayda.databinding.CategoryListItemBinding
import com.example.moqayda.models.CategoryItem

class SpinnerAdapter(context: Context, categoryList: List<CategoryItem>) : ArrayAdapter<CategoryItem>(
    context, 0, categoryList


) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {

        val currency = getItem(position)

        val binding = CategoryListItemBinding.inflate(
            (LayoutInflater.from(parent.context)),
            parent, false
        )
        binding.category = currency
        return binding.root
    }

}