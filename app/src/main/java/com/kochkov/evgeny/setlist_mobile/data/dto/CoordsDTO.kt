package com.kochkov.evgeny.setlist_mobile.data.dto

import com.google.gson.annotations.SerializedName

data class CoordsDTO(
        @SerializedName("lat")
        val lat: Double,
        @SerializedName("long")
        val long: Double
)
