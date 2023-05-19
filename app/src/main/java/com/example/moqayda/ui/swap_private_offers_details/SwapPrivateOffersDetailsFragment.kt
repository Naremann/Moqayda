package com.example.moqayda.ui.swap_private_offers_details

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.moqayda.R
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.api.RetrofitBuilder.retrofitService
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentSwapPrivateOffersDetailsBinding
import kotlinx.coroutines.launch

class SwapPrivateOffersDetailsFragment : BaseFragment<FragmentSwapPrivateOffersDetailsBinding,SwapPrivateOfferDetailsViewModel>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.vm=viewModel
        getPrivateItemDetails()
        getReceiverItemDetails()
        getSwapOfferProductsId()
    }
    private fun getReceiverItemDetails(){
        viewDataBinding.receiverProgress.isVisible=true
        lifecycleScope.launch {
            val productId = SwapPrivateOffersDetailsFragmentArgs.fromBundle(requireArguments()).productId
            val productResponse= retrofitService.getProductById(productId).body()
            viewDataBinding.receiverProgress.isVisible=false
            try {
                Log.e("getReceiverItemDetails","Success $productResponse")
                viewModel.product.set(productResponse)
            }
            catch (ex:Exception){
                Log.e("getReceiverItemDetails","Fail ${ex.localizedMessage}")
            }
        }
    }
    private fun getPrivateItemDetails(){
        viewDataBinding.senderProgress.isVisible=true
        lifecycleScope.launch {
            val privateItemId = SwapPrivateOffersDetailsFragmentArgs.fromBundle(requireArguments()).privateItemId

            val privateItemResponse =  retrofitService.getPrivateProductByItemId(privateItemId)
            try {
                Log.e("privateItemResponse","Success $privateItemResponse")
                viewModel.privateItem.set(privateItemResponse)
                val appUser = retrofitService.getUserById(privateItemResponse.userId).body()
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
        Log.e("getSwapOfferProductsId","productId ${SwapPrivateOffersDetailsFragmentArgs.fromBundle(requireArguments()).productId}")

        Log.e("getSwapOfferProductsId","privateId ${SwapPrivateOffersDetailsFragmentArgs.fromBundle(requireArguments()).privateItemId}")
        viewModel.productId = SwapPrivateOffersDetailsFragmentArgs.fromBundle(requireArguments()).productId
        viewModel.privateItemId = SwapPrivateOffersDetailsFragmentArgs.fromBundle(requireArguments()).privateItemId
    }

    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): SwapPrivateOfferDetailsViewModel {
        return ViewModelProvider(this)[SwapPrivateOfferDetailsViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_swap_private_offers_details
    }

}