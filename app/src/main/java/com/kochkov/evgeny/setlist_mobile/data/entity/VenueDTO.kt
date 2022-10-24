package com.kochkov.evgeny.setlist_mobile.data.entity

import com.google.gson.annotations.SerializedName

data class VenueDTO(
        @SerializedName("city")
        val city: CityDTO,
        @SerializedName("id")
        val id: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("url")
        val url: String
)

fun VenueDTO.toVenue() = Venue(
        id = id,
        name = name,
        url =url,
        city = city.toCity()
)