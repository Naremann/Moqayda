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
import com.example.moqayda.repo.product.Resource
import com.example.moqayda.ui.updateProduct.UpdateProductViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
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

        //content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F31/ORIGINAL/NONE/1616429549

        repo.updateProduct("id","","desc","false","1",
            "0","a", Uri.parse("android.resource://${context.packageName}/" + R.drawable.background))
        val value = viewModel.toastMessage.getOrAwaitValue()

        Assert.assertEquals(context.getString(R.string.fill_all_fields),value)
    }

}