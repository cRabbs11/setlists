package com.kochkov.evgeny.setlist_mobile.data.entity

import com.kochkov.evgeny.setlist_mobile.data.Artist
import com.kochkov.evgeny.setlist_mobile.setlists.BaseModel
import com.kochkov.evgeny.setlist_mobile.setlists.SongListItem

data class Song(val name: String, val info: String?, val tape: Boolean, val cover: Artist?, val with: Artist?, var number: Int) : BaseModel(),
    SongListItem {

    override fun toString(): String {
        return "Song(name='$name', info='$info', tape=$tape, cover=$cover, with=$with, number=$number)"
    }
}