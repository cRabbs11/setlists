package com.example.evgeny.setlist_mobile.utils

import com.example.evgeny.setlist_mobile.setlists.Artist
import com.example.evgeny.setlist_mobile.setlists.Setlist
import com.example.evgeny.setlist_mobile.setlists.SetlistsAPI

object SetlistsRepository {

    val TAG = SetlistsRepository::class.java.name + " BMTH "

    private val lastSearchArtists = ArrayList<Artist>()
    private val setlists = ArrayList<Setlist>()
    private lateinit var currentArtist : Artist
    private lateinit var currentSetlist: Setlist

    fun newSearchArtists(artistName: String) : List<Artist> {
        val setlistsAPI = SetlistsAPI()
        val artists = setlistsAPI.searchArtists(artistName)
        lastSearchArtists.clear()
        artists.forEach {
            lastSearchArtists.add(it)
        }
        return getLastSearchArtists()
    }

    fun setCurrentArtist(artist: Artist) {
        currentArtist = artist
    }

    fun getCurrentArtist() :  Artist? {
        return currentArtist
    }

    fun searchSetlists(artistMbid: String) : List<Setlist> {
        val setlistsAPI = SetlistsAPI()
        val setlists = setlistsAPI.searchSetlists(artistMbid)
        this.setlists.clear()
        setlists.forEach {
            this.setlists.add(it)
        }
        return getSetlists()
    }

    fun getLastSearchArtists(): ArrayList<Artist> {
        return lastSearchArtists
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
}