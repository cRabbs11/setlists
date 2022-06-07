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

    fun searchArtist(artistName: String, callback: OnRetrofitCallback) {
        val observable = retrofit.searchArtistsObservable(
            artistName = artistName,
            page = 1,
            sort = SetlistsAPIConstants.SORT_TYPE_NAME)
            .subscribeOn(Schedulers.io())
            .onErrorComplete {
                callback.onFailure()
                false
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

    interface OnRetrofitCallback {
        fun onSuccess(list: List<Artist>)
        fun onFailure()
    }
}