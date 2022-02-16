package com.example.evgeny.setlist_mobile.data.entity

data class SetlistsDataDTO(
        val itemsPerPage: Int,
        val page: Int,
        val setlist: List<SetlistDTO>,
        val total: Int
)