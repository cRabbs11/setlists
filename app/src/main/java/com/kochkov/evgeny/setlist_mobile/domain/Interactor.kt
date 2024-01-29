package com.kochkov.evgeny.setlist_mobile.domain

import com.kochkov.evgeny.setlist_mobile.data.Artist
import com.kochkov.evgeny.setlist_mobile.data.SetlistsRepository

class Interactor(private val repository: SetlistsRepository) {

    fun setNewArtist() {
        repository.setNewArtist()
    }

    suspend fun searchArtistWithSetlists(artistName: String) = repository.searchArtistWithSetlists(artistName)

    suspend fun isHaveSetlists(artist: Artist) = repository.isSetlistsHave(artist)

    suspend fun getSetlists(artist: Artist, page: Int) = repository.getSetlists(artist, page)
}