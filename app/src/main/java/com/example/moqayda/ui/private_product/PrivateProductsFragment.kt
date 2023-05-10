package com.example.moqayda.ui.private_product

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentPrivateProductsBinding
import com.example.moqayda.initToolbar
import com.example.moqayda.models.PrivateItem

class PrivateProductsFragment :
    BaseFragment<FragmentPrivateProductsBinding, PrivateProductViewModel>(),Navigator {

    private  var adapter= PrivateProductAdapter()
    private var productList: MutableList<PrivateItem> = mutableListOf()
    private var filteredList: MutableList<PrivateItem> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomAppBar()
        hideFloatingBtn()
        viewDataBinding.vm = viewModel
        viewModel.navigator=this
        viewDataBinding.toolbar.initToolbar(viewDataBinding.toolbar,getString(R.string.items_in_private),this)
        subscribeToLiveData()
        observeToLiveData()
        initRecycler()
        searchItems()
    }

    private fun searchItems() {
        viewDataBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterItemsList(newText)
                return false
            }
        })
    }

    private fun filterItemsList(newText: String?) {
        filteredList.clear()
        for (item in productList) {
            for (i in 0 until item.privateItemName!!.length) {
                if (newText != null) {
                    if (newText.lowercase().contains(item.privateItemName[i].lowercase())
                    ) {
                        if (item !in filteredList)
                            item.let { filteredList.add(it) }
                    }
                }
            }
        }
        if (filteredList.isEmpty() && newText?.isNotBlank() == true) {
            showToastMessage("No Data Match..", Toast.LENGTH_SHORT)
        } else {
            adapter.changeData(filteredList)
            adapter.notifyDataSetChanged()
        }
    }

    private fun observeToLiveData() {
        viewModel.privateProduct.observe(viewLifecycleOwner){privateProductsList->
            adapter.changeData(privateProductsList)
            adapter.notifyDataSetChanged()

        }
        viewModel.isVisibleProgress.observe(viewLifecycleOwner) { isVisibleProgress ->
            viewDataBinding.progressBar.isVisible = isVisibleProgress
        }

    }

    private fun initRecycler() {

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,true)
        viewDataBinding.recyclerView.layoutManager=layoutManager
        viewDataBinding.recyclerView.adapter = adapter
        adapter.onItemClickListener = object :PrivateProductAdapter.OnItemClickListener {
            override fun onItemClick(productItem: PrivateItem?) {
                TODO("Not yet implemented")
            }


        }
    }






    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): PrivateProductViewModel {
        return ViewModelProvider(this)[PrivateProductViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_private_products
    }

    override fun navigateToAddPrivateProduct() {
        findNavController().navigate(R.id.addPrivateProductFragment)
    }


}