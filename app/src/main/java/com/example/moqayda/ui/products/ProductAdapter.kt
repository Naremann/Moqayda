package com.example.moqayda.ui.products

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.ImageViewerActivity
import com.example.moqayda.R
import com.example.moqayda.bindImage
import com.example.moqayda.databinding.ProductItemBinding
import com.example.moqayda.models.CategoryProductViewModel
import com.github.chrisbanes.photoview.PhotoViewAttacher




class ProductAdapter(var productList: List<CategoryProductViewModel?>? = mutableListOf()) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    lateinit var onInActiveLoveImage: OnInActiveLoveImageClickListener
    lateinit var onActiveLoveImage: OnActiveLoveImageClickListener
    lateinit var onItemClickListener: OnItemClickListener

    class ProductViewHolder(private val viewBinding: ProductItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        val activeLoveImage = viewBinding.activeLoveImg
        val inActiveLoveImage = viewBinding.inActiveLoveImg
        val addToFavoriteTv = viewBinding.addToFavoriteTv
        val productImage=viewBinding.productImage
        fun bind(product: CategoryProductViewModel) {
            viewBinding.product = product
            product.pathImage?.let { Log.e("ProductAdapter", it) }
            bindImage(viewBinding.productImage, product.pathImage)
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
        holder.productImage.setOnClickListener {
            startFullImageScreen(holder,product)
        }
        if(holder.productImage.isClickable){
            makeImageZoomable(holder)
        }
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(product)
        }
        holder.activeLoveImage.setOnClickListener {
            onActiveLoveImage.onIconClick(
                holder.activeLoveImage, holder.inActiveLoveImage, holder.addToFavoriteTv,product
            )
        }
        holder.inActiveLoveImage.setOnClickListener {
                    onInActiveLoveImage.onIconClick(
                        holder.activeLoveImage,
                        holder.inActiveLoveImage,
                        holder.addToFavoriteTv,
                        product
                    )

        }

    }

    private fun makeImageZoomable(holder: ProductViewHolder) {
        val photoViewAttacher = PhotoViewAttacher(holder.productImage)
        photoViewAttacher.update()
    }

    private fun startFullImageScreen(holder: ProductViewHolder,product:CategoryProductViewModel) {
        val intent = Intent(holder.itemView.context,ImageViewerActivity::class.java)
        intent.putExtra("image_url",product.pathImage)
        holder.itemView.context.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return productList?.size ?: 0
    }

    fun changeData(products: List<CategoryProductViewModel?>?){
        productList=products
    }

    interface OnItemClickListener {
        fun onItemClick(productItem: CategoryProductViewModel?)
    }

    interface OnInActiveLoveImageClickListener {
        fun onIconClick(
            activeLoveImage: ImageView,
            inActiveLoveImage: ImageView,
            addToFavoriteTv: TextView,
            product: CategoryProductViewModel
        )
    }

    interface OnActiveLoveImageClickListener {
        fun onIconClick(
            activeLoveImage: ImageView,
            inActiveLoveImage: ImageView,
            addToFavoriteTv: TextView,
            product: CategoryProductViewModel
        )
    }


}

