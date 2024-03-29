package com.kochkov.evgeny.setlist_mobile.data.entity

import com.kochkov.evgeny.setlist_mobile.data.Artist
import com.google.gson.annotations.SerializedName

data class ArtistDTO(
        @SerializedName("disambiguation")
        val disambiguation: String?,
        @SerializedName("mbid")
        val mbid: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("sortName")
        val sortName: String,
        @SerializedName("tmid")
        val tmid: Int,
        @SerializedName("url")
        val url: String
)

fun ArtistDTO.toArtist() = Artist(
        disambiguation = disambiguation ?: "",
        mbid = mbid,
        name = name,
        sortName = sortName,
        tmid = tmid,
        url = url
)