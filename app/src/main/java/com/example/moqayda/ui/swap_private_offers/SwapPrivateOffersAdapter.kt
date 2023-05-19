package com.example.moqayda.ui.swap_private_offers

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.R
import com.example.moqayda.databinding.SwapPrivateOffersItemBinding
import com.example.moqayda.models.PrivateProductOwnerByIdResponse

class SwapPrivateOffersAdapter(var swapPrivateOffers: List<PrivateProductOwnerByIdResponse?>?= mutableListOf(),var swapPrivateOffersFragment: SwapPrivateOffersFragment) : RecyclerView.Adapter<SwapPrivateOffersAdapter.SwapPrivateOffersViewHolder>() {

    lateinit var onItemClickListener:SwapPrivateOffersAdapter.OnItemClickListener

      class SwapPrivateOffersViewHolder(var viewBinding: SwapPrivateOffersItemBinding) : RecyclerView.ViewHolder(viewBinding.root) {
         fun bind(swapPrivateOffers: PrivateProductOwnerByIdResponse,swapPrivateOffersFragment: SwapPrivateOffersFragment) {
             viewBinding.vm= ViewModelProvider(swapPrivateOffersFragment)[SwapPrivateOffersViewModel::class.java]
           viewBinding.swapPrivateOffers=swapPrivateOffers
            viewBinding.invalidateAll()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwapPrivateOffersViewHolder {
        val viewBinding:SwapPrivateOffersItemBinding=DataBindingUtil.inflate(LayoutInflater.from(parent.context),
        R.layout.swap_private_offers_item,parent,false)
        return SwapPrivateOffersViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: SwapPrivateOffersViewHolder, position: Int) {
        val swapPrivateOffers = swapPrivateOffers?.get(position)
        holder.bind(swapPrivateOffers!!,swapPrivateOffersFragment)
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(swapPrivateOffers)
        }
    }

    override fun getItemCount(): Int {
        return swapPrivateOffers?.size ?:0
    }
    fun changeData(swapPrivateOffersList: List<PrivateProductOwnerByIdResponse?>?) {
        swapPrivateOffers = swapPrivateOffersList

    }

    interface OnItemClickListener{
        fun onItemClick(swapPrivateOffers: PrivateProductOwnerByIdResponse)
    }


}