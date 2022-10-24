package com.kochkov.evgeny.setlist_mobile.data.entity

import com.google.gson.annotations.SerializedName

data class CoordsDTO(
        @SerializedName("lat")
        val lat: Double,
        @SerializedName("long")
        val long: Double
)

fun CoordsDTO.toCoords() = Coords(
        coord_lat = lat.toString(),
        coord_long = long.toString()
)