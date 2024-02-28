package com.kochkov.evgeny.setlist_mobile.domain

import com.kochkov.evgeny.setlist_mobile.data.Artist
import com.kochkov.evgeny.setlist_mobile.data.SetlistsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class Interactor(private val repository: SetlistsRepository) {

    fun setNewArtist() {
        MainScope().launch(Dispatchers.IO) {
            repository.setNewArtist()
        }
    }

    suspend fun searchArtistWithSetlists(artistName: String) = repository.searchArtistWithSetlists(artistName)

    suspend fun isHaveSetlists(artist: Artist) = repository.isSetlistsHave(artist)

    suspend fun getSetlists(artist: Artist, page: Int) = repository.getSetlists(artist, page)

    suspend fun getSetlistsInTour(tourName: String) = repository.getSetlistsInTour(tourName)

    fun setlistPagingSource(artist: String) = repository.setlistPagingSource(artist)
}