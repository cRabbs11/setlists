package com.kochkov.evgeny.setlist_mobile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kochkov.evgeny.setlist_mobile.App
import com.kochkov.evgeny.setlist_mobile.data.entity.Setlist
import com.kochkov.evgeny.setlist_mobile.data.entity.Venue
import com.kochkov.evgeny.setlist_mobile.domain.Interactor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapFragmentViewModel(private val setlist: Setlist) : ViewModel() {

    @Inject
    lateinit var interactor: Interactor

    val venueLiveData = MutableLiveData<Venue>()

    val tourLiveData = MutableLiveData<List<Setlist>>()

    init {
        App.instance.dagger.inject(this)

        viewModelScope.launch(Dispatchers.IO) {
            if (setlist.tour!=null) {
                tourLiveData.postValue(getTour())
            } else {
                venueLiveData.postValue(setlist.venue)
            }
        }
    }

    private suspend fun getTour(): List<Setlist>? {
        return interactor.getSetlistsInTour(setlist.tour!!.name)
    }
}