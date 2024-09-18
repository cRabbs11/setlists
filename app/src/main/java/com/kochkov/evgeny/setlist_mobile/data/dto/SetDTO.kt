package com.kochkov.evgeny.setlist_mobile.data.dto

import com.google.gson.annotations.SerializedName
import com.kochkov.evgeny.setlist_mobile.data.entity.Set
import com.kochkov.evgeny.setlist_mobile.data.entity.Song

data class SetDTO(
        @SerializedName("encore")
        val encore: Int,
        @SerializedName("name")
        val name: String?,
        @SerializedName("song")
        val song: List<SongDTO>
)

fun SetDTO.toSet(): Set {
    var songNumber = 1
    val list = arrayListOf<Song>()
    song.forEach {
        val number = if (!it.tape) {
            songNumber++
        } else {
            songNumber
        }
        list.add(it.toSong(number))
    }
    return Set(
        name = name?: "",
        encore = encore,
        songs = list)
}
