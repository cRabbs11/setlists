package com.example.evgeny.setlist_mobile.setlists

import androidx.room.Embedded

data class City(
    val id: String,
    val name: String,
    val state: String,
    val stateCode: String?,
    @Embedded(prefix = "coords_") val coords: Coords?,
    @Embedded(prefix = "country_") val country: Country?) : BaseModel() {
}