package com.example.moqayda.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.moqayda.databinding.FragmentListProductsBinding
import com.example.moqayda.models.CategoryItem

class ProductsListFragment: Fragment() {

    private lateinit var binding:FragmentListProductsBinding
    private lateinit var category:CategoryItem
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentListProductsBinding.inflate(layoutInflater)

        category = ProductsListFragmentArgs.fromBundle(requireArguments()).selectedCategory
        binding.categoryName.text = category.name



        return binding.root
    }

}