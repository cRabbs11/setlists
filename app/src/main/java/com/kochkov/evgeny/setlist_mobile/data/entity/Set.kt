package com.kochkov.evgeny.setlist_mobile.data.entity

import com.kochkov.evgeny.setlist_mobile.setlists.BaseModel
import com.kochkov.evgeny.setlist_mobile.setlists.SongListItem

data class Set(val name: String, val encore: Int, val songs: List<Song> = arrayListOf()) : BaseModel(),
    SongListItem {

    override fun toString(): String {
        return "Set(name='$name', encore='$encore', songs=$songs)"
    }
}