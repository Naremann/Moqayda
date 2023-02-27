package com.example.moqayda.api

import com.example.moqayda.Constants
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    fun provideInterceptor():Interceptor{
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }
    @Provides
    fun provideOkHttpClient(interceptor: Interceptor):OkHttpClient{
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Provides
    fun provideGsonFactory():GsonConverterFactory{
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideRetrofit(gsonFactory: GsonConverterFactory, client: OkHttpClient):Retrofit{
        return Retrofit.Builder().baseUrl(Constants.BASE_URL).addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(client)
            .addConverterFactory(gsonFactory)
            .build()
    }

    @Provides
    fun provideWebService(retrofit: Retrofit):ApiInterface{
        return retrofit.create(ApiInterface::class.java)
    }
}