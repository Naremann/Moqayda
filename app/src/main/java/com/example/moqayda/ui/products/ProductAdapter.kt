package com.example.moqayda.ui.products

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.R
import com.example.moqayda.bindImage
import com.example.moqayda.databinding.ProductItemBinding
import com.example.moqayda.models.CategoryProductViewModel

class ProductAdapter(private var productList: List<CategoryProductViewModel?>?) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    lateinit var onInActiveLoveImage: OnInActiveLoveImageClickListener
    lateinit var onActiveLoveImage: OnActiveLoveImageClickListener
    lateinit var onItemClickListener: OnItemClickListener

    class ProductViewHolder(private val viewBinding: ProductItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        val activeLoveImage = viewBinding.activeLoveImg
        val inActiveLoveImage = viewBinding.inActiveLoveImg
        val addToFavoriteTv = viewBinding.addToFavoriteTv
        fun bind(product: CategoryProductViewModel) {
            viewBinding.product = product
            bindImage(viewBinding.productImage, "http://www.moqayda.somee.com/" + product.pathImage)
            viewBinding.invalidateAll()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val viewBinding: ProductItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.product_item,
            parent,
            false
        )
        return ProductViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList?.get(position)
        Log.e("product", "$product")
        holder.bind(product!!)
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(product)
        }
        holder.activeLoveImage.setOnClickListener {
            onActiveLoveImage.onIconClick(
                holder.activeLoveImage, holder.inActiveLoveImage, holder.addToFavoriteTv
            )
        }
        holder.inActiveLoveImage.setOnClickListener {
                    onInActiveLoveImage.onIconClick(
                        holder.activeLoveImage,
                        holder.inActiveLoveImage,
                        holder.addToFavoriteTv
                    )

        }

    }

    override fun getItemCount(): Int {
        return productList?.size ?: 0
    }

    interface OnItemClickListener {
        fun onItemClick(productItem: CategoryProductViewModel?)
    }

    interface OnInActiveLoveImageClickListener {
        fun onIconClick(
            activeLoveImage: ImageView,
            inActiveLoveImage: ImageView,
            addToFavoriteTv: TextView,
        )
    }

    interface OnActiveLoveImageClickListener {
        fun onIconClick(
            activeLoveImage: ImageView,
            inActiveLoveImage: ImageView,
            addToFavoriteTv: TextView
        )
    }


}

