package com.example.evgeny.setlist_mobile.data.entity

import com.example.evgeny.setlist_mobile.data.Artist
import com.example.evgeny.setlist_mobile.setlists.BaseModel
import com.example.evgeny.setlist_mobile.setlists.Set
import com.example.evgeny.setlist_mobile.setlists.Tour
import com.example.evgeny.setlist_mobile.setlists.Venue

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