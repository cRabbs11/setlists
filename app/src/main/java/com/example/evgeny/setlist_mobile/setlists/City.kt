package com.example.evgeny.setlist_mobile.setlists

data class City(val id: String,
                val name: String,
                val state: String,
                val stateCode: String,
                val coords: Coords?,
                val country: Country?) : BaseModel() {
}