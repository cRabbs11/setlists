package com.kochkov.evgeny.setlist_mobile.data.dto
data class SetlistsDataDTO(
    val itemsPerPage: Int,
    val page: Int,
    val setlist: List<SetlistDTO>,
    val total: Int
)