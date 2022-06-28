package com.example.evgeny.setlist_mobile.data.entity

import com.google.gson.annotations.SerializedName

data class SetlistDTO(
        @SerializedName("artist")
        val artist: ArtistDTO,
        @SerializedName("eventDate")
        val eventDate: String,
        @SerializedName("id")
        val id: String,
        @SerializedName("info")
        val info: String,
        @SerializedName("lastUpdated")
        val lastUpdated: String,
        @SerializedName("sets")
        val sets: SetsDTO,
        @SerializedName("tour")
        val tour: TourDTO?,
        @SerializedName("url")
        val url: String,
        @SerializedName("venue")
        val venue: VenueDTO,
        @SerializedName("versionId")
        val versionId: String,
)

fun SetlistDTO.toSetlist(): Setlist {
        val list = arrayListOf<Set>()
        sets.set.forEach {
                list.add(it.toSet())
        }
        return Setlist(
                artist = artist.toArtist(),
                venue = venue.toVenue(),
                tour = tour?.toTour(),
                eventDate = eventDate,
                lastUpdated = lastUpdated,
                sets = list
        )
}