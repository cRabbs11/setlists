package com.kochkov.evgeny.setlist_mobile.data

import com.kochkov.evgeny.setlist_mobile.data.dao.ArtistDao
import com.kochkov.evgeny.setlist_mobile.domain.repository.LocalRepository

class RoomRepository(private val artistDao: ArtistDao): LocalRepository {
    override suspend fun saveSearchQuery(query: SearchQuery) = artistDao.insertSearchQuery(query)

    override suspend fun getSearchQueryArtists() = artistDao.getSearchQueryArtists()

    override suspend fun setNewArtist() {
        clearSetlistsInDB()
    }
    private suspend fun clearSetlistsInDB() {
        artistDao.clearSetlists()
    }
}