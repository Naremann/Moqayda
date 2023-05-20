package com.example.moqayda.ui.swap_private_offers

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.DataUtils
import com.example.moqayda.R
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.api.RetrofitBuilder.retrofitService
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentSwapPrivateOffersBinding
import com.example.moqayda.models.PrivateProductOwnerByIdResponse
import com.example.moqayda.models.Product
import com.example.moqayda.ui.products.ProductAdapter
import kotlinx.coroutines.launch

class SwapPrivateOffersFragment : BaseFragment<FragmentSwapPrivateOffersBinding,SwapPrivateOffersViewModel>(),Navigator {
    var adapter = SwapPrivateOffersAdapter(swapPrivateOffersFragment = this)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator=this
        observeToLiveData()
        initRecycler()

    }
    private fun observeToLiveData() {
        viewModel.swapPrivateOffers.observe(viewLifecycleOwner){ swapPrivateOffers->
            adapter.changeData(swapPrivateOffers)
            adapter.notifyDataSetChanged()
        }

        viewModel.isVisibleProgressBar.observe(viewLifecycleOwner){isVisible->
            viewDataBinding.progressBar.isVisible=isVisible
        }

        viewModel.toastMessage.observe(viewLifecycleOwner){toastMessage->
            showToastMessage(toastMessage)

        }


    }
    private fun initRecycler() {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext(),
            RecyclerView.VERTICAL,true)
        viewDataBinding.recyclerView.layoutManager=layoutManager
        viewDataBinding.recyclerView.adapter = adapter
        adapter.onDetailsClickListener=object:SwapPrivateOffersAdapter.OnDetailsClickListener{
            override fun onItemClick(senderProductId: Int, receiverProductId: Int) {
                navigateToSwapOfferDetailsFragment(receiverProductId,senderProductId)
            }

        }


    }


    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): SwapPrivateOffersViewModel {
        return ViewModelProvider(this)[SwapPrivateOffersViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_swap_private_offers
    }

    fun navigateToSwapOfferDetailsFragment(productId: Int, privateItemId: Int) {
        findNavController().navigate(SwapPrivateOffersFragmentDirections.actionSwapPrivateOffersFragmentToSwapPrivateOffersDetailsFragment(productId,privateItemId))
    }

}