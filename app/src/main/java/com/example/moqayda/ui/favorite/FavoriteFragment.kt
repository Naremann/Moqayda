package com.example.moqayda.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentFavoriteBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FavoriteFragment: BaseFragment<FragmentFavoriteBinding,FavoriteViewModel>() {
    private lateinit var adapter: FavoriteAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val activityBottomAppBar = activity?.findViewById<BottomAppBar>(R.id.bottomAppBar)
        val activityBottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val activityFabButton = activity?.findViewById<FloatingActionButton>(R.id.fabButton)
        activityBottomAppBar?.visibility = View.VISIBLE
        activityBottomNavigationView?.visibility = View.VISIBLE
        activityFabButton?.visibility = View.GONE

        initRecyclerView()

        viewModel.productsWishlist.observe(viewLifecycleOwner){

            adapter = FavoriteAdapter(it, requireContext(),this,this)

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
}