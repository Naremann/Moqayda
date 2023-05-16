package com.example.moqayda.ui.private_product

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.ImageViewerActivity
import com.example.moqayda.R
import com.example.moqayda.databinding.PrivateProductItemBinding
import com.example.moqayda.models.PrivateItem
import com.example.moqayda.models.UserPrivateItemViewModelsItem
import com.github.chrisbanes.photoview.PhotoViewAttacher

class PrivateProductAdapter(var productList: List<UserPrivateItemViewModelsItem?>? = mutableListOf(),var privateProductsFragment: PrivateProductsFragment):
    RecyclerView.Adapter<PrivateProductAdapter.PrivateProductViewHolder>(){


    lateinit var onSwapLinearClickListener: OnSwapLinearClickListener

    class PrivateProductViewHolder(private val viewBinding: PrivateProductItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        var isVisibleSwapLinear=viewBinding.linearSwap
        val productImage=viewBinding.productImage
        fun bind(product: UserPrivateItemViewModelsItem) {

            viewBinding.product = product

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
            onSwapLinearClickListener.onSwapLinearClick(product)
        }

       holder.isVisibleSwapLinear.isVisible = PrivateProductsFragmentArgs.fromBundle(privateProductsFragment.requireArguments())
           .isVisible
       Log.e("adapter", "Value ${holder.isVisibleSwapLinear.isVisible}")

        holder.isVisibleSwapLinear.setOnClickListener {
            onSwapLinearClickListener.onSwapLinearClick(product)
        }
    }






    private fun makeImageZoomable(holder: PrivateProductViewHolder) {
        val photoViewAttacher = PhotoViewAttacher(holder.productImage)
        photoViewAttacher.update()
    }

    private fun startFullImageScreen(holder: PrivateProductViewHolder, product: UserPrivateItemViewModelsItem?) {
        val intent = Intent(holder.itemView.context, ImageViewerActivity::class.java)
        intent.putExtra("image_url",product?.privateItemName)
        holder.itemView.context.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return productList?.size ?: 0
    }

    fun changeData(products: List<UserPrivateItemViewModelsItem?>?){
        productList=products

    }

    interface OnSwapLinearClickListener {
        fun onSwapLinearClick(privateProductItem: UserPrivateItemViewModelsItem?)
    }




}