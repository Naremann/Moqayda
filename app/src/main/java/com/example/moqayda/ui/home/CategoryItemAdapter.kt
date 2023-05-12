package com.example.moqayda.ui.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.R
import com.example.moqayda.bindImage
import com.example.moqayda.databinding.CategoryLeftItemBinding
import com.example.moqayda.databinding.CategoryRightItemBinding
import com.example.moqayda.models.CategoryItem

class CategoryItemAdapter(var categoryList: List<CategoryItem?>, private val clickListener: CategoryListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val leftItemCode = 0
    private val rightItemCode = 1


    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) leftItemCode else rightItemCode
    }


    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewBindingLeft: CategoryLeftItemBinding
        val viewBindingRight: CategoryRightItemBinding
        if (viewType == leftItemCode) {
            viewBindingLeft = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.category_left_item,
                parent,
                false
            )
            return CategoryLeftItemViewHolder(viewBindingLeft)
        } else {
            viewBindingRight = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.category_right_item,
                parent,
                false
            )
            return CategoryRightItemViewHolder(viewBindingRight)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val categoryItem = categoryList[position]
        val hexColor ="#" + categoryItem?.categoryBackgroundColor?.let { Integer.toHexString(it) }
        holder.itemView.setOnClickListener {

        }

        if (getItemViewType(position) == rightItemCode) {
            (holder as CategoryRightItemViewHolder).bind(categoryItem!!,clickListener)
            holder.rightItemBinding.materialCardView.setCardBackgroundColor(
            Color.parseColor(hexColor)
            )
            bindImage(holder.rightItemBinding.categoryImg,categoryItem.pathImage)

        } else {
            (holder as CategoryLeftItemViewHolder).bind(categoryItem!!,clickListener)
            holder.leftItemBinding.materialCardView.setCardBackgroundColor(
                Color.parseColor(hexColor)
            )
            bindImage(holder.leftItemBinding.categoryImg,categoryItem.pathImage)
        }


    }




    class CategoryLeftItemViewHolder(val leftItemBinding: CategoryLeftItemBinding) :
        RecyclerView.ViewHolder(leftItemBinding.root) {
        fun bind(categoryItem: CategoryItem, clickListener: CategoryListener) {
            leftItemBinding.clickListener = clickListener
            leftItemBinding.item = categoryItem
            leftItemBinding.invalidateAll()
        }
    }

    class CategoryRightItemViewHolder(val rightItemBinding: CategoryRightItemBinding) :
        RecyclerView.ViewHolder(rightItemBinding.root) {
        fun bind(categoryItem: CategoryItem, clickListener: CategoryListener) {
            rightItemBinding.clickListener = clickListener
            rightItemBinding.item = categoryItem
            rightItemBinding.invalidateAll()

        }
    }
}

class CategoryListener(val clickListener: (categoryItem: CategoryItem) -> Unit) {
    fun onClick(categoryItem: CategoryItem) = clickListener(categoryItem)
}