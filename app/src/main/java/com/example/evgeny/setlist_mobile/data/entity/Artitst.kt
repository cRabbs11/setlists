package com.example.evgeny.setlist_mobile.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.evgeny.setlist_mobile.setlists.BaseModel
import java.io.Serializable

@Entity(tableName = AppDataBase.SAVED_ARTISTS_TABLE_NAME, indices = [Index(value = ["mbid"], unique = true)])
data class Artist(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        @ColumnInfo(name = "disambiguation") val disambiguation: String,
        @ColumnInfo(name = "mbid") val mbid: String,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "sort_name") val sortName: String,
        @ColumnInfo(name = "tmid") val tmid: Int,
        @ColumnInfo(name = "url") val url: String,
        @ColumnInfo(name = "isWatched") var isWatched: Boolean
) : BaseModel(), Serializable