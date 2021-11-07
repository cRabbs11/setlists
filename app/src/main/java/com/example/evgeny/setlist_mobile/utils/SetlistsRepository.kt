package com.example.evgeny.setlist_mobile.utils

import com.example.evgeny.setlist_mobile.setlists.Artist
import com.example.evgeny.setlist_mobile.setlists.Setlist
import com.example.evgeny.setlist_mobile.setlists.SetlistsAPI

object SetlistsRepository {

    val TAG = SetlistsRepository::class.java.name + " BMTH "

    private val lastSearchArtists = ArrayList<Artist>()
    private var setlists = ArrayList<Setlist>()
    private lateinit var currentArtist : Artist
    private lateinit var currentSetlist: Setlist
    private var setlistPage = 2

    fun newSearchArtists(artistName: String) : List<Artist> {
        val setlistsAPI = SetlistsAPI()
        val artists = setlistsAPI.getArtists(artistName)
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

    fun searchSetlists(artistMbid: String, page: Int = 1) : List<Setlist> {
        val setlistsAPI = SetlistsAPI()
        return setlistsAPI.getSetlists(artistMbid, page)
    }

    fun setNewSetlists(setlists: ArrayList<Setlist>) {
        this.setlists = setlists
    }

    fun addToSetlists(setlists: ArrayList<Setlist>) {
        this.setlists.addAll(setlists)
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

    fun getSetlistPage(): Int {
        return setlistPage
    }

    fun increaseSetlistPage() {
        setlistPage++
    }
}