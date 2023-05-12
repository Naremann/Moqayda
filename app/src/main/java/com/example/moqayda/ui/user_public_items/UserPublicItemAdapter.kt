package com.example.moqayda.ui.user_public_items

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.ImageViewerActivity
import com.example.moqayda.R
import com.example.moqayda.databinding.UserPublicProductItemBinding
import com.example.moqayda.models.PrivateProduct
import com.example.moqayda.ui.private_product.PrivateProductsFragmentArgs
import com.github.chrisbanes.photoview.PhotoViewAttacher

class UserPublicItemAdapter (var productList: List<PrivateProduct?>? = mutableListOf(),
var userPublicItemsFragment:UserPublicItemsFragment):
RecyclerView.Adapter<UserPublicItemAdapter.UserPublicItemsViewHolder>(){


    lateinit var onSwapLinearClickListener: OnSwapLinearClickListener

    class UserPublicItemsViewHolder(private val viewBinding: UserPublicProductItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        var isVisibleSwapLinear = viewBinding.linearSwap
        val productImage = viewBinding.productImage
        fun bind(product: PrivateProduct) {

            viewBinding.userItem = product

            viewBinding.invalidateAll()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPublicItemsViewHolder {
        val viewBinding: UserPublicProductItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.user_public__product_item,
            parent,
            false
        )
        return UserPublicItemsViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: UserPublicItemsViewHolder, position: Int) {
        val product = productList?.get(position)
        Log.e("product", "$product")
        holder.bind(product!!)


        holder.productImage.setOnClickListener {
            startFullImageScreen(holder, product)
        }
        if (holder.productImage.isClickable) {
            makeImageZoomable(holder)
        }
        holder.itemView.setOnClickListener {
            onSwapLinearClickListener.onSwapLinearClick(product)
        }

        holder.isVisibleSwapLinear.isVisible =
            UserPublicItemsFragmentArgs.fromBundle(userPublicItemsFragment.requireArguments())
                .isVisibleSwapLinear
        Log.e("adapter", "Value ${holder.isVisibleSwapLinear.isVisible}")

       /* holder.isVisibleSwapLinear.setOnClickListener {
            onSwapLinearClickListener.onSwapLinearClick(product)
        }*/
    }


    private fun makeImageZoomable(holder: UserPublicItemsViewHolder) {
        val photoViewAttacher = PhotoViewAttacher(holder.productImage)
        photoViewAttacher.update()
    }

    private fun startFullImageScreen(holder: UserPublicItemsViewHolder, product: PrivateProduct?) {
        val intent = Intent(holder.itemView.context, ImageViewerActivity::class.java)
        intent.putExtra("image_url", product?.pathImage)
        holder.itemView.context.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return productList?.size ?: 0
    }

    fun changeData(products: List<PrivateProduct?>?) {
        productList = products

    }

    interface OnSwapLinearClickListener {
        fun onSwapLinearClick(ProductItem: PrivateProduct?)
    }

}