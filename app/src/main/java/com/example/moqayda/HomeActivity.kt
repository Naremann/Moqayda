package com.example.moqayda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
}