package com.example.moqayda.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.R
import com.example.moqayda.databinding.FragmentHomeBinding
import com.example.moqayda.models.CategoryItem

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater)

        val categoryList = listOf(
            CategoryItem(
                null,
                null,
                true,
                "Electronics",
                R.drawable.img_electronics,
                R.color.electronics_category_color
            ),
            CategoryItem(
                null,
                null,
                true,
                "Furniture",
                R.drawable.img_furniture,
                R.color.category_furniture_color
            ),
            CategoryItem(
                null,
                null,
                true,
                "Fashion",
                R.drawable.img_fashion,
                R.color.category_fashion_color
            ),
            CategoryItem(
                null,
                null,
                true,
                "Pets",
                R.drawable.img_pets,
                R.color.category_pets_color
            ),
            CategoryItem(
                null,
                null,
                true,
                "Books",
                R.drawable.img_books,
                R.color.category_books_color
            ),
            CategoryItem(
                null,
                null,
                true,
                "Other",
                R.drawable.img_other,
                R.color.category_other_color
            )
        )

        val adapter = CategoryItemAdapter(categoryList)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(this.context, 2)
        binding.categoryRecycler.layoutManager = layoutManager
        binding.categoryRecycler.adapter = adapter
        return binding.root
    }
}