package com.kochkov.evgeny.setlist_mobile.utils

import com.kochkov.evgeny.setlist_mobile.data.entity.Setlist
import com.kochkov.evgeny.setlist_mobile.setlists.SongListItem

object SetlistHelper {

    fun fromSetlistToSongList(setlist: Setlist): List<SongListItem> {
        val newSongList = arrayListOf<SongListItem>()
        setlist.sets!!.forEach { set ->
            if (set.name!="") {
                newSongList.add(set)
            } else if (set.encore>0) {
                newSongList.add(set)
            }
            set.songs.forEach { song ->
                newSongList.add(song)
            }
        }
        return newSongList
    }
}