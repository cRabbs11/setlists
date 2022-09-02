package com.example.evgeny.setlist_mobile.di.modules

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.evgeny.setlist_mobile.data.AppDataBase
import com.example.evgeny.setlist_mobile.data.dao.ArtistDao
import com.example.evgeny.setlist_mobile.utils.SearchHistoryHelper
import com.example.evgeny.setlist_mobile.data.SetlistsRepository
import com.example.evgeny.setlist_mobile.utils.SetlistsRetrofitInterface
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

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE ${AppDataBase.SAVED_ARTISTS_TABLE_NAME} ADD COLUMN isWatched INTEGER NOT NULL DEFAULT '0'")
                database.execSQL("ALTER TABLE ${AppDataBase.CASHED_SETLISTS_TABLE_NAME} ADD COLUMN artist_isWatched INTEGER")
            }
        }

        val database = Room.databaseBuilder(
                context,
                AppDataBase::class.java,
                AppDataBase.SAVED_ARTISTS_TABLE_NAME)
            .addMigrations(MIGRATION_1_2)
            .build().artistDao()
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