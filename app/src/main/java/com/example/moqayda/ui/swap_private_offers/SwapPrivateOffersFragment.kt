package com.example.moqayda.ui.swap_private_offers

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentSwapPrivateOffersBinding
import com.example.moqayda.models.Product
import com.example.moqayda.ui.user_public_items.UserPublicItemAdapter

class SwapPrivateOffersFragment : BaseFragment<FragmentSwapPrivateOffersBinding,SwapPrivateOffersViewModel>() {
    var adapter = SwapPrivateOffersAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeToLiveData()
        initRecycler()

    }
    private fun observeToLiveData() {
        viewModel.swapPrivateOffers.observe(viewLifecycleOwner){ swapPrivateOffers->
            adapter.changeData(swapPrivateOffers)
            adapter.notifyDataSetChanged()

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

    override fun initViewModeL(): SwapPrivateOffersViewModel {
        return ViewModelProvider(this)[SwapPrivateOffersViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_swap_private_offers
    }

}