package com.example.evgeny.setlist_mobile.utils

import com.example.evgeny.setlist_mobile.data.Artist
import com.example.evgeny.setlist_mobile.data.entity.ArtistDTO

object Converter {
    fun convertArtistDTOListToArtistList(list : List<ArtistDTO>?) : List<Artist> {
        val artistList = arrayListOf<Artist>()
        list?.forEach {
            artistList.add(
                    Artist(disambiguation = it.disambiguation ?: "",
                    mbid = it.mbid,
                    name = it.name,
                    sortName = it.sortName,
                    tmid = it.tmid,
                    url = it.url))
        }
        return artistList
    }
}