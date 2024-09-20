package com.kochkov.evgeny.setlist_mobile.data.dto

data class ArtistDataDTO(
    val artist: List<ArtistDTO>,
    val itemsPerPage: Int,
    val page: Int,
    val total: Int
)

