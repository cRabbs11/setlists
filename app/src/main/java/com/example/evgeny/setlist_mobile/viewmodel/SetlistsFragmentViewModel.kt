package com.example.evgeny.setlist_mobile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.evgeny.setlist_mobile.App
import com.example.evgeny.setlist_mobile.data.Artist
import com.example.evgeny.setlist_mobile.data.entity.Setlist
import com.example.evgeny.setlist_mobile.domain.Interactor
import com.example.evgeny.setlist_mobile.utils.*
import com.example.evgeny.setlist_mobile.utils.Constants.SETLISTS_SEARCH_FAILURE
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class SetlistsFragmentViewModel(private val artist: Artist) : ViewModel() {

    val setlistsLiveData = MutableLiveData<List<Setlist>>()
    val toastEventLiveData = SingleLiveEvent<String>()
    private var isLoading = false

    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
        getSetlists(artist)
    }

    fun onRecyclerViewScrolled(lastVisiblePos: Int, totalPosCount: Int) {
        if (!isLoading && lastVisiblePos>=totalPosCount-1) {
            isLoading=true
            getSetlists(artist)
        }
    }

    private fun getSetlists(artist: Artist) {
        interactor.getSetlists(artist)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = {
                    isLoading = false
                    setlistsLiveData.postValue(it)
                },
                onError = {
                    isLoading = false
                    toastEventLiveData.postValue(SETLISTS_SEARCH_FAILURE)
                }
            )
    }

}