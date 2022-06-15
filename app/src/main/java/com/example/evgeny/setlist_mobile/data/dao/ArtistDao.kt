package com.example.evgeny.setlist_mobile.data.dao

import androidx.room.*
import com.example.evgeny.setlist_mobile.data.AppDataBase
import com.example.evgeny.setlist_mobile.data.Artist
import com.example.evgeny.setlist_mobile.data.SearchQuery
import com.example.evgeny.setlist_mobile.data.entity.Setlist
import io.reactivex.rxjava3.core.Observable


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

    @Query("DELETE FROM ${AppDataBase.CASHED_SETLISTS_TABLE_NAME}")
    fun clearSetlists()

    @Update
    fun updateArtist(artist: Artist)

    @Query("SELECT * FROM ${AppDataBase.SAVED_SEARCH_QUERRY_TABLE_NAME}")
    fun getSearchQueryArtists() : List<SearchQuery>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSearchQuery(searchQuery: SearchQuery)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSetlists(setlists: List<Setlist>)

    @Query("SELECT * FROM ${AppDataBase.CASHED_SETLISTS_TABLE_NAME}")
    fun getSetlists(): Observable<List<Setlist>>
}