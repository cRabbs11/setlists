package com.kochkov.evgeny.setlist_mobile.data.dto

import com.google.gson.annotations.SerializedName
import com.kochkov.evgeny.setlist_mobile.data.entity.Country

data class CountryDTO(
        @SerializedName("code")
        val code: String,
        @SerializedName("name")
        val name: String
)

fun CountryDTO.toCountry() = Country(
        code = code,
        name = name
)