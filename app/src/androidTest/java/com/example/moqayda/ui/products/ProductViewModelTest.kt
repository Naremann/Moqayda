package com.example.moqayda.ui.products

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductViewModelTest{
    lateinit var viewModel : ProductViewModel

    @Before
    fun setUp(){
        viewModel= ProductViewModel(ApplicationProvider.getApplicationContext())
    }
    @Test
    fun addProductToFavoriteWithNullIdReturnsNull() = runBlockingTest{
        val product = viewModel.addProductToFavorite(1)
        Assert.assertEquals(null,product)
    }
}