package com.example.evgeny.setlist_mobile.data.entity

import com.google.gson.annotations.SerializedName

data class TourDTO(
        @SerializedName("name")
        val name: String?
)

fun TourDTO.toTour() = Tour(name = name ?: "")