package com.example.evgeny.setlist_mobile.setlists

data class Artist(
        val disambiguation: String,
        val mbid: String,
        val name: String,
        val sortName: String,
        val tmid: Int,
        val url: String
) : BaseModel()