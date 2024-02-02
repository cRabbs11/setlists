package com.kochkov.evgeny.setlist_mobile.data.dao

import androidx.room.*
import com.kochkov.evgeny.setlist_mobile.data.AppDataBase
import com.kochkov.evgeny.setlist_mobile.data.Artist
import com.kochkov.evgeny.setlist_mobile.data.SearchQuery
import com.kochkov.evgeny.setlist_mobile.data.entity.Setlist
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow


@Dao
interface ArtistDao {
    @Query("SELECT * FROM ${AppDataBase.SAVED_ARTISTS_TABLE_NAME}")
    suspend fun getAllArtists() : List<Artist>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtist(artist: Artist)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtists(artists: List<Artist>)

    @Delete
    suspend fun deleteArtist(artist: Artist)

    @Query("DELETE FROM ${AppDataBase.CASHED_SETLISTS_TABLE_NAME}")
    suspend fun clearSetlists()

    @Update
    suspend fun updateArtist(artist: Artist)

    @Query("SELECT * FROM ${AppDataBase.SAVED_SEARCH_QUERRY_TABLE_NAME}")
    fun getSearchQueryArtists() : Flow<List<SearchQuery>>
    //переделать на корутину

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchQuery(searchQuery: SearchQuery)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetlists(setlists: List<Setlist>)

    //код на rxJava для получения сетлистов
    //@Query("SELECT * FROM ${AppDataBase.CASHED_SETLISTS_TABLE_NAME}")
    //fun getSetlists(): Observable<List<Setlist>>
}