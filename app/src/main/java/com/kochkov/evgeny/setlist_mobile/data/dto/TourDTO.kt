package com.kochkov.evgeny.setlist_mobile.data.dto

import com.google.gson.annotations.SerializedName
import com.kochkov.evgeny.setlist_mobile.data.entity.Tour

data class TourDTO(
        @SerializedName("name")
        val name: String?
)

fun TourDTO.toTour() = Tour(name = name ?: "")