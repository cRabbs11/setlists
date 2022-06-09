package com.example.evgeny.setlist_mobile.data.dao

import androidx.room.*
import com.example.evgeny.setlist_mobile.data.AppDataBase
import com.example.evgeny.setlist_mobile.data.Artist
import com.example.evgeny.setlist_mobile.data.SearchQuery


@Dao
interface ArtistDao {

    @Query("SELECT * FROM ${AppDataBase.SAVED_ARTISTS_TABLE_NAME}")
    fun getAllArtists() : List<Artist>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArtist(artist: Artist)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArtists(artists: List<Artist>)

    @Delete
    fun deleteArtist(artist: Artist)

    @Update
    fun updateArtist(artist: Artist)

    @Query("SELECT * FROM ${AppDataBase.SAVED_SEARCH_QUERRY_TABLE_NAME}")
    fun getSearchQueryArtists() : List<SearchQuery>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSearchQuery(searchQuery: SearchQuery)
}