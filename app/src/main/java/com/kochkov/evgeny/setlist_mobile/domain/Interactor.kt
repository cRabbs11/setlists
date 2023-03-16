package com.kochkov.evgeny.setlist_mobile.domain

import com.kochkov.evgeny.setlist_mobile.data.Artist
import com.kochkov.evgeny.setlist_mobile.data.SetlistsRepository
import com.kochkov.evgeny.setlist_mobile.data.entity.Setlist
import io.reactivex.rxjava3.core.Observable

class Interactor(private val repository: SetlistsRepository) {

    fun setNewArtist() {
        repository.setNewArtist()
    }

    suspend fun searchArtist(artistName: String) = repository.searchArtist(artistName)

    suspend fun isHaveSetlists(artist: Artist) = repository.isSetlistsHave(artist)

    suspend fun getSetlists(artist: Artist, page: Int) = repository.getSetlists(artist, page)
}