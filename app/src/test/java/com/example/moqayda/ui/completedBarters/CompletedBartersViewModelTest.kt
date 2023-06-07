package com.example.moqayda.ui.completedBarters

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CompletedBartersViewModelTest{

    private val appContext: Context = ApplicationProvider.getApplicationContext()
    private val completedBartersViewModel = CompletedBartersViewModel(appContext)

    @Test
     fun getProduct_nullId_returnsNull() = runBlocking{
        val product = completedBartersViewModel.getProduct(null)
        assertEquals(null,product)
    }


    @Test
    fun getProductUsingProductOwnerId_nullId_returnsNull() = runBlocking {
        val product = completedBartersViewModel.getProductUsingProductOwnerId(null)
        assertEquals(null,product)
    }

}