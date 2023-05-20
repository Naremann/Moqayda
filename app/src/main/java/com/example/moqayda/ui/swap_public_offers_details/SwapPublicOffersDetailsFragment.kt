package com.example.moqayda.ui.swap_public_offers_details

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.moqayda.R
import com.example.moqayda.api.RetrofitBuilder.retrofitService
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentSwapPublicOffersDetailsBinding
import kotlinx.coroutines.launch

class SwapPublicOffersDetailsFragment : BaseFragment<FragmentSwapPublicOffersDetailsBinding,SwapPublicOffersDetailsViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.vm=viewModel
        getSenderItemDetails()
        getReceiverItemDetails()
        getSwapOfferProductsId()

    }
    private fun getReceiverItemDetails(){
        viewDataBinding.receiverProgress.isVisible=true
        lifecycleScope.launch {
            val productId = SwapPublicOffersDetailsFragmentArgs.fromBundle(requireArguments()).receiverProductId
            val productResponse= retrofitService.getProductById(productId).body()
            viewDataBinding.receiverProgress.isVisible=false
            try {
                Log.e("getReceiverItemDetails","Success $productResponse")
                viewModel.receiverProduct.set(productResponse)
            }
            catch (ex:Exception){
                Log.e("getReceiverItemDetails","Fail ${ex.localizedMessage}")
            }
        }
    }

    private fun getSenderItemDetails(){
        viewDataBinding.senderProgress.isVisible=true
        lifecycleScope.launch {
            val senderItemId = SwapPublicOffersDetailsFragmentArgs.fromBundle(requireArguments()).senderProductId

            val publicItemResponse = retrofitService.getProductById(senderItemId).body()
            try {
                Log.e("privateItemResponse","Success $publicItemResponse")
                viewModel.senderPublicItem.set(publicItemResponse)
                val appUser = retrofitService.getUserById(publicItemResponse?.userId).body()
                viewDataBinding.senderProgress.isVisible=false
                try {
                    Log.e("appUser","Success $appUser")
                    viewModel.user.set(appUser)
                }
                catch (ex:Exception){
                    Log.e("appUser","Fail ${ex.localizedMessage}")

                }
            }
            catch (ex:Exception){
                Log.e("privateItemResponse","Fail ${ex.localizedMessage}")
            }


        }
    }
    private fun getSwapOfferProductsId() {
        Log.e("getSwapOfferProductsId","receiverProductId ${SwapPublicOffersDetailsFragmentArgs.fromBundle(requireArguments()).receiverProductId}")

        Log.e("getSwapOfferProductsId","senderProductId ${SwapPublicOffersDetailsFragmentArgs.fromBundle(requireArguments()).senderProductId}")
        viewModel.receiverProductId = SwapPublicOffersDetailsFragmentArgs.fromBundle(requireArguments()).receiverProductId
        viewModel.senderItemId = SwapPublicOffersDetailsFragmentArgs.fromBundle(requireArguments()).senderProductId
    }

    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): SwapPublicOffersDetailsViewModel {
        return ViewModelProvider(this)[SwapPublicOffersDetailsViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_swap_public_offers_details
    }

}