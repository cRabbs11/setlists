package com.example.evgeny.setlist_mobile.utils

import com.example.evgeny.setlist_mobile.data.Artist
import com.example.evgeny.setlist_mobile.data.entity.*
import com.example.evgeny.setlist_mobile.setlists.*
import com.example.evgeny.setlist_mobile.setlists.Set

object DTOConverter {
    fun convertArtistDTOListToArtistList(list : List<ArtistDTO>?) : List<Artist> {
        val artistList = arrayListOf<Artist>()
        list?.forEach {
            artistList.add(
                    convertArtistDtoToArtist(it))
        }
        return artistList
    }

    fun convertSetlistDTOListToSetlistList(list : List<SetlistDTO>?) : List<Setlist> {
        val setlistList = arrayListOf<Setlist>()
        list?.forEach {
            setlistList.add(convertSetlistDtoToSetlist(it))
        }
        return setlistList
    }

    private fun convertArtistDtoToArtist(item: ArtistDTO): Artist {
        return Artist(
                disambiguation = item?.disambiguation ?: "",
                mbid = item.mbid,
                name = item.name,
                sortName = item.sortName,
                tmid = item.tmid,
                url = item.url)
    }

    private fun convertSetlistDtoToSetlist(item: SetlistDTO): Setlist {
        songNumber = 1
        return  Setlist(
                artist = convertArtistDtoToArtist(item.artist),
                venue = convertVenueDtoToVenue(item.venue),
                tour = convertTourDtoToTour(item.tour),
                eventDate = item.eventDate,
                lastUpdated = item.lastUpdated,
                sets = convertSetListDTOtoSetList(item.sets.set))
    }

    private fun convertVenueDtoToVenue(item: VenueDTO) : Venue {
        return Venue(
                id = item.id,
                name = item.name,
                url = item.url,
                city = convertCityDtoToCity(item.city))
    }

    private fun convertCityDtoToCity(item: CityDTO): City {
        return City(
                id = item.id,
                name = item.name,
                state = item.state ?: "",
                stateCode = item.stateCode?: "",
                coords = convertCoordsDtoToCoords(item.coords),
                country = convertCountryDtoToCountry(item.country)
        )
    }

    private fun convertCoordsDtoToCoords(item: CoordsDTO): Coords {
        return Coords(
                coord_lat = item.lat.toString(),
                coord_long = item.long.toString()
        )
    }

    private fun convertCountryDtoToCountry(item: CountryDTO): Country {
        return Country(
                code = item.code,
                name = item.name
        )
    }

    private fun convertTourDtoToTour(item: TourDTO?): Tour {
        return Tour(
                name = item?.name ?: ""
        )
    }

    private fun convertSetListDTOtoSetList(list : List<SetDTO>?) : List<Set> {
        val setList = arrayListOf<Set>()
        list?.forEach {
            setList.add(convertSetDtoToSet(it))
        }
        return setList
    }

    var songNumber = 1
    private fun convertSetDtoToSet(item: SetDTO): Set {
        return Set(
                name = item.name?: "",
                encore = item.encore,
                songs = convertSongListDTOtoSongList(item.song)
        )
    }

    private fun convertSongListDTOtoSongList(list : List<SongDTO>?) : List<Song> {
        val songList = arrayListOf<Song>()
        list?.forEach {
            songList.add(convertSongDtoToSong(it))
        }
        return songList
    }

    private fun convertSongDtoToSong(item: SongDTO): Song {
        val cover = if (item.cover!=null) {convertArtistDtoToArtist(item.cover)} else { null }

        val with = if (item.with!=null) {convertArtistDtoToArtist(item.with)} else { null }

        val number = if (!item.tape) {
            songNumber++
        } else {
            songNumber
        }

        return Song(
                name = item.name,
                info = item.info,
                tape = item.tape,
                cover = cover,
                with = with,
                number = number
        )
    }




}