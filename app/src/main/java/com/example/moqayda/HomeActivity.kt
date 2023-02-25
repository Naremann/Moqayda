package com.example.moqayda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import com.example.moqayda.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.setOnItemSelectedListener { item ->

            when(item.itemId){
                R.id.home ->{
                    binding.fabButton.show()
                    findNavController(R.id.home_nav_host_fragment).navigate(R.id.homeFragment)
                    true
                }
                R.id.favorite -> {
                    binding.fabButton.hide()
                    findNavController(R.id.home_nav_host_fragment).navigate(R.id.favoriteFragment)
                    true
                }
                R.id.chat -> {
                    binding.fabButton.hide()
                    findNavController(R.id.home_nav_host_fragment).navigate(R.id.chatFragment)
                    true
                }
                R.id.profile -> {
                    binding.fabButton.hide()
                    findNavController(R.id.home_nav_host_fragment).navigate(R.id.profileFragment)
                    true
                }
                else -> false

            }

        }

    }
    override fun onBackPressed() {
        findNavController(R.id.home_nav_host_fragment).popBackStack()
        Log.e("onBackPressed", findNavController(R.id.home_nav_host_fragment).currentDestination?.id.toString())
        when (findNavController(R.id.home_nav_host_fragment).currentDestination?.id) {
            R.id.homeFragment -> {
                binding.bottomNavigationView.selectedItemId = R.id.home
                binding.fabButton.show()
                super.onBackPressed()
            }
            R.id.favoriteFragment -> {
                binding.bottomNavigationView.selectedItemId = R.id.favorite
                binding.fabButton.hide()
                super.onBackPressed()
            }
            R.id.chatFragment -> {
                binding.bottomNavigationView.selectedItemId = R.id.chat
                binding.fabButton.hide()
                super.onBackPressed()
            }
            R.id.profileFragment -> {
                binding.bottomNavigationView.selectedItemId = R.id.profile
                binding.fabButton.hide()
                super.onBackPressed()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }



}