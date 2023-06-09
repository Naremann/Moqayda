package com.example.moqayda.database.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.moqayda.models.CategoryItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class CategoryDaoTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: MyDatabase
    private lateinit var dao: CategoryDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MyDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.categoryDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun updateCategoryList() = runBlockingTest {
        val bookCategory = CategoryItem(0, null, 1,
            false, "books","text")
        val electronicsCategory = CategoryItem(0, null, 2,
            false, "electronics","text")
        val categoryItems : MutableList<CategoryItem> = mutableListOf()
        categoryItems.add(bookCategory)
        categoryItems.add(electronicsCategory)
        dao.updateCategory(categoryItems)

        val allCategoryItems = dao.getCategories()
        Assert.assertEquals(allCategoryItems,categoryItems)
    }
}