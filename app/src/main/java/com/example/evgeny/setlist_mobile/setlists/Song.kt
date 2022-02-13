package com.example.evgeny.setlist_mobile.setlists

import com.example.evgeny.setlist_mobile.data.Artist

data class Song(val name: String, val info: String, val tape: Boolean, val cover: Artist?, val with: Artist?, var number: Int) : BaseModel(), SongListItem {

    override fun toString(): String {
        return "Song(name='$name', info='$info', tape=$tape, cover=$cover, with=$with, number=$number)"
    }
}