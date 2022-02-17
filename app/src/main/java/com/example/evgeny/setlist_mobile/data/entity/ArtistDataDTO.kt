package com.example.evgeny.setlist_mobile.data.entity

data class ArtistDataDTO(
        val artist: List<ArtistDTO>,
        val itemsPerPage: Int,
        val page: Int,
        val total: Int
)