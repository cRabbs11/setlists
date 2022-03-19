package com.example.evgeny.setlist_mobile.di.modules

import android.content.Context
import com.example.evgeny.setlist_mobile.utils.SearchHistoryHelper
import com.example.evgeny.setlist_mobile.utils.SetlistsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DataModule(val context: Context) {


    @Provides
    fun provideContext(): Context = context

    @Singleton
    @Provides
    fun provideSetlistRepository(): SetlistsRepository = SetlistsRepository

    @Singleton
    @Provides
    fun provideSearchHistoryHelper(context: Context): SearchHistoryHelper = SearchHistoryHelper(context)
}