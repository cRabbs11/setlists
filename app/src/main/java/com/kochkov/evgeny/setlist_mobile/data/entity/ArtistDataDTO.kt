package com.kochkov.evgeny.setlist_mobile.data.entity

import com.kochkov.evgeny.setlist_mobile.data.Artist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class ArtistDataDTO(
        val artist: List<ArtistDTO>,
        val itemsPerPage: Int,
        val page: Int,
        val total: Int
)

fun ArtistDataDTO.toArtistList(): List<Artist> {
        val list = arrayListOf<Artist>()
        artist.forEach { artistDTO ->
                list.add(artistDTO.toArtist())
        }
        return list
}

fun ArtistDataDTO.toArtistListFlow(): Flow<List<Artist>?> {
        return flow {
                emit(toArtistList())
        }
}

