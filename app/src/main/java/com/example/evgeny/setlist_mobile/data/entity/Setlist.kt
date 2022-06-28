package com.example.evgeny.setlist_mobile.data.entity

import androidx.room.*
import com.example.evgeny.setlist_mobile.data.AppDataBase
import com.example.evgeny.setlist_mobile.data.Artist
import com.example.evgeny.setlist_mobile.setlists.BaseModel
import java.io.Serializable

@Entity(tableName = AppDataBase.CASHED_SETLISTS_TABLE_NAME)
data class Setlist(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @Embedded(prefix = "artist_") var artist: Artist?,
    @Embedded(prefix = "venue_") var venue: Venue?,
    @Embedded(prefix = "tour_") var tour: Tour?,
    @ColumnInfo(name = "eventDate") var eventDate: String,
    @ColumnInfo(name = "lastUpdated") var lastUpdated: String,
    var sets: List<Set>?) : BaseModel(), Serializable {
        constructor(): this(
            artist = null,
            venue = null,
            tour = null,
            eventDate = "null",
            lastUpdated = "null",
            sets = null)
}