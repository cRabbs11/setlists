package com.kochkov.evgeny.setlist_mobile.data.entity

import androidx.room.Embedded
import com.kochkov.evgeny.setlist_mobile.setlists.BaseModel

data class City(
    val id: String,
    val name: String,
    val state: String,
    val stateCode: String?,
    @Embedded(prefix = "coords_") val coords: Coords?,
    @Embedded(prefix = "country_") val country: Country?) : BaseModel() {
}

