package com.example.evgeny.setlist_mobile.data.entity

import com.example.evgeny.setlist_mobile.data.Artist

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

