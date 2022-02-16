package com.example.evgeny.setlist_mobile.data.entity

import com.google.gson.annotations.SerializedName

data class CountryDTO(
        @SerializedName("code")
        val code: String,
        @SerializedName("name")
        val name: String
)