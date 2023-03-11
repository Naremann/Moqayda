package com.example.moqayda.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.moqayda.R
import com.example.moqayda.databinding.FragmentChatBinding
import com.example.moqayda.databinding.FragmentFavoriteBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ChatFragment: Fragment() {

    private lateinit var binding: FragmentChatBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChatBinding.inflate(inflater)

        val activityBottomAppBar = activity?.findViewById<BottomAppBar>(R.id.bottomAppBar)
        val activityBottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val activityFabButton = activity?.findViewById<FloatingActionButton>(R.id.fabButton)

        activityBottomAppBar?.visibility = VISIBLE
        activityBottomNavigationView?.visibility = VISIBLE
        activityFabButton?.visibility = GONE


        return binding.root
    }

}