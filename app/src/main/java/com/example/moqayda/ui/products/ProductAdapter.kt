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
import com.example.moqayda.models.Product
import com.github.chrisbanes.photoview.PhotoViewAttacher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ProductAdapter(var productList: List<Product?>? = mutableListOf(),private val productViewModel: ProductViewModel) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    lateinit var onItemClickListener: OnItemClickListener

    class ProductViewHolder(val viewBinding: ProductItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        val activeLoveImage = viewBinding.activeLoveImg
        val inActiveLoveImage = viewBinding.inActiveLoveImg
        val addToFavoriteTv = viewBinding.addToFavoriteTv
        val productImage=viewBinding.productImage
        fun bind(product: Product) {
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

    @OptIn(DelicateCoroutinesApi::class)
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList?.get(position)
        holder.viewBinding.viewModel = productViewModel
        Log.e("product", "$product")
        holder.bind(product!!)
        GlobalScope.launch {
            val appUser = productViewModel.getProductOwner(product.userId!!)
            holder.viewBinding.user = appUser

        }


        holder.productImage.setOnClickListener {
            startFullImageScreen(holder,product)
        }
        if(holder.productImage.isClickable){
            makeImageZoomable(holder)
        }
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(product)
        }


        holder.viewBinding.addToFavoriteTv.setOnClickListener {
            Log.e("ProductAdapter","addToFavoriteTv pressed")
            product.id?.let { it1 -> productViewModel.addProductToFavorite(it1) }
        }

    }

    private fun makeImageZoomable(holder: ProductViewHolder) {
        val photoViewAttacher = PhotoViewAttacher(holder.productImage)
        photoViewAttacher.update()
    }

    private fun startFullImageScreen(holder: ProductViewHolder,product:Product) {
        val intent = Intent(holder.itemView.context,ImageViewerActivity::class.java)
        intent.putExtra("image_url",product.pathImage)
        holder.itemView.context.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return productList?.size ?: 0
    }

    fun changeData(products: List<Product?>?){
        productList=products
    }

    interface OnItemClickListener {
        fun onItemClick(productItem: Product?)
    }

    interface OnInActiveLoveImageClickListener {
        fun onIconClick(
            activeLoveImage: ImageView,
            inActiveLoveImage: ImageView,
            addToFavoriteTv: TextView,
            product: Product
        )
    }




}

