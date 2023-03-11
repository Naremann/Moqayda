package com.example.moqayda.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.moqayda.R
import com.example.moqayda.databinding.FragmentFavoriteBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FavoriteFragment: Fragment() {
    private lateinit var binding:FragmentFavoriteBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater)


        val activityBottomAppBar = activity?.findViewById<BottomAppBar>(R.id.bottomAppBar)
        val activityBottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val activityFabButton = activity?.findViewById<FloatingActionButton>(R.id.fabButton)

        activityBottomAppBar?.visibility = View.VISIBLE
        activityBottomNavigationView?.visibility = View.VISIBLE
        activityFabButton?.visibility = View.GONE

        return binding.root
    }
}