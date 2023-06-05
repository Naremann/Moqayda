package com.example.moqayda.ui.completedBarters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.ImageViewerActivity
import com.example.moqayda.R
import com.example.moqayda.databinding.BarteredItemBinding
import com.example.moqayda.models.BarteredProduct
import com.example.moqayda.models.Product
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class CompletedBartersAdapter(
    private val bartersList: List<BarteredProduct>,
    private val mContext: Context,
    private val owner: ViewModelStoreOwner,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<CompletedBartersAdapter.CompletedBartersViewHolder>() {
    val currentUser = Firebase.auth.currentUser
    inner class CompletedBartersViewHolder(val binding: BarteredItemBinding) :
        RecyclerView.ViewHolder(binding.root){

            fun bind(firstProduct: Product, secondProduct:Product ){
                binding.firstItem = firstProduct
                binding.secondItem = secondProduct
            }

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedBartersViewHolder {
        val binding: BarteredItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.bartered_item,
            parent,
            false
        )
        return CompletedBartersViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return bartersList.size
    }

    override fun onBindViewHolder(holder: CompletedBartersViewHolder, position: Int) {
        val barter = bartersList[position]
        val vmFactory = CompletedBartersViewModelFactory(mContext)
        val viewModel = ViewModelProvider(owner,vmFactory)[CompletedBartersViewModel::class.java]

        lifecycleOwner.lifecycleScope.launch{
            val productOne = viewModel.getProduct(barter.productId)
            val productTwo = viewModel.getProductUsingProductOwnerId(barter.productOwnerId)
            if (currentUser?.uid == productOne?.userId){
                holder.binding.firstItem = productOne
                holder.binding.secondItem = productTwo
            }else{
                holder.binding.firstItem = productTwo
                holder.binding.secondItem = productOne
            }
        }

        holder.binding.firstProductImage.setOnClickListener {
            startFullImageScreen(holder,holder.binding.firstItem)
        }
        holder.binding.productImage.setOnClickListener {
            startFullImageScreen(holder,holder.binding.secondItem)
        }

    }


    private fun startFullImageScreen(holder: CompletedBartersViewHolder , product: Product?) {
        val intent = Intent(holder.itemView.context, ImageViewerActivity::class.java)
        intent.putExtra("image_url", product?.pathImage)
        holder.itemView.context.startActivity(intent)
    }

}