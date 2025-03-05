package com.kochkov.evgeny.setlist_mobile.utils

import com.kochkov.evgeny.setlist_mobile.data.Artist
import com.kochkov.evgeny.setlist_mobile.data.dto.ArtistDTO
import com.kochkov.evgeny.setlist_mobile.data.dto.ArtistDataDTO
import com.kochkov.evgeny.setlist_mobile.data.dto.CityDTO
import com.kochkov.evgeny.setlist_mobile.data.dto.CoordsDTO
import com.kochkov.evgeny.setlist_mobile.data.dto.CountryDTO
import com.kochkov.evgeny.setlist_mobile.data.dto.SetDTO
import com.kochkov.evgeny.setlist_mobile.data.dto.SetlistDTO
import com.kochkov.evgeny.setlist_mobile.data.dto.SetlistsDataDTO
import com.kochkov.evgeny.setlist_mobile.data.dto.SongDTO
import com.kochkov.evgeny.setlist_mobile.data.dto.TourDTO
import com.kochkov.evgeny.setlist_mobile.data.dto.VenueDTO
import com.kochkov.evgeny.setlist_mobile.data.entity.City
import com.kochkov.evgeny.setlist_mobile.data.entity.Coords
import com.kochkov.evgeny.setlist_mobile.data.entity.Country
import com.kochkov.evgeny.setlist_mobile.data.entity.Set
import com.kochkov.evgeny.setlist_mobile.data.entity.Setlist
import com.kochkov.evgeny.setlist_mobile.data.entity.Song
import com.kochkov.evgeny.setlist_mobile.data.entity.Tour
import com.kochkov.evgeny.setlist_mobile.data.entity.Venue
import com.kochkov.evgeny.setlist_mobile.setlists.SongListItem

object SetlistConverter {

    fun fromSetlistToSongList(setlist: Setlist): List<SongListItem> {
        val newSongList = arrayListOf<SongListItem>()
        setlist.sets.forEach { set ->
            if (set.name!="") {
                newSongList.add(set)
            } else if (set.encore>0) {
                newSongList.add(set)
            }
            set.songs.forEach { song ->
                newSongList.add(song)
            }
        }
        return newSongList
    }

    fun fromArtistDataDTOToArtists(artistDataDTO: ArtistDataDTO): List<Artist> {
        val list = arrayListOf<Artist>()
        artistDataDTO.artist.forEach { artistDTO ->
            list.add(fromArtistDTOToArtist(artistDTO))
        }
        return list
    }

    fun fromArtistDTOToArtist(artistDTO: ArtistDTO): Artist {
        return Artist(
            disambiguation = artistDTO.disambiguation ?: "",
            mbid = artistDTO.mbid,
            name = artistDTO.name,
            sortName = artistDTO.sortName,
            tmid = artistDTO.tmid,
            url = artistDTO.url)
    }

    fun fromSetlistDataDTOtoSetlists(setlistsDataDTO: SetlistsDataDTO): List<Setlist> {
        val list = arrayListOf<Setlist>()
        setlistsDataDTO.setlist.forEach {setlistDTO ->
            list.add(fromSetlistDTOtoSetlist(setlistDTO))
        }
        return list
    }

    private fun fromSetlistDTOtoSetlist(setlistDTO: SetlistDTO): Setlist {
        val list = arrayListOf<Set>()
        var songNumber = 1
        setlistDTO.sets.set.forEach {
            val set = fromSetDTOToSet(it, songNumber)
            songNumber = set.songs.last().number + 1
            list.add(set)
        }
        return Setlist(
            id = setlistDTO.id.hashCode(),
            artist = fromArtistDTOToArtist(setlistDTO.artist) ,
            venue = fromVenueDTOToVenue(setlistDTO.venue),
            tour = setlistDTO.tour?.let { fromTourDTOToTour(it) },
            eventDate = setlistDTO.eventDate,
            lastUpdated = setlistDTO.lastUpdated,
            sets = list
        )
    }

    private fun fromTourDTOToTour(tourDTO: TourDTO) = Tour(name = tourDTO.name ?: "")

    private fun fromVenueDTOToVenue(venueDTO: VenueDTO) = Venue(
        id = venueDTO.id,
        name = venueDTO.name,
        url = venueDTO.url,
        city = fromCityDTOToCity(venueDTO.city)
    )

    private fun fromCityDTOToCity(cityDTO: CityDTO) = City(
        id = cityDTO.id,
        name = cityDTO.name,
        state = cityDTO.state ?: "",
        stateCode = cityDTO.stateCode?: "",
        coords = fromCoordsDTOToCoords(cityDTO.coords),
        country = fromCountryDTOToCountry(cityDTO.country)
    )

    private fun fromCountryDTOToCountry(countryDTO: CountryDTO) = Country(
        code = countryDTO.code,
        name = countryDTO.name
    )

    private fun fromCoordsDTOToCoords(coordsDTO: CoordsDTO) = Coords(
    coord_lat = coordsDTO.lat.toString(),
    coord_long = coordsDTO.long.toString()
    )

    private fun fromSetDTOToSet(setDTO: SetDTO, songNumber: Int): Set {
        var number = songNumber
        val list = arrayListOf<Song>()
        setDTO.song.forEach {
            list.add(fromSongDTOToSong(it, number))
            if (!it.tape) {
                number++
            }
        }
        return Set(
            name = setDTO.name?: "",
            encore = setDTO.encore,
            songs = list)
    }

    private fun fromSongDTOToSong(songDTO: SongDTO, songNumber: Int) = Song(
        name = songDTO.name,
        info = songDTO.info,
        tape = songDTO.tape,
        cover = songDTO.cover?.let { fromArtistDTOToArtist(it) },
        with = songDTO.with?.let { fromArtistDTOToArtist(it) },
        number = songNumber)
}