package com.example.evgeny.setlist_mobile.data

import com.example.evgeny.setlist_mobile.data.dao.ArtistDao
import com.example.evgeny.setlist_mobile.data.entity.Setlist
import com.example.evgeny.setlist_mobile.data.entity.toArtistList
import com.example.evgeny.setlist_mobile.data.entity.toSetlistList
import com.example.evgeny.setlist_mobile.utils.SetlistsAPIConstants
import com.example.evgeny.setlist_mobile.utils.SetlistsRetrofitInterface
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class SetlistsRepository(private val artistDao: ArtistDao, private val retrofit: SetlistsRetrofitInterface) {

    private var setlistPage = 1

    fun setNewArtist() {
        setlistPage = 1
    }

    fun getSetlistPage(): Int {
        return setlistPage
    }

    fun increaseSetlistPage() {
        setlistPage++
    }

    fun getSearchQueryArtists() : Observable<List<SearchQuery>> {
        return artistDao.getSearchQueryArtists()
    }

    fun saveSearchQueryArtists(query: SearchQuery): Single<Long> {
        return artistDao.insertSearchQuery(query)
    }

    fun searchArtist(artistName: String): Observable<List<Artist>> {
        return retrofit.searchArtists(
            artistName = artistName,
            page = 1,
            sort = SetlistsAPIConstants.SORT_TYPE_NAME)
            .subscribeOn(Schedulers.io())
            .onErrorComplete {
                false
            }
            .observeOn(Schedulers.computation())
            .map {
                val list = it.toArtistList()
                list
            }
    }

    fun isSetlistsHave(artist: Artist): Observable<Boolean> {
        return retrofit.getSetlistsByArtistObservable(
            artistMbid = artist.mbid,
            page = 1
        ).subscribeOn(Schedulers.io())
            .onErrorComplete{
                false
            }
            .observeOn(Schedulers.computation())
            .map {
                val list = it.toSetlistList()
                list.isNotEmpty()
            }

    }

    fun getSetlistsWithDB(artist: Artist): Observable<List<Setlist>> {
        return Observable.concat(
            retrofit.getSetlistsByArtistObservable(
                artistMbid = artist.mbid,
                page = getSetlistPage()
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
                        increaseSetlistPage()
                    }
                    Observable.empty<List<Setlist>>()
                },
            artistDao.getSetlists().subscribeOn(Schedulers.io())
        )
    }

    fun addArtistInwathed(artist: Artist): Single<Long> {
        return artistDao.insertWatchedArtist(artist)
    }

    fun getWatchedArtists(): Observable<List<Artist>> {
        return artistDao.getWatchedArtists()
    }

    fun getSetlists(artist: Artist): Observable<List<Setlist>> {
        return retrofit.getSetlistsByArtistObservable(
            artistMbid = artist.mbid,
            page = getSetlistPage()
        )
            .map {
                val list = it.toSetlistList()
                if (list.isNotEmpty()) {
                    increaseSetlistPage()
                }
                list
            }
    }

    private fun insertSetlistsInDB(list: List<Setlist>) {
        artistDao.insertSetlists(list)
    }
}