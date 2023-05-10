package com.example.moqayda.ui.private_product

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.ImageViewerActivity
import com.example.moqayda.R
import com.example.moqayda.bindImage
import com.example.moqayda.databinding.PrivateProductItemBinding
import com.example.moqayda.models.PrivateItem
import com.example.moqayda.models.PrivateProduct
import com.example.moqayda.models.Product
import com.github.chrisbanes.photoview.PhotoViewAttacher
import kotlinx.coroutines.DelicateCoroutinesApi

class PrivateProductAdapter(var productList: List<PrivateItem?>? = mutableListOf()):
    RecyclerView.Adapter<PrivateProductAdapter.PrivateProductViewHolder>(){


    lateinit var onItemClickListener: OnItemClickListener

    class PrivateProductViewHolder(private val viewBinding: PrivateProductItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        val productImage=viewBinding.productImage
        fun bind(product: PrivateItem) {
            viewBinding.product = product
           // product.privateItempathImage?.let { Log.e("ProductAdapter", it) }
           // bindImage(viewBinding.productImage, product.privateItempathImage)
            viewBinding.invalidateAll()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrivateProductViewHolder {
        val viewBinding: PrivateProductItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.private_product_item,
            parent,
            false
        )
        return PrivateProductViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: PrivateProductViewHolder, position: Int) {
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


    }

    private fun makeImageZoomable(holder: PrivateProductViewHolder) {
        val photoViewAttacher = PhotoViewAttacher(holder.productImage)
        photoViewAttacher.update()
    }

    private fun startFullImageScreen(holder: PrivateProductViewHolder, product: PrivateItem?) {
        val intent = Intent(holder.itemView.context, ImageViewerActivity::class.java)
        intent.putExtra("image_url",product?.privateItempathImage)
        holder.itemView.context.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return productList?.size ?: 0
    }

    fun changeData(products: List<PrivateItem?>?){
        productList=products

    }

    interface OnItemClickListener {
        fun onItemClick(productItem: PrivateItem?)
    }




}