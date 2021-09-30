package com.example.evgeny.setlist_mobile.setlists

data class Artist(val mbid: String, val  name: String, val sortName: String, val url: String) : BaseModel() {
   lateinit var disambiguation : String
}