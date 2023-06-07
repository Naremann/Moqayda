package com.example.moqayda.ui.updtaeProduct

import android.content.Context
import android.net.Uri
import com.example.moqayda.R
import com.example.moqayda.getOrAwaitValue
import kotlinx.coroutines.test.runBlockingTest
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.example.moqayda.repo.product.ProductRepository
import com.example.moqayda.ui.updateProduct.UpdateProductViewModel

class UpdateProductViewModel {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    lateinit var repo: ProductRepository

    private val context : Context = ApplicationProvider.getApplicationContext()
    lateinit var viewModel : UpdateProductViewModel

    @Before
    fun setUp(){
        repo= ProductRepository(context)
        viewModel= UpdateProductViewModel(context)
    }


    @Test
    fun updateItemWithEmptyFieldsReturnErrorMessage() = runBlockingTest{
        repo.updateProduct("id","","desc","false","1",
            "0","a", Uri.parse("https://image.tmdb.org/t/p/w92/dRLSoufWtc16F5fliK4ECIVs56p.jpg"))
        val value = viewModel.messageLiveData.getOrAwaitValue()
        Assert.assertEquals(context.getString(R.string.fill_all_fields),value)
    }

}