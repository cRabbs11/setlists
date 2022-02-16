package com.example.evgeny.setlist_mobile.data.entity

import com.google.gson.annotations.SerializedName

data class SongDTO(
        @SerializedName("cover")
        val cover: ArtistDTO?,
        @SerializedName("info")
        val info: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("tape")
        val tape: Boolean,
        @SerializedName("with")
        val with: ArtistDTO?
)