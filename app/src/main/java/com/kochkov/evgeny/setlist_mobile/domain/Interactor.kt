package com.kochkov.evgeny.setlist_mobile.domain

import com.kochkov.evgeny.setlist_mobile.data.AppDataBase
import com.kochkov.evgeny.setlist_mobile.data.Artist
import com.kochkov.evgeny.setlist_mobile.data.SearchQuery
import com.kochkov.evgeny.setlist_mobile.domain.repository.LocalRepository
import com.kochkov.evgeny.setlist_mobile.domain.repository.RemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class Interactor(private val remoteRepository: RemoteRepository, private val localRepository: LocalRepository) {

    fun setNewArtist() {
        MainScope().launch(Dispatchers.IO) {
            localRepository.setNewArtist()
        }
    }

    suspend fun getSearchQueryArtists() = localRepository.getSearchQueryArtists()

    suspend fun searchArtistsWithSetlists(artistName: String): List<Artist> {
        return coroutineScope {
            val list = remoteRepository.searchArtists(artistName)
            val artistsWithSetlists = arrayListOf<Artist>()
            val deferred = list.flatMap {
                listOf(
                    async {
                        if (isArtistHaveSetlists(it)) {
                            it
                        } else {null}
                    }
                )
            }

            deferred.awaitAll().forEach { artist ->
                artist?.let {
                    artistsWithSetlists.add(it)
                }
            }
            if (artistsWithSetlists.isNotEmpty()) {
                val searchQuery = SearchQuery(queryText = artistName, searchType = AppDataBase.SEARCH_TYPE_ARTISTS)
                localRepository.saveSearchQuery(searchQuery)
            }
            artistsWithSetlists
        }
    }

    suspend fun isHaveSetlists(artist: Artist) = remoteRepository.isSetlistsHave(artist)

    suspend fun getSetlists(artist: Artist, page: Int) = remoteRepository.getSetlists(artist, page)

    suspend fun getSetlistsInTour(tourName: String) = remoteRepository.getAllSetlistsInTour(tourName)

    fun setlistPagingSource(artist: String) = remoteRepository.setlistPagingSource(artist)

    private suspend fun isArtistHaveSetlists(artist: Artist): Boolean {
        return coroutineScope {
            remoteRepository.getSetlists(artist, 1).isNotEmpty()
        }
    }

    //код на rxJava для получения сетлистов и с БД и с сети
    //fun getSetlistsWithDB(artist: Artist, page: Int): Observable<List<Setlist>> {
    //    return Observable.concat(
    //        retrofit.getSetlistsByArtistObservable(
    //            artistMbid = artist.mbid,
    //            page = page
    //        ).subscribeOn(Schedulers.io())
    //            .onErrorComplete{
    //                false
    //            }
    //            .map {
    //                val list = it.toSetlistList()
    //                list
    //            }
    //            .flatMap {
    //                if (it.isNotEmpty()) {
    //                    insertSetlistsInDB(it)
    //                }
    //                Observable.empty<List<Setlist>>()
    //            },
    //        artistDao.getSetlists().subscribeOn(Schedulers.io())
    //    )
    //}
}