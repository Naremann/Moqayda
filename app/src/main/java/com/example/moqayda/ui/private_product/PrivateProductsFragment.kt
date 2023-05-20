package com.example.moqayda.ui.private_product

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.DataUtils
import com.example.moqayda.R
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.api.RetrofitBuilder.retrofitService
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentPrivateProductsBinding
import com.example.moqayda.initToolbar
import com.example.moqayda.models.*
import kotlinx.coroutines.launch

class PrivateProductsFragment :
    BaseFragment<FragmentPrivateProductsBinding, PrivateProductViewModel>(),Navigator {

    private  var adapter= PrivateProductAdapter(privateProductsFragment = this)
    private var productList: MutableList<PrivateProduct> = mutableListOf()
    private var filteredList: MutableList<PrivateProduct> = mutableListOf()

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
            for (i in 0 until item.name!!.length) {
                if (newText != null) {
                    if (newText.lowercase().contains(item.name[i].lowercase())
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
        adapter.onSwapLinearClickListener = object :PrivateProductAdapter.OnSwapLinearClickListener {
            override fun onSwapLinearClick(privateProductItem: PrivateProduct?) {
              addPrivateItemOwner(privateProductItem?.id!!)
                navigateToSwapRequestFragment(privateProductItem!!)
            }


        }
    }
    fun addPrivateItemOwner(productId:Int){
        showProgressDialog()
        val userId = DataUtils.USER?.id
        val privateOwnerItem = PrivateProductOwnerResponse(id=0, privateItemId = productId, userId = userId!!)
        lifecycleScope.launch {
            val response = retrofitService.addPrivateItemOwner(privateOwnerItem)
            hideProgressDialog()
            try {
                if(response.isSuccessful){
                    Log.e("addPrivateItemOwner","Success ${response.body()}")
                }
                else
                    Log.e("addPrivateItemOwner","Fail :Unknown")


            }
            catch (ex:Exception){
                Log.e("addPrivateItemOwner","Fail ${ex.localizedMessage}")

            }
        }
    }

    private fun navigateToSwapRequestFragment(privateProductItem:PrivateProduct) {

        val product = PrivateProductsFragmentArgs.fromBundle(requireArguments()).product
        findNavController().navigate(PrivateProductsFragmentDirections.actionPrivateProductsFragmentToSwapRequestFragment(privateProductItem.id!!,
            product,privateProductItem.pathImage,privateProductItem.name))
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