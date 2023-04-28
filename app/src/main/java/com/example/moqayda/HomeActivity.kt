package com.example.moqayda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.VISIBLE
import androidx.navigation.findNavController
import com.example.moqayda.databinding.ActivityHomeBinding
import com.example.moqayda.ui.setting.LanguagesSettingsHelper
import com.example.moqayda.ui.setting.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLocalLanguage()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomAppBar.visibility = VISIBLE


        binding.bottomNavigationView.setOnItemSelectedListener { item ->

            when(item.itemId){
                R.id.home ->{
                    binding.bottomAppBar.visibility = VISIBLE
                    binding.fabButton.show()
                    findNavController(R.id.home_nav_host_fragment).navigate(R.id.homeFragment)
                    true
                }
                R.id.favorite -> {
                    binding.bottomAppBar.visibility = VISIBLE
                    binding.fabButton.hide()
                    findNavController(R.id.home_nav_host_fragment).navigate(R.id.favoriteFragment)
                    true
                }
                R.id.chat -> {
                    binding.bottomAppBar.visibility = VISIBLE
                    binding.fabButton.hide()
                    findNavController(R.id.home_nav_host_fragment).navigate(R.id.requestFragment)
                    true
                }
                R.id.profile -> {
                    binding.bottomAppBar.visibility = VISIBLE
                    binding.fabButton.hide()
                    findNavController(R.id.home_nav_host_fragment).navigate(R.id.profileFragment)
                    true
                }
                else -> false

            }

        }

        binding.fabButton.setOnClickListener {
            findNavController(R.id.home_nav_host_fragment).navigate(R.id.selectCategoryFragment)
        }

    }
    override fun onBackPressed() {
        findNavController(R.id.home_nav_host_fragment).popBackStack()
        Log.e("onBackPressed", findNavController(R.id.home_nav_host_fragment).currentDestination?.id.toString())
        when (findNavController(R.id.home_nav_host_fragment).currentDestination?.id) {
            R.id.homeFragment -> {
                binding.bottomAppBar.visibility = VISIBLE
                binding.bottomNavigationView.selectedItemId = R.id.home
                binding.fabButton.show()
                super.onBackPressed()
            }
            R.id.favoriteFragment -> {
                binding.bottomAppBar.visibility = VISIBLE
                binding.bottomNavigationView.selectedItemId = R.id.favorite
                binding.fabButton.hide()
                super.onBackPressed()
            }
            R.id.requestFragment -> {
                binding.bottomAppBar.visibility = VISIBLE
                binding.bottomNavigationView.selectedItemId = R.id.chat
                binding.fabButton.hide()
                super.onBackPressed()
            }
            R.id.profileFragment -> {
                binding.bottomAppBar.visibility = VISIBLE
                binding.bottomNavigationView.selectedItemId = R.id.profile
                binding.fabButton.hide()
                super.onBackPressed()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }
    private fun setLocalLanguage() {
        var data = LanguagesSettingsHelper.retreiveDataFromSharedPreferences("lang", this)
        if (data == "ar") {
            LocaleHelper.setLocale(this, "ar")
        } else {
            Log.e("lang", data)
            LocaleHelper.setLocale(this, "en")

        }
    }



}