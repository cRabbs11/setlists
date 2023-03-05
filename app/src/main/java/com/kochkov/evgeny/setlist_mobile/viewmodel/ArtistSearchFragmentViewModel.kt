package com.kochkov.evgeny.setlist_mobile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kochkov.evgeny.setlist_mobile.App
import com.kochkov.evgeny.setlist_mobile.data.Artist
import com.kochkov.evgeny.setlist_mobile.data.SetlistsRepository
import com.kochkov.evgeny.setlist_mobile.data.entity.toArtistListFlow
import com.kochkov.evgeny.setlist_mobile.domain.Interactor
import com.kochkov.evgeny.setlist_mobile.utils.*
import com.kochkov.evgeny.setlist_mobile.utils.Constants.ARTIST_SEARCH_FIELD_IS_EMPTY
import com.kochkov.evgeny.setlist_mobile.utils.Constants.ARTIST_SEARCH_ON_FAILURE
import com.kochkov.evgeny.setlist_mobile.utils.Constants.NETWORK_IS_NOT_OK
import com.kochkov.evgeny.setlist_mobile.utils.Constants.SETLISTS_SEARCH_FAILURE
import com.kochkov.evgeny.setlist_mobile.utils.Constants.SETLISTS_SEARCH_NOT_FOUND
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.net.UnknownHostException
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

    fun searchArtist(artistName: String) {
        if (artistName.isNotEmpty()) {
            loadingIndicatorLiveData.postValue(true)
            interactor.searchArtist(artistName)
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onNext = { list ->
                        artistsLiveData.postValue(list)
                        loadingIndicatorLiveData.postValue(false)
                    },
                    onError = {
                        toastEventLiveData.postValue(ARTIST_SEARCH_ON_FAILURE)
                        loadingIndicatorLiveData.postValue(false)
                    }
                )
        } else {
            toastEventLiveData.postValue(ARTIST_SEARCH_FIELD_IS_EMPTY)
        }
    }

    fun searchArtistCoroutines(artistName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = interactor.searchArtistCoroutines(artistName)
                when (result.isSuccessful) {
                    true -> {
                        result.body()?.let {
                            it.toArtistListFlow().collect {
                                artistsLiveData.postValue(it)
                                loadingIndicatorLiveData.postValue(false)
                            }
                        } ?: toastEventLiveData.postValue(ARTIST_SEARCH_ON_FAILURE)
                    }
                    false -> {
                        toastEventLiveData.postValue(ARTIST_SEARCH_ON_FAILURE)
                    }
                }
            } catch (e: UnknownHostException) {
                toastEventLiveData.postValue(NETWORK_IS_NOT_OK)
            }
        }
    }

    fun isSetlistsHave(artist: Artist) {
        interactor.isHaveSetlists(artist)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = { ifSetlistsHave ->
                    if (ifSetlistsHave) {
                        interactor.setNewArtist()
                        isSetlistsHaveLiveData.postValue(artist)
                    } else {
                        toastEventLiveData.postValue(SETLISTS_SEARCH_NOT_FOUND)
                    }
                },
                onError = {
                    toastEventLiveData.postValue(SETLISTS_SEARCH_FAILURE)
                }
            )
    }
}