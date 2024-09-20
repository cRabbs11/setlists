package com.kochkov.evgeny.setlist_mobile.utils

import com.kochkov.evgeny.setlist_mobile.data.Artist
import com.kochkov.evgeny.setlist_mobile.data.dto.ArtistDTO
import com.kochkov.evgeny.setlist_mobile.data.dto.ArtistDataDTO
import com.kochkov.evgeny.setlist_mobile.data.dto.SetDTO
import com.kochkov.evgeny.setlist_mobile.data.dto.SetlistDTO
import com.kochkov.evgeny.setlist_mobile.data.dto.SetlistsDataDTO
import com.kochkov.evgeny.setlist_mobile.data.dto.toArtist
import com.kochkov.evgeny.setlist_mobile.data.dto.toSong
import com.kochkov.evgeny.setlist_mobile.data.dto.toTour
import com.kochkov.evgeny.setlist_mobile.data.dto.toVenue
import com.kochkov.evgeny.setlist_mobile.data.entity.Set
import com.kochkov.evgeny.setlist_mobile.data.entity.Setlist
import com.kochkov.evgeny.setlist_mobile.data.entity.Song
import com.kochkov.evgeny.setlist_mobile.setlists.SongListItem

object SetlistConverter {

    fun fromSetlistToSongList(setlist: Setlist): List<SongListItem> {
        val newSongList = arrayListOf<SongListItem>()
        setlist.sets.forEach { set ->
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

    fun fromArtistDataDTOToArtists(artistDataDTO: ArtistDataDTO): List<Artist> {
        val list = arrayListOf<Artist>()
        artistDataDTO.artist.forEach { artistDTO ->
            list.add(fromArtistDTOToArtist(artistDTO))
        }
        return list
    }

    fun fromArtistDTOToArtist(artistDTO: ArtistDTO): Artist {
        return Artist(
            disambiguation = artistDTO.disambiguation ?: "",
            mbid = artistDTO.mbid,
            name = artistDTO.name,
            sortName = artistDTO.sortName,
            tmid = artistDTO.tmid,
            url = artistDTO.url)
    }

    fun fromSetlistDataDTOtoSetlists(setlistsDataDTO: SetlistsDataDTO): List<Setlist> {
        val list = arrayListOf<Setlist>()
        setlistsDataDTO.setlist.forEach {setlistDTO ->
            list.add(fromSetlistDTOtoSetlist(setlistDTO))
        }
        return list
    }

    private fun fromSetlistDTOtoSetlist(setlistDTO: SetlistDTO): Setlist {
        val list = arrayListOf<Set>()
        setlistDTO.sets.set.forEach {
            list.add(fromSetDTOToSet(it))
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

    private fun fromSetDTOToSet(setDTO: SetDTO): Set {
        var songNumber = 1
        val list = arrayListOf<Song>()
        setDTO.song.forEach {
            val number = if (!it.tape) {
                songNumber++
            } else {
                songNumber
            }
            list.add(it.toSong(number))
        }
        return Set(
            name = setDTO.name?: "",
            encore = setDTO.encore,
            songs = list)
    }
}