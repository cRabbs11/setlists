package com.kochkov.evgeny.setlist_mobile.data

import com.kochkov.evgeny.setlist_mobile.data.dao.ArtistDao
import com.kochkov.evgeny.setlist_mobile.data.entity.*
import com.kochkov.evgeny.setlist_mobile.utils.SetlistsAPIConstants
import com.kochkov.evgeny.setlist_mobile.utils.SetlistsAPIConstants.SETLISTS_IN_TOUR_IS_NULL
import com.kochkov.evgeny.setlist_mobile.utils.SetlistsRetrofitInterface
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.*
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


    suspend fun searchArtistWithSetlists(artistName: String): List<Artist>? {
        return coroutineScope {
            val rearchResult = retrofit.searchArtists(
                artistName = artistName,
                page = 1,
                sort = SetlistsAPIConstants.SORT_TYPE_NAME)
            val list = rearchResult.body()?.toArtistList()
            val result = arrayListOf<Artist>()
            val deferred = list?.flatMap {
                listOf(
                    async {
                        isSetlistsHaveReturnedArtist(it)
                    }
                )
            }
            deferred?.awaitAll()?.forEach { artist ->
                artist?.let {
                    result.add(it)
                }
            }
            if (!result.isNullOrEmpty()) {
                val searchQuery = SearchQuery(queryText = artistName, searchType = AppDataBase.SEARCH_TYPE_ARTISTS)
                saveSearchQueryArtists(searchQuery)
            }
            result
        }
    }

    suspend fun searchArtistAndSaveQuery(artistName: String): List<Artist>? {
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

    //код на rxJava для получения сетлистов и с БД и с сети
    //fun getSetlistsWithDB(artist: Artist, page: Int): Observable<List<Setlist>> {
    //    return Observable.concat(
    //        retrofit.getSetlistsByArtistObservable(
    //            artistMbid = artist.mbid,
    //            page = page
    //        ).subscribeOn(Schedulers.io())
    //            .onErrorComplete{
    //                false
    //            }
    //            .map {
    //                val list = it.toSetlistList()
    //                list
    //            }
    //            .flatMap {
    //                if (it.isNotEmpty()) {
    //                    insertSetlistsInDB(it)
    //                }
    //                Observable.empty<List<Setlist>>()
    //            },
    //        artistDao.getSetlists().subscribeOn(Schedulers.io())
    //    )
    //}

    suspend fun getSetlists(artist: Artist, page: Int): List<Setlist>? {
        return coroutineScope {
            val result = retrofit.getSetlistsByArtist(
                artistMbid = artist.mbid,
                page = page)
            result.body()?.toSetlistList()
        }
    }

    suspend fun getSetlistsInTour(tourName: String): List<Setlist>? {
        return coroutineScope {
            val setlistsInTour = arrayListOf<Setlist>()
            var isTourEnded = false
            var page = 0
            var setlistsInTourTotal = SETLISTS_IN_TOUR_IS_NULL
            var setlistsInTourCount = 0
            while (!isTourEnded) {
                val response = retrofit.searchSetlistsByTour(tourName, ++page)
                setlistsInTourTotal = response.body()?.total?: SETLISTS_IN_TOUR_IS_NULL
                val result = response.body()?.toSetlistList()
                    result?.forEach { setlist ->
                        setlistsInTour.add(setlist)
                        setlistsInTourCount++
                    }
                if (setlistsInTourCount>=setlistsInTourTotal) isTourEnded = true
                    //убрать проверку на совпадение имени тура (это происходит внутри апи?)
            }
            setlistsInTour
        }
    }

    suspend fun isSetlistsHave(artist: Artist): Boolean {
        return coroutineScope {
            val result = retrofit.getSetlistsByArtist(artist.mbid, 1)
            result.body()?.toSetlistList()?.isNotEmpty()?: false
        }
    }

    private suspend fun isSetlistsHaveReturnedArtist(artist: Artist): Artist? {
        return coroutineScope {
            val result = retrofit.getSetlistsByArtist(artist.mbid, 1)
            result.body()?.toSetlistList()?.isNotEmpty()?.let {
                if (it) {
                    artist
                } else {
                    null
                }
            }
        }
    }
}