package com.example.moqayda.ui.favorite
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.ImageViewerActivity
import com.example.moqayda.R
import com.example.moqayda.bindImage
import com.example.moqayda.databinding.FavoriteProductItemBinding
import com.example.moqayda.models.CategoryProductViewModel
class FavoriteAdapter(
    private var productList: List<CategoryProductViewModel?>? = mutableListOf(),
    private val mContext: Context,
    private val owner: ViewModelStoreOwner,
    private val fragment: FavoriteFragment
) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    inner class FavoriteViewHolder(val binding: FavoriteProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val dotMenu = binding.dotMenu
        val popupMenu = PopupMenu(mContext,dotMenu)
        fun bind(product: CategoryProductViewModel) {
            binding.product = product
            bindImage(binding.productImage, product.pathImage)
            binding.invalidateAll()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {

        val viewBinding:FavoriteProductItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.favorite_product_item,
            parent,
            false
        )
        return FavoriteViewHolder(
            viewBinding
        )
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val viewModel = ViewModelProvider(owner)[FavoriteViewModel::class.java]
        val builder = AlertDialog.Builder(mContext)
        builder.setTitle("Confirmation")
        builder.setMessage("Are you sure you want to do remove it from favorites ?")
        val product = productList?.get(position)
        holder.bind(product!!)
        holder.popupMenu.menuInflater.inflate(R.menu.favorite_product_dot_menu,holder.popupMenu.menu)
        holder.popupMenu.setOnMenuItemClickListener {menuItem ->
            when(menuItem.itemId){
                R.id.menu_item_show_original ->{
                    true
                }
                R.id.menu_item_remove_from_favorite ->{
                    builder.setPositiveButton("OK") { dialog, which ->

                        viewModel.wishlist.value?.forEach{
                            if (product.id == it.productId){
                                viewModel.removeFavoriteProduct(it.id)

                            }
                        }
                    }

                    builder.setNegativeButton("Cancel") { dialog, which ->
                        // Cancel button clicked
                        // Do something here

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

        holder.binding.productImage.setOnClickListener {
            val intent = Intent(holder.itemView.context, ImageViewerActivity::class.java)
            intent.putExtra("image_url", product.pathImage)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return productList?.size ?: 0
    }
}