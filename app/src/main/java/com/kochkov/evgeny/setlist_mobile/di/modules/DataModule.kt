package com.kochkov.evgeny.setlist_mobile.di.modules

import android.content.Context
import androidx.room.Room
import com.kochkov.evgeny.setlist_mobile.data.AppDataBase
import com.kochkov.evgeny.setlist_mobile.data.RoomRepository
import com.kochkov.evgeny.setlist_mobile.data.dao.ArtistDao
import com.kochkov.evgeny.setlist_mobile.utils.SearchHistoryHelper
import com.kochkov.evgeny.setlist_mobile.remote.SetlistsRepository
import com.kochkov.evgeny.setlist_mobile.domain.repository.LocalRepository
import com.kochkov.evgeny.setlist_mobile.domain.repository.RemoteRepository
import com.kochkov.evgeny.setlist_mobile.remote.SetlistsRetrofitInterface
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
        retrofit: SetlistsRetrofitInterface
    ): RemoteRepository = SetlistsRepository(retrofit)

    @Singleton
    @Provides
    fun provideLocalRepository(artistDao: ArtistDao): LocalRepository = RoomRepository(artistDao)

    @Singleton
    @Provides
    fun provideSearchHistoryHelper(context: Context): SearchHistoryHelper = SearchHistoryHelper(context)
}