package com.kochkov.evgeny.setlist_mobile.data.entity

import com.google.gson.annotations.SerializedName

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