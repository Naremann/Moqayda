package com.example.moqayda.ui.otherUserProfile

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moqayda.R
import com.example.moqayda.api.ApiService
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.models.UserBlockage
import com.example.moqayda.ui.blockedUsers.BlockedUsersViewModel
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import retrofit2.Response

@RunWith(AndroidJUnit4::class)
class OtherUserProfileViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val context: Context = ApplicationProvider.getApplicationContext()
    private lateinit var viewModel: OtherUserProfileViewModel
    private val apiServiceMock: ApiService = Mockito.mock(ApiService::class.java)

    @Before
    fun setUp() {
        viewModel = OtherUserProfileViewModel(context)
    }

    @Test
    fun blockUser(): Unit = runBlocking {
        val retrofitBuilderMock = Mockito.mock(RetrofitBuilder::class.java)
        Mockito.`when`(retrofitBuilderMock.retrofitService)
            .thenReturn(apiServiceMock)

        val userBlockage = UserBlockage(1, "226688", "564513")
        val valueLResponse: ResponseBody = ResponseBody.create(null, "Success")
        Mockito.`when`(apiServiceMock.blockUser(userBlockage))
            .thenReturn(Response.success(valueLResponse))

        viewModel.blockUser("564513")
        viewModel.messageLiveData.observeForever { }
        assertEquals(
            viewModel.messageLiveData.value,
            context.getString(R.string.product_already_in_your_favorites)
        )
    }
}