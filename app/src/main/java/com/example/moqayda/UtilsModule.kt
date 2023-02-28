package com.example.moqayda

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object UtilsModule {
    @Provides
    fun provideNetWorkHandler():NetworkHandler{
        return NetworkHandlerImp()
    }

}