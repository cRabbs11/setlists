package com.kochkov.evgeny.setlist_mobile.domain.repository

import com.kochkov.evgeny.setlist_mobile.data.Artist
import com.kochkov.evgeny.setlist_mobile.data.SetlistPagingSource
import com.kochkov.evgeny.setlist_mobile.data.dto.SetlistsDataDTO
import com.kochkov.evgeny.setlist_mobile.data.entity.Setlist
import retrofit2.Response

interface RemoteRepository {

    suspend fun searchArtists(artistName: String): List<Artist>

    suspend fun getSetlists(artist: Artist, page: Int): List<Setlist>

    suspend fun getAllSetlistsInTour(tourName: String): List<Setlist>

    fun setlistPagingSource(artist : String) : SetlistPagingSource
}