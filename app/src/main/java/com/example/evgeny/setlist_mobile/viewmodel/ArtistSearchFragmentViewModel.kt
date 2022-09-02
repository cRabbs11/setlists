package com.example.evgeny.setlist_mobile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.evgeny.setlist_mobile.App
import com.example.evgeny.setlist_mobile.data.AppDataBase
import com.example.evgeny.setlist_mobile.data.Artist
import com.example.evgeny.setlist_mobile.data.SearchQuery
import com.example.evgeny.setlist_mobile.data.SetlistsRepository
import com.example.evgeny.setlist_mobile.domain.Interactor
import com.example.evgeny.setlist_mobile.utils.*
import com.example.evgeny.setlist_mobile.utils.Constants.ARTIST_SEARCH_FIELD_IS_EMPTY
import com.example.evgeny.setlist_mobile.utils.Constants.ARTIST_SEARCH_ON_FAILURE
import com.example.evgeny.setlist_mobile.utils.Constants.SETLISTS_SEARCH_FAILURE
import com.example.evgeny.setlist_mobile.utils.Constants.SETLISTS_SEARCH_NOT_FOUND
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

class ArtistSearchFragmentViewModel: ViewModel() {

    val artistsLiveData = MutableLiveData<List<Artist>>()
    val isSetlistsHaveLiveData = SingleLiveEvent<Artist>()
    val queryArtistLiveData = MutableLiveData<List<String>>()
    val toastEventLiveData = SingleLiveEvent<String>()
    val loadingIndicatorLiveData = MutableLiveData<Boolean>()

    @Inject
    lateinit var interactor: Interactor

    @Inject
    lateinit var setlistsRepository: SetlistsRepository

    init {
        App.instance.dagger.inject(this)
        getSearchQueryArtists()
        loadingIndicatorLiveData.postValue(false)
    }

    fun searchArtist(artistName: String) {
        if (artistName.isNotEmpty()) {
            loadingIndicatorLiveData.postValue(true)
            interactor.searchArtist(artistName)
                .subscribeBy(
                    onNext = { list ->
                        if (list.isNotEmpty()) {
                            saveSearchQuery(SearchQuery(queryText = artistName, searchType = AppDataBase.SEARCH_TYPE_ARTISTS))
                        }
                        artistsLiveData.postValue(list)
                        loadingIndicatorLiveData.postValue(false)
                    },
                    onError = {
                        toastEventLiveData.postValue(ARTIST_SEARCH_ON_FAILURE)
                        loadingIndicatorLiveData.postValue(false)
                    },
                )
        } else {
            toastEventLiveData.postValue(ARTIST_SEARCH_FIELD_IS_EMPTY)
        }
    }

    fun isSetlistsHave(artist: Artist) {
        loadingIndicatorLiveData.postValue(true)
        interactor.isHaveSetlists(artist)
            .subscribeBy(
                onNext = { ifSetlistsHave ->
                    loadingIndicatorLiveData.postValue(false)
                    if (ifSetlistsHave) {
                        interactor.setNewArtist()
                        isSetlistsHaveLiveData.postValue(artist)
                    } else {
                        toastEventLiveData.postValue(SETLISTS_SEARCH_NOT_FOUND)
                    }
                },
                onError = {
                    loadingIndicatorLiveData.postValue(false)
                    toastEventLiveData.postValue(SETLISTS_SEARCH_FAILURE)
                }
            )
    }

    private fun saveSearchQuery(searchQuery: SearchQuery) {
        interactor.saveSearchQuery(searchQuery).subscribe()
    }

    private fun getSearchQueryArtists() {
        interactor.getSearchQueryArtists()
            .subscribe{
            if (it.isNotEmpty()) {
                queryArtistLiveData.postValue(it)
            }
        }
    }
}