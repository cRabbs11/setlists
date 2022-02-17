package com.example.evgeny.setlist_mobile.data.entity

import com.google.gson.annotations.SerializedName

data class CoordsDTO(
        @SerializedName("lat")
        val lat: Double,
        @SerializedName("long")
        val long: Double
)