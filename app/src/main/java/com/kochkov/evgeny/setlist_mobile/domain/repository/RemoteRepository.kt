package com.kochkov.evgeny.setlist_mobile.domain.repository

import com.kochkov.evgeny.setlist_mobile.data.Artist
import com.kochkov.evgeny.setlist_mobile.data.SetlistPagingSource
import com.kochkov.evgeny.setlist_mobile.data.entity.Setlist

interface RemoteRepository {
    //старые методы
    suspend fun setNewArtist()

    suspend fun searchArtistWithSetlists(artistName: String): List<Artist>?

    suspend fun isSetlistsHave(artist: Artist): Boolean

    suspend fun getSetlists(artist: Artist, page: Int): List<Setlist>?

    suspend fun getSetlistsInTour(tourName: String): List<Setlist>?

    fun setlistPagingSource(artist : String) : SetlistPagingSource
}