package com.example.moqayda.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.HomeActivity
import com.example.moqayda.R
import com.example.moqayda.databinding.ActivityHomeBinding
import com.example.moqayda.databinding.FragmentHomeBinding
import com.example.moqayda.models.CategoryItem
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomViewModel
    private lateinit var adapter: CategoryItemAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater)

        viewModel = ViewModelProvider(this)[HomViewModel::class.java]
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(this.context, 2)
        binding.categoryRecycler.layoutManager = layoutManager

        viewModel.categoryList.observe(viewLifecycleOwner, Observer { data ->
            adapter = CategoryItemAdapter(data, CategoryListener { categoryItem ->

                viewModel.onCategorySelected(categoryItem)

            })



            binding.categoryRecycler.adapter = adapter
        })


        viewModel.navigateToProductListFragment.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToProductsListFragment(it))
                viewModel.onProductListNavigated()
            }

        })


        return binding.root
    }
}