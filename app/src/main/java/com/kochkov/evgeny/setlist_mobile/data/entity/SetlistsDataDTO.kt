package com.kochkov.evgeny.setlist_mobile.data.entity

data class SetlistsDataDTO(
        val itemsPerPage: Int,
        val page: Int,
        val setlist: List<SetlistDTO>,
        val total: Int
)

fun SetlistsDataDTO.toSetlistList(): List<Setlist> {
        val list = arrayListOf<Setlist>()
        setlist.forEach {
                list.add(it.toSetlist())
        }
        return list
}