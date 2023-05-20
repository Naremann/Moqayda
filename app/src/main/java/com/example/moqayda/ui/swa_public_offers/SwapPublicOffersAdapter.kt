package com.example.moqayda.ui.swa_public_offers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.R
import com.example.moqayda.databinding.SwapPrivateOffersItemBinding
import com.example.moqayda.databinding.SwapPublicOffersItemBinding
import com.example.moqayda.models.PrivateProductOwnerByIdResponse
import com.example.moqayda.models.Product
import com.example.moqayda.ui.swap_private_offers.SwapPrivateOffersAdapter
import com.example.moqayda.ui.swap_private_offers.SwapPrivateOffersFragment

class SwapPublicOffersAdapter(var swapPublicOffers: List<Product?>?= mutableListOf(),var swapPublicOffersFragment: SwapOffersOfPublicItemsFragment): RecyclerView.Adapter<SwapPublicOffersAdapter.SwapPublicOffersViewHolder>() {

    lateinit var onDetailsClickListener: OnDetailsClickListener

    open class SwapPublicOffersViewHolder(var viewBinding: SwapPublicOffersItemBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(swapPublicOffers: Product, swapPublicOffersFragment: SwapOffersOfPublicItemsFragment) {
            viewBinding.swapPublicOffers=swapPublicOffers
            viewBinding.invalidateAll()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwapPublicOffersViewHolder {
        val viewBinding: SwapPublicOffersItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.swap_public_offers_item,parent,false)
        return SwapPublicOffersViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: SwapPublicOffersViewHolder, position: Int) {
        val swapPrivateOffers = swapPublicOffers?.get(position)
        holder.bind(swapPrivateOffers!!, swapPublicOffersFragment )
        holder.viewBinding.productDetailsTv.setOnClickListener {
            onDetailsClickListener.onItemClick(swapPrivateOffers.id!!,swapPublicOffersFragment.viewModel.productId)
        }

    }

    override fun getItemCount(): Int {
        return swapPublicOffers?.size ?:0
    }
    fun changeData(swapPrivateOffersList: List<Product?>?) {
        swapPublicOffers = swapPrivateOffersList

    }

    interface OnDetailsClickListener{
        fun onItemClick(senderProductId:Int,receiverProductId:Int)
    }
}