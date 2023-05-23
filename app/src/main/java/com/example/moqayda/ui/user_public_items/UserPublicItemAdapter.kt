package com.example.moqayda.ui.user_public_items

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.ImageViewerActivity
import com.example.moqayda.R
import com.example.moqayda.databinding.UserPublicProductItemBinding
import com.example.moqayda.models.Product
import com.example.moqayda.ui.otherUserProfile.OtherUserProfileVMFactory
import com.example.moqayda.ui.otherUserProfile.OtherUserProfileViewModel
import com.github.chrisbanes.photoview.PhotoViewAttacher

class UserPublicItemAdapter(
    var productList: List<Product?>? = mutableListOf(),
    var userPublicItemsFragment: UserPublicItemsFragment?,
    val mContext: Context,
    val owner: ViewModelStoreOwner
):
RecyclerView.Adapter<UserPublicItemAdapter.UserPublicItemsViewHolder>(){


    lateinit var onSwapLinearClickListener: OnSwapLinearClickListener

   inner class UserPublicItemsViewHolder(private val viewBinding: UserPublicProductItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        val dotMenu = viewBinding.dotMenu
       val popupMenu = PopupMenu(mContext, dotMenu)
        var isVisibleSwapLinear = viewBinding.linearSwap
        val productImage = viewBinding.productImage
        fun bind(product: Product) {

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

        val builder = AlertDialog.Builder(mContext)
        builder.setTitle(mContext.getString(R.string.confirmation))
        builder.setMessage(mContext.getString(R.string.remove_product_confirmation))
        holder.productImage.setOnClickListener {
            startFullImageScreen(holder, product)
        }
        if (holder.productImage.isClickable) {
            makeImageZoomable(holder)
        }


        if (userPublicItemsFragment!= null){
            // if userPublicItemsFragment!= null -> the current user is browsing his own products
            // because i used this adapter in otherUserProfileFragment and of course i didn't pass
            // the userPublicItemsFragment to the adapter
            Log.e("UserPublicItemAdapter","userPublicItemsFragment not null")
            val viewModelFactory = UserPublicItemViewModelFactory(mContext)
            val userPublicItemViewModel = ViewModelProvider(owner,viewModelFactory)[UserPublicItemViewModel::class.java]
            holder.isVisibleSwapLinear.isVisible =
                UserPublicItemsFragmentArgs.fromBundle(userPublicItemsFragment!!.requireArguments())
                    .isVisibleSwapLinear
            Log.e("adapter", "Value ${holder.isVisibleSwapLinear.isVisible}")

            holder.isVisibleSwapLinear.setOnClickListener {
                onSwapLinearClickListener.onSwapLinearClick(product)
            }
            holder.popupMenu.menuInflater.inflate(R.menu.current_user_public_products_menu,holder.popupMenu.menu)
            holder.popupMenu.setOnMenuItemClickListener { menuItem->
                when (menuItem.itemId){
                    R.id.menu_item_show_original ->{
                        userPublicItemViewModel.navigateToProductDetails(product)
                        true
                    }
                    R.id.delete_post ->{
                        builder.setPositiveButton("OK") { dialog, which ->
                            userPublicItemViewModel.deleteSelectedProduct(product)
                        }
                        builder.setNegativeButton("Cancel") { _, _ ->
                            // Cancel button clicked
                        }
                        val dialog = builder.create()
                        dialog.show()

                        true
                    }
                    R.id.edit_post -> {
                        userPublicItemViewModel.navigateToUpdateProduct(product,product.categoryId!!)
                        true
                    }
                    else -> false
                }
            }
            holder.dotMenu.setOnClickListener {
                holder.popupMenu.show()
            }


        }else{
            val vmFactory = OtherUserProfileVMFactory(mContext.applicationContext)
            val otherUserPublicItemViewModel = ViewModelProvider(owner,vmFactory)[OtherUserProfileViewModel::class.java]
            holder.popupMenu.menuInflater.inflate(R.menu.other_user_public_product_menu,holder.popupMenu.menu)
            holder.popupMenu.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId){
                    R.id.show_product_details ->{
                        otherUserPublicItemViewModel.navigateToProductDetails(product)
                        true
                    }
                    R.id.add_to_favorite ->{
                        otherUserPublicItemViewModel.addProductToFavorite(product.id!!)
                        true
                    }
                    else -> false
                }
            }
            holder.dotMenu.setOnClickListener {
                holder.popupMenu.show()
            }

        }

    }


    private fun makeImageZoomable(holder: UserPublicItemsViewHolder) {
        val photoViewAttacher = PhotoViewAttacher(holder.productImage)
        photoViewAttacher.update()
    }

    private fun startFullImageScreen(holder: UserPublicItemsViewHolder, product: Product?) {
        val intent = Intent(holder.itemView.context, ImageViewerActivity::class.java)
        intent.putExtra("image_url", product?.pathImage)
        holder.itemView.context.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return productList?.size ?: 0
    }

    fun changeData(products: List<Product?>?) {
        productList = products

    }

    interface OnSwapLinearClickListener {
        fun onSwapLinearClick(ProductItem: Product?)
    }

}