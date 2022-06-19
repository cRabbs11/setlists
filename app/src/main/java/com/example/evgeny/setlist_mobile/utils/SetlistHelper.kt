package com.example.evgeny.setlist_mobile.utils

import com.example.evgeny.setlist_mobile.data.entity.Setlist
import com.example.evgeny.setlist_mobile.setlists.SongListItem

object SetlistHelper {

    fun fromSetlistToSongList(setlist: Setlist): List<SongListItem> {
        val newSongList = arrayListOf<SongListItem>()
        setlist.sets!!.forEach {
            if (it.name!="") {
                newSongList.add(it)
            } else if (it.encore>0) {
                newSongList.add(it)
            }
            it.songs.forEach {
                newSongList.add(it)
            }
        }
        return newSongList
    }
}