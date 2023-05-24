package com.example.moqayda

import android.os.Bundle
import android.util.Log
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import com.example.moqayda.database.local.LanguagesSettingsHelper
import com.example.moqayda.database.local.LocaleHelper
import com.example.moqayda.database.local.ThemeModeSettingHelper.Companion.getThemeMode
import com.example.moqayda.databinding.ActivityHomeBinding
import com.example.moqayda.ui.home.HomeFragmentDirections
import com.example.moqayda.ui.products.ProductFragmentDirections
import com.example.moqayda.ui.products.ProductFragmentDirections.ActionProductsListFragmentToOtherUserProfileFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLocalLanguage()
        checkAppThemeMode()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomAppBar.visibility = VISIBLE


        binding.bottomNavigationView.setOnItemSelectedListener { item ->

            when (item.itemId) {
                R.id.home -> {
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
            if (findNavController(R.id.home_nav_host_fragment).currentDestination?.id == R.id.homeFragment) {
                this.findNavController(R.id.home_nav_host_fragment)
                    .navigate(
                        HomeFragmentDirections.actionHomeFragmentToSelectCategoryFragment(
                            null, false
                        )
                    )
            } else if (findNavController(R.id.home_nav_host_fragment).currentDestination?.id == R.id.productsListFragment) {
                findNavController(R.id.home_nav_host_fragment).navigate(
                    ProductFragmentDirections.actionProductsListFragmentToSelectCategoryFragment(
                        null,
                        false
                    )
                )
            }

        }

    }

    override fun onBackPressed() {
        findNavController(R.id.home_nav_host_fragment).popBackStack()
        Log.e(
            "onBackPressed",
            findNavController(R.id.home_nav_host_fragment).currentDestination?.id.toString()
        )
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
        val data = LanguagesSettingsHelper.retreiveDataFromSharedPreferences("lang", this)
        if (data == "ar") {
            LocaleHelper.setLocale(this, "ar")
        } else {
            Log.e("lang", data)
            LocaleHelper.setLocale(this, "en")

        }
    }

    private fun checkAppThemeMode() {
        if (getThemeMode(this))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }


}