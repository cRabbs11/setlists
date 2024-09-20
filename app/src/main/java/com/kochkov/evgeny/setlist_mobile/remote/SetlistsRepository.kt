package com.kochkov.evgeny.setlist_mobile.remote

import com.kochkov.evgeny.setlist_mobile.data.Artist
import com.kochkov.evgeny.setlist_mobile.data.SetlistPagingSource
import com.kochkov.evgeny.setlist_mobile.data.entity.*
import com.kochkov.evgeny.setlist_mobile.domain.repository.RemoteRepository
import com.kochkov.evgeny.setlist_mobile.utils.SetlistConverter
import com.kochkov.evgeny.setlist_mobile.utils.SetlistsAPIConstants
import com.kochkov.evgeny.setlist_mobile.utils.SetlistsAPIConstants.SETLISTS_IN_TOUR_IS_NULL
import kotlinx.coroutines.*

class SetlistsRepository(private val retrofit: SetlistsRetrofitInterface): RemoteRepository {

    val TAG = SetlistsRepository::class.java.name + " BMTH "

    private val lastSearchArtists = ArrayList<Artist>()

    override suspend fun searchArtists(artistName: String): List<Artist> {
        val list = arrayListOf<Artist>()
        val searchResult = retrofit.searchArtists(
            artistName = artistName,
            page = 1,
            sort = SetlistsAPIConstants.SORT_TYPE_NAME)
        searchResult.body()?.let {
            list.addAll(SetlistConverter.fromArtistDataDTOToArtists(it))
        }
        return list
    }

    fun setLastSearchArtists(list: List<Artist>) {
        lastSearchArtists.clear()
        list.forEach {
            lastSearchArtists.add(it)
        }
    }

    override suspend fun getSetlists(artist: Artist, page: Int): List<Setlist> {
        return coroutineScope {
            val result = retrofit.getSetlistsByArtist(
                artistMbid = artist.mbid,
                page = page)
            val list = arrayListOf<Setlist>()
            result.body()?.let {
                list.addAll(SetlistConverter.fromSetlistDataDTOtoSetlists(it))
            }
            list
            //SetlistHelper.fromSetlistDataDTOtoSetlists()
            //result.body()?.toSetlistList()
        }
    }

    override suspend fun getAllSetlistsInTour(tourName: String): List<Setlist> {
        return coroutineScope {
            val setlistsInTour = arrayListOf<Setlist>()
            var isTourEnded = false
            var page = 0
            var setlistsInTourTotal = SETLISTS_IN_TOUR_IS_NULL
            var setlistsInTourCount = 0
            while (!isTourEnded) {
                val response = retrofit.searchSetlistsByTour(tourName, ++page)
                setlistsInTourTotal = response.body()?.total?: SETLISTS_IN_TOUR_IS_NULL
                response.body()?.let {
                    SetlistConverter.fromSetlistDataDTOtoSetlists(it).forEach { setlist ->
                        setlistsInTour.add(setlist)
                        setlistsInTourCount++
                    }
                    if (setlistsInTourCount>=setlistsInTourTotal) isTourEnded = true
                    //убрать проверку на совпадение имени тура (это происходит внутри апи?)
                }
            }
            setlistsInTour
        }
    }

    private suspend fun isSetlistsHaveReturnedArtist(artist: Artist): Artist? {
        return coroutineScope {
            val result = retrofit.getSetlistsByArtist(artist.mbid, 1)
            result.body()?.let {
                if (SetlistConverter.fromSetlistDataDTOtoSetlists(it).isNotEmpty()) {
                    artist
                } else {
                    null
                }
            }
        }
    }

    override fun setlistPagingSource(artist : String) = SetlistPagingSource(retrofit, artist)

}