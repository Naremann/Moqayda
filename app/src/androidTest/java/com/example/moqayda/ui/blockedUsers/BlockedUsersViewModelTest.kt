package com.example.moqayda.ui.blockedUsers
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moqayda.api.ApiService
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.models.AppUser
import com.example.moqayda.models.UserBlockage
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import retrofit2.Response

@RunWith(AndroidJUnit4::class)
class BlockedUsersViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val context: Context = ApplicationProvider.getApplicationContext()
    private lateinit var viewModel: BlockedUsersViewModel
    private val apiServiceMock: ApiService = mock(ApiService::class.java)
    @Before
    fun setUp() {
        viewModel = BlockedUsersViewModel(context)
    }
    @Test
    fun testGetBlockedUsers(): Unit = runBlocking {
        val retrofitBuilderMock = mock(RetrofitBuilder::class.java)
        Mockito.`when`(retrofitBuilderMock.retrofitService)
            .thenReturn(apiServiceMock)

        Mockito.`when`(apiServiceMock.getBlockedUsers())
            .thenReturn(Response.success(listOf(UserBlockage(1, "07775000", "226688"))))

        Mockito.`when`(apiServiceMock.getUserById("07775000"))
            .thenReturn(Response.success(AppUser("07775000","Ahmed","Talaat")))

        Mockito.`when`(apiServiceMock.getUserById("226688"))
            .thenReturn(Response.success(AppUser("226688","Abdo","Talaat")))

        viewModel.getBlockedUsers()
        viewModel.userBlockage.observeForever { }
        assert(viewModel.userBlockage.value?.size == 1)
    }
}
