package com.example.moqayda.ui.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class FavoriteViewModelTest{

    lateinit var viewModel:FavoriteViewModel

    @Before
    fun setUp(){
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
        viewModel= FavoriteViewModel()

    }

    @Test
    fun getProductOwnerWithNullUserIdReturnNull()= runBlocking {
        val user = viewModel.getProductOwner(null)
        assertEquals(null, user)
    }
}