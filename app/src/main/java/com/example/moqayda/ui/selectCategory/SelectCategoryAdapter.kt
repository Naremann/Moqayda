package com.example.moqayda.ui.selectCategory

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.R
import com.example.moqayda.bindImage
import com.example.moqayda.databinding.CategoryListItemBinding
import com.example.moqayda.models.CategoryItem

class SelectCategoryAdapter(
    var categoryList: List<CategoryItem?>,
    private val clickListener: CategoryListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    class CategoryListViewHolder(val binding: CategoryListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(categoryItem: CategoryItem, clickListener: CategoryListener) {
            binding.clickListener = clickListener
            binding.category = categoryItem
            binding.invalidateAll()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CategoryListViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.category_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val categoryItem = categoryList[position]
        val hexColor ="#" + categoryItem?.categoryBackgroundColor?.let { Integer.toHexString(it) }
        (holder as CategoryListViewHolder).bind(categoryItem!!,clickListener)
        holder.binding.categoryItemImg.setBackgroundColor(
            Color.parseColor(hexColor)
        )
        bindImage(holder.binding.categoryItemImg,categoryItem.pathImage)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }


}

class CategoryListener(val clickListener: (categoryItem: CategoryItem) -> Unit) {
    fun onClick(categoryItem: CategoryItem) = clickListener(categoryItem)


}