package com.example.evgeny.setlist_mobile.setlists

data class Setlist(val artist: Artist?,
                   val venue: Venue?,
                   val tour: Tour?,
                   //val set: Set,
                   //val info: String,
                   //val url: String,
                   //val id: String,
                   //val versionId: String,
                   val eventDate: String,
                   val lastUpdated: String,
                   val sets: List<Set>) : BaseModel() {
}