package com.example.moqayda.ui.AddProduct

import android.content.Context
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.net.toUri
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moqayda.repo.product.ProductRepository
import com.example.moqayda.ui.addProduct.AddProductViewModel
import com.example.moqayda.ui.products.ProductViewModel
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import retrofit2.Response

@RunWith(AndroidJUnit4::class)
class ProductViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val context: Context = ApplicationProvider.getApplicationContext()
    private lateinit var viewModel: AddProductViewModel
    private val productRepositoryMock: ProductRepository = mock(ProductRepository::class.java)

    @Before
    fun setUp() {
        viewModel = AddProductViewModel(context)
    }

    @Test
    fun testUploadProduct_ValidInput_Success() = runBlocking {
        // Prepare test data
        val selectedCategory = "Electronics"
        val imageUri: Uri = "...".toUri() // Provide a valid image Uri
        val fileRealPath = "/path/to/file"
        val userId = "123456789"
        val productName = "Test Product"
        val productDescription = "This is a test product."
        val productToSwap = "Other product"

        // Set the input values in the view model
        viewModel.productName.set(productName)
        viewModel.productDescription.set(productDescription)
        viewModel.productToSwap.set(productToSwap)

        // Set up the expected response from the repository
        Mockito.`when`(productRepositoryMock.uploadProduct(any(), any(), any(), any(), any(), any(), any())
        )
            .thenReturn(Unit)

        // Call the function under test
        viewModel.uploadProduct(selectedCategory, imageUri, fileRealPath, userId)

        // Verify the expected interactions
        verify(productRepositoryMock).uploadProduct(productName, productDescription,
            productToSwap, selectedCategory, imageUri, fileRealPath, userId)

        // Assert the expected results
        assertTrue(viewModel._descriptionHelperText.value.isNullOrBlank())
        assertNull(viewModel._toastMessage.value)
    }
}