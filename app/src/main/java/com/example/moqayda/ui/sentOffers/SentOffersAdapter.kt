package com.example.moqayda.ui.sentOffers

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
import com.example.moqayda.models.Product
import com.example.moqayda.models.SwapPublicItem
import com.example.moqayda.ui.completedBarters.CompletedBartersViewModel
import com.example.moqayda.ui.completedBarters.CompletedBartersViewModelFactory
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class SentOffersAdapter(
    private val offersList: List<SwapPublicItem>,
    private val mContext: Context,
    private val owner: ViewModelStoreOwner,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<SentOffersAdapter.PendingSentOffersViewHolder>() {
    val currentUser = Firebase.auth.currentUser
    inner class PendingSentOffersViewHolder(val binding: BarteredItemBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bind(firstProduct: Product, secondProduct:Product ){
            binding.firstItem = firstProduct
            binding.secondItem = secondProduct
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingSentOffersViewHolder {
        val binding: BarteredItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.bartered_item,
            parent,
            false
        )
        return PendingSentOffersViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return offersList.size
    }

    override fun onBindViewHolder(holder: PendingSentOffersViewHolder, position: Int) {
        val offer = offersList[position]
        val vmFactory = SentOffersVmFactory(mContext)
        val viewModel = ViewModelProvider(owner,vmFactory)[SentOffersViewModel::class.java]

        lifecycleOwner.lifecycleScope.launch{
            val productOne = viewModel.getProduct(offer.productId)
            val productTwo = viewModel.getProductUsingProductOwnerId(offer.productOwnerId)
            holder.binding.firstItem = productTwo
            holder.binding.secondItem = productOne
        }

        holder.binding.firstProductImage.setOnClickListener {
            startFullImageScreen(holder,holder.binding.firstItem)
        }
        holder.binding.productImage.setOnClickListener {
            startFullImageScreen(holder,holder.binding.secondItem)
        }

    }


    private fun startFullImageScreen(holder: PendingSentOffersViewHolder, product: Product?) {
        val intent = Intent(holder.itemView.context, ImageViewerActivity::class.java)
        intent.putExtra("image_url", product?.pathImage)
        holder.itemView.context.startActivity(intent)
    }

}