package com.kochkov.evgeny.setlist_mobile.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kochkov.evgeny.setlist_mobile.App
import com.kochkov.evgeny.setlist_mobile.data.Artist
import com.kochkov.evgeny.setlist_mobile.domain.Interactor
import com.kochkov.evgeny.setlist_mobile.utils.*
import com.kochkov.evgeny.setlist_mobile.utils.Constants.ARTIST_SEARCH_FIELD_IS_EMPTY
import com.kochkov.evgeny.setlist_mobile.utils.Constants.ARTIST_SEARCH_ON_FAILURE
import com.kochkov.evgeny.setlist_mobile.utils.Constants.NETWORK_IS_NOT_OK
import com.kochkov.evgeny.setlist_mobile.utils.Constants.SETLISTS_SEARCH_NOT_FOUND
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

    init {
        App.instance.dagger.inject(this)

        viewModelScope.launch(Dispatchers.IO) {
            interactor.getSearchQueryArtists().collect { list ->
                val result = arrayListOf<String>()
                list.forEach {
                    result.add(it.queryText)
                }
                queryArtistLiveData.postValue(result)
            }
        }
        loadingIndicatorLiveData.postValue(false)
    }

    fun searchArtistWithSetlists(artistName: String) {
        if (artistName.isNotEmpty()) {
            loadingIndicatorLiveData.postValue(true)
            artistsLiveData.postValue(arrayListOf())
            viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
                val list = interactor.searchArtistsWithSetlists(artistName)
                if (list.isEmpty()) {
                    toastEventLiveData.postValue(ARTIST_SEARCH_ON_FAILURE)
                } else {
                    artistsLiveData.postValue(list)
                }
                loadingIndicatorLiveData.postValue(false)
            }
        } else {
            toastEventLiveData.postValue(ARTIST_SEARCH_FIELD_IS_EMPTY)
        }
    }
}