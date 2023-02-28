package com.example.moqayda.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentHomeBinding
import com.example.moqayda.models.CategoryItem

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(){
    private lateinit var adapter: CategoryItemAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.vm=viewModel
        initRecyclerView()
        observeToLiveData()

    }

    private fun initRecyclerView() {
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(this.context, 2)
       viewDataBinding.categoryRecycler.layoutManager = layoutManager
    }


    private fun observeToLiveData() {
        viewModel.navigateToProductListFragment.observe(viewLifecycleOwner) {
            it?.let {
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToProductsListFragment(it))
                viewModel.onProductListNavigated()
            }

        }
        viewModel.categoryList.observe(viewLifecycleOwner) { data ->
            adapter = CategoryItemAdapter(data, CategoryListener { categoryItem ->
                viewModel.onCategorySelected(categoryItem)
            })



            viewDataBinding.categoryRecycler.adapter = adapter
        }
        viewModel.progressBarVisible.observe(viewLifecycleOwner) { isVisibleProgress ->
            viewDataBinding.progressBar.isVisible = isVisibleProgress
        }
    }

    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): HomeViewModel {
        return ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

}