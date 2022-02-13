package com.example.evgeny.setlist_mobile.data.entity

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