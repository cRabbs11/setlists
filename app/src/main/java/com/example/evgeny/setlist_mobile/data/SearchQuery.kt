package com.example.evgeny.setlist_mobile.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = AppDataBase.SAVED_SEARCH_QUERRY_TABLE_NAME, indices = [Index(value = ["queryText"], unique = true)])
class SearchQuery(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "queryText") val queryText: String,
    @ColumnInfo(name = "searchType") val searchType: Int
) {
}