package com.example.evgeny.setlist_mobile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.evgeny.setlist_mobile.App
import com.example.evgeny.setlist_mobile.data.Artist
import com.example.evgeny.setlist_mobile.data.SearchQuery
import com.example.evgeny.setlist_mobile.data.SetlistsRepository
import com.example.evgeny.setlist_mobile.domain.Interactor
import com.example.evgeny.setlist_mobile.utils.*
import com.example.evgeny.setlist_mobile.utils.Constants.ARTIST_SEARCH_FIELD_IS_EMPTY
import com.example.evgeny.setlist_mobile.utils.Constants.ARTIST_SEARCH_ON_FAILURE
import com.example.evgeny.setlist_mobile.utils.Constants.SETLISTS_SEARCH_FAILURE
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class ArtistSearchFragmentViewModel: ViewModel() {

    val artistsLiveData = MutableLiveData<List<Artist>>()
    val isSetlistsHaveLiveData = SingleLiveEvent<Boolean>()
    val searchQueryArtistLiveData = MutableLiveData<List<SearchQuery>>()
    val toastEventLiveData = SingleLiveEvent<String>()

    @Inject
    lateinit var interactor: Interactor

    @Inject
    lateinit var setlistsRepository: SetlistsRepository

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

    fun isSetlistsHave(artist: Artist) {
        interactor.setSelectedArtist(artist)
        interactor.isHaveSetlists(artist)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = {
                    isSetlistsHaveLiveData.postValue(it)
                },
                onError = {
                    toastEventLiveData.postValue(SETLISTS_SEARCH_FAILURE)
                }
            )
    }

    fun getSearchQueryArtists(): List<String> {
        return setlistsRepository.getSearchQueryArtists()
    }
}