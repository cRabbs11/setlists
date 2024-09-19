package com.kochkov.evgeny.setlist_mobile.data.dto

import com.kochkov.evgeny.setlist_mobile.data.entity.Setlist

data class SetlistsDataDTO(
    val itemsPerPage: Int,
    val page: Int,
    val setlist: List<SetlistDTO>,
    val total: Int
)