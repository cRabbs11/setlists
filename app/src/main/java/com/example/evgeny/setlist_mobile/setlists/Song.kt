package com.example.evgeny.setlist_mobile.setlists

data class Song(val name: String, val info: String, val tape: Boolean, val cover: Artist?, var number: Int) : BaseModel() {

}