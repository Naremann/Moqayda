package com.example.moqayda.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentFavoriteBinding
import com.example.moqayda.models.Product

class FavoriteFragment: BaseFragment<FragmentFavoriteBinding,FavoriteViewModel>(),Navigator {
    private lateinit var adapter: FavoriteAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        showBottomAppBar()
        hideFloatingBtn()

        initRecyclerView()
        viewModel.navigator = this
        viewModel.favoriteProducts.observe(viewLifecycleOwner){
            val list = it.reversed()
            adapter = FavoriteAdapter(list, requireContext(),this)
            it?.forEach{item ->
                item.name?.let { it1 -> Log.e("FavoriteFragment", it1) }
            }
            viewDataBinding.recyclerView.adapter = adapter
        }

    }

    private fun initRecyclerView() {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        viewDataBinding.recyclerView.layoutManager = layoutManager
    }

    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): FavoriteViewModel {
        return ViewModelProvider(this)[FavoriteViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_favorite
    }

    override fun onNavigateToProductDetails(product: Product) {
        findNavController().navigate(FavoriteFragmentDirections.actionFavoriteFragmentToProductDetailsFragment(product))
    }
}