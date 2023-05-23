package com.example.moqayda.api.fcm
import com.example.moqayda.ui.notification.Api
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object Retrofit {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
    var gson: Gson = GsonBuilder().setLenient().create()
    private var retrofit : Retrofit? = null
    private fun getInstance(): Retrofit {
        if(retrofit ==null){
            retrofit =
                Retrofit.Builder().baseUrl(Api.BASE_URL).addCallAdapterFactory(
                    CoroutineCallAdapterFactory()
                )
                    .client(okHttpClient)
                    .addConverterFactory(
                        GsonConverterFactory.create(gson)
                    ).addConverterFactory(MoshiConverterFactory.create(moshi))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build()
        }
        return retrofit!!
    }

    val retrofitService: Api by lazy {
        getInstance().create(Api::class.java)
    }


}