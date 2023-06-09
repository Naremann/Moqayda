package com.example.moqayda.ui.updtaeProduct

import android.content.Context
import android.net.Uri
import com.example.moqayda.R
import com.example.moqayda.getOrAwaitValue
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.moqayda.MainCoroutineRule
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.example.moqayda.repo.product.ProductRepository
import com.example.moqayda.ui.updateProduct.UpdateProductViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class UpdateProductViewModel {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var repo: ProductRepository
    private val context : Context = ApplicationProvider.getApplicationContext()
    lateinit var viewModel : UpdateProductViewModel

    @Before
    fun setUp(){
        repo= ProductRepository(context)
        viewModel= UpdateProductViewModel(context)
    }

    @Test
    fun updateItemWithEmptyFieldsReturnErrorMessage() = runBlocking{
        repo.updateProduct("id",null,"desc","false","1",
            "0","phone", Uri.parse("android.resource://${context.packageName}/" + R.drawable.background))
        val value = viewModel.toastMessage.getOrAwaitValue()

        Assert.assertEquals(context.getString(R.string.fill_all_fields),value)
    }

    @Test
    fun updateItemWithDescriptionLengthMoreThan100thanCharacterReturnErrorMessage() = runBlocking{
        repo.updateProduct("id",
            "portable laptop stand",
            "Rillatek-ae portable laptop stand, aluminium portable notebook riser, aluminum construction 6-position adjustable " +
                    "ergonomic design, fits laptops and tablets from 10 to 15.6(silver)"

            ,"false","1",
            "0","laptop stand", Uri.parse("android.resource://${context.packageName}/" + R.drawable.background))
        val value = viewModel.descriptionHelperText.getOrAwaitValue()

        Assert.assertEquals(context.getString(R.string.maximum_100_characters),value)
    }

}