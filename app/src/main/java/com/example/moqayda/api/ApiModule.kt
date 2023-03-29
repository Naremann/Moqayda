package com.example.moqayda.api

import android.util.Log
import com.example.moqayda.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    fun provideInterceptor():HttpLoggingInterceptor{
        val logginInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.e("api",message)
            }
            
        })
        logginInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return logginInterceptor
    }
    @Provides
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor):OkHttpClient{
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Provides
    fun provideGsonFactory():GsonConverterFactory{
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideRetrofit(gsonFactory: GsonConverterFactory,client: OkHttpClient):Retrofit{
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            //.addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(client)
            .addConverterFactory(gsonFactory)
            .build()
    }

    @Provides
    fun provideWebService(retrofit: Retrofit):ApiService{
        return retrofit.create(ApiService::class.java)
    }
}