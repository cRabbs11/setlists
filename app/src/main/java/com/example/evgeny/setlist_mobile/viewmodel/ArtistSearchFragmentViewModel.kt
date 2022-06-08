package com.example.evgeny.setlist_mobile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.evgeny.setlist_mobile.App
import com.example.evgeny.setlist_mobile.data.Artist
import com.example.evgeny.setlist_mobile.data.SearchQuery
import com.example.evgeny.setlist_mobile.domain.Interactor
import com.example.evgeny.setlist_mobile.utils.*
import javax.inject.Inject

class ArtistSearchFragmentViewModel: ViewModel() {

    val artistsLiveData = MutableLiveData<List<Artist>>()
    val isSetlistsHaveLiveData = SingleLiveEvent<Boolean>()
    val searchQueryArtistLiveData = MutableLiveData<List<SearchQuery>>()
    val toastEventLiveData = SingleLiveEvent<String>()
    val ARTIST_SEARCH_ON_FAILURE = "ничего не найдено"
    val ARTIST_SEARCH_FIELD_IS_EMPTY = "введите что-то в поле поиска"
    val SETLISTS_SEARCH_FAILURE = "поиск сетлистов неудался"

    @Inject
    lateinit var interactor: Interactor

    @Inject
    lateinit var setlistsRepository: SetlistsRepository

    @Inject
    lateinit var setlistsRetrofit: SetlistsRetrofitInterface

    init {
        App.instance.dagger.inject(this)
    }

    fun searchArtist(artistName: String) {
        if (artistName.isNotEmpty()) {
            interactor.searchArtist(artistName, object : Interactor.OnRetrofitCallback<List<Artist>> {
                override fun onSuccess(list: List<Artist>) {
                    artistsLiveData.postValue(list)
                }

                override fun onFailure() {
                    toastEventLiveData.postValue(ARTIST_SEARCH_ON_FAILURE)
                }
            })
        } else {
            toastEventLiveData.postValue(ARTIST_SEARCH_FIELD_IS_EMPTY)
        }
    }

    fun getSetlists(artist: Artist) {
        interactor.getSetlists(artist, object: Interactor.OnRetrofitCallback<Boolean> {
            override fun onSuccess(item: Boolean) {
                isSetlistsHaveLiveData.postValue(item)
            }

            override fun onFailure() {
                toastEventLiveData.postValue(SETLISTS_SEARCH_FAILURE)
            }
        })
    }

    fun getSearchQueryArtists(): List<String> {
        return setlistsRepository.getSearchQueryArtists()
    }
}