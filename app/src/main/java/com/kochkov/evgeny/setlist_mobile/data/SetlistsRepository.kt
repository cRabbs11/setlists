package com.kochkov.evgeny.setlist_mobile.data

import com.kochkov.evgeny.setlist_mobile.data.dao.ArtistDao
import com.kochkov.evgeny.setlist_mobile.data.entity.ArtistDataDTO
import com.kochkov.evgeny.setlist_mobile.data.entity.Setlist
import com.kochkov.evgeny.setlist_mobile.data.entity.toArtistList
import com.kochkov.evgeny.setlist_mobile.data.entity.toSetlistList
import com.kochkov.evgeny.setlist_mobile.utils.SetlistsAPIConstants
import com.kochkov.evgeny.setlist_mobile.utils.SetlistsRetrofitInterface
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response
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

    fun searchArtist(artistName: String): Observable<List<Artist>> {
        return retrofit.searchArtistsObservable(
            artistName = artistName,
            page = 1,
            sort = SetlistsAPIConstants.SORT_TYPE_NAME)
            .subscribeOn(Schedulers.io())
            .onErrorComplete {

                false
            }
            .map {
                val list = it.toArtistList()
                if (list.isNotEmpty()) {
                    setLastSearchArtists(list)
                    val searchQuery = SearchQuery(queryText = artistName, searchType = AppDataBase.SEARCH_TYPE_ARTISTS)
                    saveSearchQueryArtists(searchQuery)
                }
                list
            }
    }

    suspend fun searchArtistCoroutines(artistName: String): Response<ArtistDataDTO> {
        return retrofit.searchArtistsCoroutines(
            artistName = artistName,
            page = 1,
            sort = SetlistsAPIConstants.SORT_TYPE_NAME)
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