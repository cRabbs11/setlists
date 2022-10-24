package com.kochkov.evgeny.setlist_mobile.di.modules

import com.kochkov.evgeny.setlist_mobile.utils.ApiKeys
import com.kochkov.evgeny.setlist_mobile.utils.SetlistsAPIConstants
import com.kochkov.evgeny.setlist_mobile.utils.SetlistsRetrofitInterface
import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class RemoteModule {

    private val CALL_TIMEOUT_MILLI_30 = 30L

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            callTimeout(CALL_TIMEOUT_MILLI_30, TimeUnit.SECONDS)
            readTimeout(CALL_TIMEOUT_MILLI_30, TimeUnit.SECONDS)
            addInterceptor {
                val request = it.request()
                        .newBuilder()
                        .addHeader(SetlistsAPIConstants.HEADER_NAME_API_KEY, ApiKeys.SETLISTS_API_KEY)
                        .addHeader(SetlistsAPIConstants.HEADER_NAME_ACCEPT, SetlistsAPIConstants.ACCEPT_HEADER)
                        .build()
                it.proceed(request)
            }
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }).build()
        }.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): SetlistsRetrofitInterface{
        val retrofit = Retrofit.Builder()
                .baseUrl(SetlistsAPIConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(okHttpClient)
                .build().create(SetlistsRetrofitInterface::class.java)
        return retrofit
    }
}