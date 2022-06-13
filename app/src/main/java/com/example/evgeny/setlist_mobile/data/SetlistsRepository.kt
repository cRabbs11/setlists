package com.example.evgeny.setlist_mobile.data

import com.example.evgeny.setlist_mobile.data.dao.ArtistDao
import com.example.evgeny.setlist_mobile.data.entity.Setlist
import com.example.evgeny.setlist_mobile.setlists.SongListItem
import io.reactivex.rxjava3.subjects.BehaviorSubject

class SetlistsRepository(val artistDao: ArtistDao) {

    val TAG = SetlistsRepository::class.java.name + " BMTH "

    private val lastSearchArtists = ArrayList<Artist>()
    private var setlists = ArrayList<Setlist>()
    private lateinit var selectedArtist : Artist
    private lateinit var currentSetlist: Setlist
    private var songlist =  ArrayList<SongListItem>()
    private var setlistPage = 1

    val setlistSubject = BehaviorSubject.create<List<Setlist>>()

    fun setSelectedArtist(artist: Artist) {
        selectedArtist = artist
        this.setlists.clear()
        setlistPage = 1
    }

    fun getSelectedArtist() :  Artist? {
        return selectedArtist
    }

    fun addToSetlists(setlists: List<Setlist>) {
        this.setlists.addAll(setlists)
        increaseSetlistPage()
        setlistSubject.onNext(this.setlists)
    }

    fun setLastSearchArtists(list: List<Artist>) {
        lastSearchArtists.clear()
        list.forEach {
            lastSearchArtists.add(it)
        }
    }

    fun getSetlists(): ArrayList<Setlist> {
        return setlists
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
        setlist.sets.forEach {
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
}