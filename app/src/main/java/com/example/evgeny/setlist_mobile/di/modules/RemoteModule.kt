package com.example.evgeny.setlist_mobile.di.modules

import com.example.evgeny.setlist_mobile.utils.SetlistsAPIConstants
import com.example.evgeny.setlist_mobile.utils.SetlistsRetrofitInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class RemoteModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val okHttpCLient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply{
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()
        return okHttpCLient
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): SetlistsRetrofitInterface{
        val retrofit = Retrofit.Builder()
                .baseUrl(SetlistsAPIConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build().create(SetlistsRetrofitInterface::class.java)
        return retrofit
    }
}