package com.kochkov.evgeny.setlist_mobile.data.dto

import com.google.gson.annotations.SerializedName

data class SetDTO(
        @SerializedName("encore")
        val encore: Int,
        @SerializedName("name")
        val name: String?,
        @SerializedName("song")
        val song: List<SongDTO>
)
