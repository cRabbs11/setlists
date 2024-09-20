package com.kochkov.evgeny.setlist_mobile.data.dto

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