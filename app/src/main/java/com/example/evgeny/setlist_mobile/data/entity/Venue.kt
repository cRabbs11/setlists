package com.example.evgeny.setlist_mobile.data.entity

import androidx.room.Embedded
import com.example.evgeny.setlist_mobile.setlists.BaseModel
import java.io.Serializable

data class Venue(
    val id: String,
    val name: String,
    val url: String,
    @Embedded(prefix = "city_") val city: City?) : BaseModel(), Serializable {

}