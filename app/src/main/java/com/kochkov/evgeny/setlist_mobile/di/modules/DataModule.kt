package com.kochkov.evgeny.setlist_mobile.di.modules

import android.content.Context
import androidx.room.Room
import com.kochkov.evgeny.setlist_mobile.data.AppDataBase
import com.kochkov.evgeny.setlist_mobile.data.dao.ArtistDao
import com.kochkov.evgeny.setlist_mobile.utils.SearchHistoryHelper
import com.kochkov.evgeny.setlist_mobile.data.SetlistsRepository
import com.kochkov.evgeny.setlist_mobile.utils.SetlistsRetrofitInterface
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
    fun provideSetlistRepository(
        artistDao: ArtistDao,
        retrofit: SetlistsRetrofitInterface): SetlistsRepository = SetlistsRepository(artistDao, retrofit)

    @Singleton
    @Provides
    fun provideSearchHistoryHelper(context: Context): SearchHistoryHelper = SearchHistoryHelper(context)
}