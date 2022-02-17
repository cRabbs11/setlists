package com.example.evgeny.setlist_mobile.utils

import com.example.evgeny.setlist_mobile.data.Artist
import com.example.evgeny.setlist_mobile.setlists.Setlist
import com.example.evgeny.setlist_mobile.setlists.SongListItem

object SetlistsRepository {

    val TAG = SetlistsRepository::class.java.name + " BMTH "

    private val lastSearchArtists = ArrayList<Artist>()
    private var setlists = ArrayList<Setlist>()
    private lateinit var currentArtist : Artist
    private lateinit var currentSetlist: Setlist
    private var songlist =  ArrayList<SongListItem>()
    private var setlistPage = 2

    fun setCurrentArtist(artist: Artist) {
        currentArtist = artist
    }

    fun getCurrentArtist() :  Artist? {
        return currentArtist
    }

    fun setNewSetlists(setlists: List<Setlist>) {
        this.setlists.clear()
        addToSetlists(setlists)
    }

    fun addToSetlists(setlists: List<Setlist>) {
        this.setlists.addAll(setlists)
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
}