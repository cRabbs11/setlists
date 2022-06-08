package com.example.evgeny.setlist_mobile.domain

import com.example.evgeny.setlist_mobile.data.AppDataBase
import com.example.evgeny.setlist_mobile.data.Artist
import com.example.evgeny.setlist_mobile.data.SearchQuery
import com.example.evgeny.setlist_mobile.utils.Converter
import com.example.evgeny.setlist_mobile.utils.SetlistsAPIConstants
import com.example.evgeny.setlist_mobile.utils.SetlistsRepository
import com.example.evgeny.setlist_mobile.utils.SetlistsRetrofitInterface
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers


class Interactor(private val repository: SetlistsRepository, private val retrofit: SetlistsRetrofitInterface) {

    fun searchArtist(artistName: String, callback: OnRetrofitCallback<List<Artist>>) {
        val observable = retrofit.searchArtistsObservable(
            artistName = artistName,
            page = 1,
            sort = SetlistsAPIConstants.SORT_TYPE_NAME)
            .subscribeOn(Schedulers.io())
            .onErrorComplete {
                callback.onFailure()
                true
            }
            .map {
                Converter.convertArtistDTOListToArtistList(it.artist)
            }
            .subscribeBy(
                onNext = {
                    repository.setLastSearchArtists(it)
                    val searchQuery = SearchQuery(queryText = artistName, searchType = AppDataBase.SEARCH_TYPE_ARTISTS)
                    repository.saveSearchQueryArtists(searchQuery)
                    callback.onSuccess(it)
                },
                onError = {
                    callback.onFailure()
                }
            )
    }

    fun getSetlists(artist: Artist, callback: OnRetrofitCallback<Boolean>) {
        retrofit.getSetlistsByArtistObservable(
            artistMbid = artist.mbid,
            page = 1
        ).subscribeOn(Schedulers.io())
            .onErrorComplete {
                callback.onSuccess(false)
                true
            }
            .map {
                Converter.convertSetlistDTOListToSetlistList(it.setlist)
            }
            .subscribeBy(
                onNext = {
                    if (!it.isEmpty()) {
                        repository.setCurrentArtist(artist)
                        repository.setNewSetlists(it)
                        callback.onSuccess(true)
                    } else {
                        callback.onSuccess(false)
                    }
                },
                onError = {
                    callback.onSuccess(false)
                }
            )
    }

    interface OnRetrofitCallback<T> {
        fun onSuccess(item: T)
        fun onFailure()
    }
}