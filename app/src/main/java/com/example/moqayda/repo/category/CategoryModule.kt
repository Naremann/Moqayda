package com.example.moqayda.repo.category

import com.example.moqayda.NetworkHandler
import com.example.moqayda.api.ApiInterface
import com.example.moqayda.database.MyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CategoryModule {

    @Provides
    fun provideOfflineCategory(database: MyDatabase):CategoryOfflineRepository{
        return CategoryOfflineRepositoryImp(database)
    }
    @Provides
    fun provideOnlineCategory(webServices: ApiInterface):CategoryOnlineRepository{
        return CategoryOnlineRepositoryImp(webServices)
    }

    @Provides
    fun provideCategoryDataSource(networkHandler: NetworkHandler,
                                  onlineRepository: CategoryOnlineRepository,
                                  offlineRepository: CategoryOfflineRepository
    ):CategoryRepository{
        return CategoryRepositoryImp(networkHandler,onlineRepository,offlineRepository)
    }
}