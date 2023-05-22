package com.example.moqayda.ui.private_product

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.ImageViewerActivity
import com.example.moqayda.R
import com.example.moqayda.databinding.PrivateProductItemBinding
import com.example.moqayda.models.PrivateProduct
import com.github.chrisbanes.photoview.PhotoViewAttacher

class PrivateProductAdapter(var productList: List<PrivateProduct?>? = mutableListOf(),var privateProductsFragment: PrivateProductsFragment):
    RecyclerView.Adapter<PrivateProductAdapter.PrivateProductViewHolder>(){


    lateinit var onSwapLinearClickListener: OnSwapLinearClickListener

    inner class PrivateProductViewHolder(private val viewBinding: PrivateProductItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        val dotMenu = viewBinding.dotMenu
        val popupMenu = PopupMenu(privateProductsFragment.requireContext(), dotMenu)
        var isVisibleSwapLinear=viewBinding.linearSwap
        val productImage=viewBinding.productImage
        fun bind(product: PrivateProduct) {

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
        val builder = AlertDialog.Builder(privateProductsFragment.requireContext())
        builder.setTitle(privateProductsFragment.requireContext().getString(R.string.confirmation))
        builder.setMessage(privateProductsFragment.requireContext().getString(R.string.remove_product_confirmation))

        holder.popupMenu.menuInflater.inflate(R.menu.user_private_product_menu,holder.popupMenu.menu)
        holder.popupMenu.setOnMenuItemClickListener { menuItem->
            when (menuItem.itemId){

                R.id.delete_post ->{
                    builder.setPositiveButton("OK") { dialog, which ->
                        privateProductsFragment.viewModel.deletePrivateItemOwnerById(product.id!!)

                    }
                    builder.setNegativeButton("Cancel") { _, _ ->
                        // Cancel button clicked
                    }
                    val dialog = builder.create()
                    dialog.show()

                    true
                }

                else -> false
            }
        }
        holder.dotMenu.setOnClickListener {
            holder.popupMenu.show()
        }



        holder.productImage.setOnClickListener {
            startFullImageScreen(holder,product)
        }
        if(holder.productImage.isClickable){
            makeImageZoomable(holder)
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

    private fun startFullImageScreen(holder: PrivateProductViewHolder, product: PrivateProduct?) {
        val intent = Intent(holder.itemView.context, ImageViewerActivity::class.java)
        intent.putExtra("image_url",product?.name)
        holder.itemView.context.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return productList?.size ?: 0
    }

    fun changeData(products: List<PrivateProduct?>?){
        productList=products

    }

    interface OnSwapLinearClickListener {
        fun onSwapLinearClick(privateProductItem: PrivateProduct?)
    }




}