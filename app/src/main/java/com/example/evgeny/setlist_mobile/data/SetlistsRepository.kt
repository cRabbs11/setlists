package com.example.evgeny.setlist_mobile.data

import com.example.evgeny.setlist_mobile.data.dao.ArtistDao
import com.example.evgeny.setlist_mobile.data.entity.Setlist
import com.example.evgeny.setlist_mobile.setlists.SongListItem
import com.example.evgeny.setlist_mobile.utils.Converter
import com.example.evgeny.setlist_mobile.utils.SetlistsAPIConstants
import com.example.evgeny.setlist_mobile.utils.SetlistsRetrofitInterface
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.Executors

class SetlistsRepository(private val artistDao: ArtistDao, private val retrofit: SetlistsRetrofitInterface) {

    val TAG = SetlistsRepository::class.java.name + " BMTH "

    private val lastSearchArtists = ArrayList<Artist>()
    private lateinit var currentSetlist: Setlist
    private var songlist =  ArrayList<SongListItem>()
    private var setlistPage = 1

    fun setNewArtist() {
        Executors.newSingleThreadExecutor().execute {
            clearSetlistsInDB()
            setlistPage = 1
        }
    }

    fun setLastSearchArtists(list: List<Artist>) {
        lastSearchArtists.clear()
        list.forEach {
            lastSearchArtists.add(it)
        }
    }

    fun setCurrentSetlist(setlist: Setlist) {
        this.currentSetlist = setlist
    }

    fun getCurrentSetlist(): Setlist? {
        return currentSetlist
    }

    fun getSetlistPage(): Int {
        return setlistPage
    }

    fun increaseSetlistPage() {
        setlistPage++
    }

    fun setSongList(setlist: Setlist) {
        val newSongList = arrayListOf<SongListItem>()
        setlist.sets!!.forEach {
            if (it.name!="") {
                newSongList.add(it)
            } else if (it.encore>0) {
                newSongList.add(it)
            }
            it.songs.forEach {
                newSongList.add(it)
            }
        }
        this.songlist = newSongList
    }

    fun getSongList(): ArrayList<SongListItem> {
        return songlist
    }

    fun getSearchQueryArtists() : List<String> {
        val result = arrayListOf<String>()
        val list = artistDao.getSearchQueryArtists()
        list.forEach {
            result.add(it.queryText)
        }
        return result
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
                val list = Converter.convertArtistDTOListToArtistList(it.artist)
                if (list.isNotEmpty()) {
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
                val list = Converter.convertSetlistDTOListToSetlistList(it.setlist)
                if (list.isNotEmpty()) {
                    Observable.just(true)
                } else {
                    Observable.just(false)
                }
            }
    }

    fun getSetlists(artist: Artist): Observable<List<Setlist>> {
        return Observable.concat(
            retrofit.getSetlistsByArtistObservable(
                artistMbid = artist.mbid,
                page = getSetlistPage()
            ).subscribeOn(Schedulers.io())
                .onErrorComplete{
                    false
                }
                .map {
                    Converter.convertSetlistDTOListToSetlistList(it.setlist)
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
}