package com.example.evgeny.setlist_mobile.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.evgeny.setlist_mobile.data.dao.ArtistDao
import com.example.evgeny.setlist_mobile.data.entity.Setlist
import com.example.evgeny.setlist_mobile.utils.SetsTypeConverter

@Database(entities = [
    Artist::class,
    SearchQuery::class,
    Setlist::class],
    version = 2, exportSchema = true)
@TypeConverters(SetsTypeConverter::class)
abstract class AppDataBase: RoomDatabase() {

    abstract fun artistDao(): ArtistDao

    companion object {
        const val SAVED_ARTISTS_TABLE_NAME = "artists_table"
        const val SAVED_SEARCH_QUERRY_TABLE_NAME = "saved_search_query_table"
        const val CASHED_SETLISTS_TABLE_NAME = "cashed_setlists_table"

        const val SEARCH_TYPE_ARTISTS = 1
    }
}