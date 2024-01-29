package com.kochkov.evgeny.setlist_mobile.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kochkov.evgeny.setlist_mobile.App
import com.kochkov.evgeny.setlist_mobile.data.Artist
import com.kochkov.evgeny.setlist_mobile.data.SetlistsRepository
import com.kochkov.evgeny.setlist_mobile.domain.Interactor
import com.kochkov.evgeny.setlist_mobile.utils.*
import com.kochkov.evgeny.setlist_mobile.utils.Constants.ARTIST_SEARCH_FIELD_IS_EMPTY
import com.kochkov.evgeny.setlist_mobile.utils.Constants.ARTIST_SEARCH_ON_FAILURE
import com.kochkov.evgeny.setlist_mobile.utils.Constants.NETWORK_IS_NOT_OK
import com.kochkov.evgeny.setlist_mobile.utils.Constants.SETLISTS_SEARCH_NOT_FOUND
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.*
import javax.inject.Inject

class ArtistSearchFragmentViewModel: ViewModel() {

    val artistsLiveData = MutableLiveData<List<Artist>>()
    val isSetlistsHaveLiveData = SingleLiveEvent<Artist>()
    val queryArtistLiveData = MutableLiveData<List<String>>()
    val toastEventLiveData = SingleLiveEvent<String>()
    val loadingIndicatorLiveData = MutableLiveData<Boolean>()

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        loadingIndicatorLiveData.postValue(false)
        toastEventLiveData.postValue(NETWORK_IS_NOT_OK)
        Log.d("BMTH", "throwable: ${throwable.printStackTrace()}")
    }

    @Inject
    lateinit var interactor: Interactor

    @Inject
    lateinit var setlistsRepository: SetlistsRepository

    init {
        App.instance.dagger.inject(this)

        setlistsRepository.getSearchQueryArtists()
            .subscribeOn(Schedulers.io())
            .map { list ->
                val result = arrayListOf<String>()
                list.forEach {
                    result.add(it.queryText)
                }
                result
            }
            .subscribe{
                if (it.isNotEmpty()) {
                    queryArtistLiveData.postValue(it)
                }
            }
        loadingIndicatorLiveData.postValue(false)
    }

    //fun searchArtistCoroutines(artistName: String) {
    //    if (artistName.isNotEmpty()) {
    //        loadingIndicatorLiveData.postValue(true)
    //                    viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
    //            val list = interactor.searchArtist(artistName)
    //                            list?.let {
    //                artistsLiveData.postValue(it)
    //                                } ?: toastEventLiveData.postValue(ARTIST_SEARCH_ON_FAILURE)
    //                            loadingIndicatorLiveData.postValue(false)
    //                        }
    //                } else {
    //        toastEventLiveData.postValue(ARTIST_SEARCH_FIELD_IS_EMPTY)
    //                }
    //        }

    fun searchArtistWithSetlists(artistName: String) {
        if (artistName.isNotEmpty()) {
            loadingIndicatorLiveData.postValue(true)
            viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
                val list = interactor.searchArtistWithSetlists(artistName)
                list?.let {
                    artistsLiveData.postValue(it)
                } ?: toastEventLiveData.postValue(ARTIST_SEARCH_ON_FAILURE)
                loadingIndicatorLiveData.postValue(false)
            }
        } else {
            toastEventLiveData.postValue(ARTIST_SEARCH_FIELD_IS_EMPTY)
        }
    }


    fun isSetlistsHave(artist: Artist) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val result = interactor.isHaveSetlists(artist)
            if (result) {
                interactor.setNewArtist()
                isSetlistsHaveLiveData.postValue(artist)
            } else {
                toastEventLiveData.postValue(SETLISTS_SEARCH_NOT_FOUND)
            }
        }
    }
}