package com.kochkov.evgeny.setlist_mobile.data.entity
import com.google.gson.annotations.SerializedName

data class CityDTO(
        @SerializedName("coords")
        val coords: CoordsDTO,
        @SerializedName("country")
        val country: CountryDTO,
        @SerializedName("id")
        val id: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("state")
        val state: String?,
        @SerializedName("stateCode")
        val stateCode: String?
)

fun CityDTO.toCity() = City(
        id = id,
        name = name,
        state = state ?: "",
        stateCode = stateCode?: "",
        coords = coords.toCoords(),
        country = country.toCountry()
)