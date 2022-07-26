package com.example.evgeny.setlist_mobile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.evgeny.setlist_mobile.data.entity.Venue

class MapFragmentViewModel(private val venue: Venue) : ViewModel() {

    val venueLiveData = MutableLiveData<Venue>()

    init {
        venueLiveData.postValue(venue)
    }
}