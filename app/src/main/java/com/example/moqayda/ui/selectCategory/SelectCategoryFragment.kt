package com.example.moqayda.ui.selectCategory

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentSelectCategoryBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectCategoryFragment :
    BaseFragment<FragmentSelectCategoryBinding, SelectCategoryViewModel>(), Navigator {

    private lateinit var adapter: SelectCategoryAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.viewModel = viewModel
        viewModel.navigator = this
        observeToLiveData()
        activity?.findViewById<FloatingActionButton>(R.id.fabButton)?.hide()
    }

    private fun observeToLiveData() {
        viewModel.categoryList.observe(viewLifecycleOwner) { data ->

            adapter = data?.let {
                SelectCategoryAdapter(it, CategoryListener { categoryItem ->
                    viewModel.navigateToAddProduct(categoryItem)
                })

            }!!
            viewDataBinding.categoriesRecyclerView.adapter = adapter
        }

        viewModel.progressBarVisible.observe(viewLifecycleOwner) { isVisibleProgress ->
            viewDataBinding.progressBar.isVisible = isVisibleProgress
        }
    }


    override fun getViews(): View {
        return viewDataBinding.root
    }


    override fun initViewModeL(): SelectCategoryViewModel {
        return ViewModelProvider(this)[SelectCategoryViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_select_category
    }

    override fun onNavigateToAddProductFragment(
        categoryId: Int,
        categoryPathImage: String,
        categoryBackgroundColor: Int
    ) {
        this.findNavController().navigate(
            SelectCategoryFragmentDirections.actionSelectCategoryFragmentToAddProductFragment(
                categoryPathImage,categoryBackgroundColor,categoryId
            )
        )
    }


}
