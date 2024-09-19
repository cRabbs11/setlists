package com.kochkov.evgeny.setlist_mobile.data.entity

import androidx.room.*
import com.kochkov.evgeny.setlist_mobile.data.AppDataBase
import com.kochkov.evgeny.setlist_mobile.data.Artist
import com.kochkov.evgeny.setlist_mobile.setlists.BaseModel
import com.kochkov.evgeny.setlist_mobile.setlists.SongListItem

@Entity(tableName = AppDataBase.CASHED_SETLISTS_TABLE_NAME)
data class Setlist(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @Embedded(prefix = "artist_") var artist: Artist?,
    @Embedded(prefix = "venue_") var venue: Venue?,
    @Embedded(prefix = "tour_") var tour: Tour?,
    @ColumnInfo(name = "eventDate") var eventDate: String,
    @ColumnInfo(name = "lastUpdated") var lastUpdated: String,
    var sets: List<Set> = listOf()
) : BaseModel() {

    fun getComponentsList(): List<SongListItem> {
        val newSongList = arrayListOf<SongListItem>()
        sets.forEach {set ->
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