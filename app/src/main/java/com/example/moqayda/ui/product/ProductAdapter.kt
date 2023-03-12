package com.example.moqayda.ui.product

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.R
import com.example.moqayda.databinding.ProductItemBinding
import com.example.moqayda.models.CategoryProductViewModel

class ProductAdapter(private var productList: List<CategoryProductViewModel?>?): RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    lateinit var onItemClickListener:OnItemClickListener
    class ProductViewHolder(private val viewBinding:ProductItemBinding): RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(product: CategoryProductViewModel){
            viewBinding.product=product
            viewBinding.invalidateAll()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val viewBinding : ProductItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.product_item,parent,false)
        return ProductViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList?.get(position)
        Log.e("product","$product")
            holder.bind(product!!)
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(product)
        }

    }

    override fun getItemCount(): Int {
        return productList?.size ?: 0
    }

    interface OnItemClickListener{
        fun onItemClick(productItem: CategoryProductViewModel?)
    }


}