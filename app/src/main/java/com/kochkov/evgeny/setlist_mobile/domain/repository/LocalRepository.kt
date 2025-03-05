package com.kochkov.evgeny.setlist_mobile.domain.repository

import com.kochkov.evgeny.setlist_mobile.data.SearchQuery
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun saveSearchQuery(query: SearchQuery)

    suspend fun getSearchQueryArtists(): Flow<List<SearchQuery>>

    suspend fun setNewArtist()
}