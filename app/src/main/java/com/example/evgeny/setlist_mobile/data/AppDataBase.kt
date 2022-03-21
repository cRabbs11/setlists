package com.example.evgeny.setlist_mobile.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.evgeny.setlist_mobile.data.dao.ArtistDao

@Database(entities = [Artist::class], version = 1, exportSchema = false)
abstract class AppDataBase: RoomDatabase() {

    abstract fun artistDao(): ArtistDao

    companion object {
        const val SAVED_ARTISTS_TABLE_NAME = "artists_table"
    }
}