package com.kochkov.evgeny.setlist_mobile.utils

import com.kochkov.evgeny.setlist_mobile.data.dto.SetlistDTO
import com.kochkov.evgeny.setlist_mobile.data.dto.SetlistsDataDTO
import com.kochkov.evgeny.setlist_mobile.data.dto.toArtist
import com.kochkov.evgeny.setlist_mobile.data.dto.toSet
import com.kochkov.evgeny.setlist_mobile.data.dto.toTour
import com.kochkov.evgeny.setlist_mobile.data.dto.toVenue
import com.kochkov.evgeny.setlist_mobile.data.entity.Set
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

    fun fromSetlistDataDTOtoSetlists(setlistsDataDTO: SetlistsDataDTO): List<Setlist> {
        val list = arrayListOf<Setlist>()
        setlistsDataDTO.setlist.forEach {setlistDTO ->
            list.add(fromSetlistDTOtoSetlist(setlistDTO))
        }
        return list
    }

    fun fromSetlistDTOtoSetlist(setlistDTO: SetlistDTO): Setlist {
        val list = arrayListOf<Set>()
        setlistDTO.sets.set.forEach {
            list.add(it.toSet())
        }
        return Setlist(
            id = setlistDTO.id.hashCode(),
            artist = setlistDTO.artist.toArtist(),
            venue = setlistDTO.venue.toVenue(),
            tour = setlistDTO.tour?.toTour(),
            eventDate = setlistDTO.eventDate,
            lastUpdated = setlistDTO.lastUpdated,
            sets = list
        )
    }


}