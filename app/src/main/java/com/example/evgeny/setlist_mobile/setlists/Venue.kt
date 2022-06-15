package com.example.evgeny.setlist_mobile.setlists

import androidx.room.Embedded

data class Venue(
    val id: String,
    val name: String,
    val url: String,
    @Embedded(prefix = "city_") val city: City?) : BaseModel() {

}