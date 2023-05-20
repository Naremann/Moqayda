package com.example.moqayda.ui.swa_public_offers

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentSwapOffersOfPublicItemsBinding
import com.example.moqayda.ui.swap_private_offers.SwapPrivateOffersAdapter

class SwapOffersOfPublicItemsFragment : BaseFragment<FragmentSwapOffersOfPublicItemsBinding,SwapOffersOfPublicItemsViewModel>(),Navigator {
    var adapter = SwapPublicOffersAdapter(swapPublicOffersFragment = this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator=this
        observeToLiveData()
        initRecycler()

    }
    private fun observeToLiveData() {
        viewModel.swappublicOffers.observe(viewLifecycleOwner){ swappublicOffers->
            adapter.changeData(swappublicOffers)
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


    }
    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): SwapOffersOfPublicItemsViewModel {
        return ViewModelProvider(this)[SwapOffersOfPublicItemsViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_swap_offers_of_public_items
    }

}