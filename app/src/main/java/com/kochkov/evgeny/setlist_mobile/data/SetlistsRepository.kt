package com.kochkov.evgeny.setlist_mobile.data

import android.util.Log
import com.kochkov.evgeny.setlist_mobile.data.dao.ArtistDao
import com.kochkov.evgeny.setlist_mobile.data.entity.*
import com.kochkov.evgeny.setlist_mobile.utils.Constants
import com.kochkov.evgeny.setlist_mobile.utils.SetlistsAPIConstants
import com.kochkov.evgeny.setlist_mobile.utils.SetlistsRetrofitInterface
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.net.UnknownHostException
import java.util.concurrent.Executors

class SetlistsRepository(private val artistDao: ArtistDao, private val retrofit: SetlistsRetrofitInterface) {

    val TAG = SetlistsRepository::class.java.name + " BMTH "

    private val lastSearchArtists = ArrayList<Artist>()

    fun setNewArtist() {
        Executors.newSingleThreadExecutor().execute {
            clearSetlistsInDB()
        }
    }

    fun setLastSearchArtists(list: List<Artist>) {
        lastSearchArtists.clear()
        list.forEach {
            lastSearchArtists.add(it)
        }
    }

    fun getSearchQueryArtists() : Observable<List<SearchQuery>> {
        return artistDao.getSearchQueryArtists()
    }

    fun saveSearchQueryArtists(query: SearchQuery) {
        return artistDao.insertSearchQuery(query)
    }

    suspend fun searchArtist(artistName: String): List<Artist>? {
        return coroutineScope {
            val result = retrofit.searchArtists(
                artistName = artistName,
                page = 1,
                sort = SetlistsAPIConstants.SORT_TYPE_NAME)
            val list = result.body()?.toArtistList()
            if (!list.isNullOrEmpty()) {
                setLastSearchArtists(list)
                val searchQuery = SearchQuery(queryText = artistName, searchType = AppDataBase.SEARCH_TYPE_ARTISTS)
                saveSearchQueryArtists(searchQuery)
            }
            list
        }
    }

    private fun insertSetlistsInDB(list: List<Setlist>) {
        artistDao.insertSetlists(list)
    }

    private fun clearSetlistsInDB() {
        artistDao.clearSetlists()
    }

    fun isSetlistsHave(artist: Artist): Observable<Boolean> {
        return retrofit.getSetlistsByArtistObservable(
            artistMbid = artist.mbid,
            page = 1
        ).subscribeOn(Schedulers.io())
            .onErrorComplete{
                false
            }
            .flatMap {
                val list = it.toSetlistList()
                if (list.isNotEmpty()) {
                    Observable.just(true)
                } else {
                    Observable.just(false)
                }
            }
    }

    fun getSetlistsWithDB(artist: Artist, page: Int): Observable<List<Setlist>> {
        return Observable.concat(
            retrofit.getSetlistsByArtistObservable(
                artistMbid = artist.mbid,
                page = page
            ).subscribeOn(Schedulers.io())
                .onErrorComplete{
                    false
                }
                .map {
                    val list = it.toSetlistList()
                    list
                }
                .flatMap {
                    if (it.isNotEmpty()) {
                        insertSetlistsInDB(it)
                    }
                    Observable.empty<List<Setlist>>()
                },
            artistDao.getSetlists().subscribeOn(Schedulers.io())
        )
    }

    fun getSetlists(artist: Artist, page: Int): Observable<List<Setlist>> {
        return retrofit.getSetlistsByArtistObservable(
            artistMbid = artist.mbid,
            page = page
        )
            .map {
                it.toSetlistList()
            }
    }
}