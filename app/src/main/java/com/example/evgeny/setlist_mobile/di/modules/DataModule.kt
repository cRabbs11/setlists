package com.example.evgeny.setlist_mobile.di.modules

import android.content.Context
import androidx.room.Room
import com.example.evgeny.setlist_mobile.data.AppDataBase
import com.example.evgeny.setlist_mobile.data.dao.ArtistDao
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
    fun provideArtistDao(): ArtistDao {
        val database = Room.databaseBuilder(
                context,
                AppDataBase::class.java,
                AppDataBase.SAVED_ARTISTS_TABLE_NAME
        ).build().artistDao()
        return database
    }

    @Singleton
    @Provides
    fun provideSetlistRepository(): SetlistsRepository = SetlistsRepository

    @Singleton
    @Provides
    fun provideSearchHistoryHelper(context: Context): SearchHistoryHelper = SearchHistoryHelper(context)
}