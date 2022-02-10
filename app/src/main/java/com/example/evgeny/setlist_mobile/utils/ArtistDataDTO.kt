package com.example.evgeny.setlist_mobile.utils

import com.example.evgeny.setlist_mobile.setlists.Artist

data class ArtistDataDTO(
        val artist: List<Artist>,
        val itemsPerPage: Int,
        val page: Int,
        val total: Int
)