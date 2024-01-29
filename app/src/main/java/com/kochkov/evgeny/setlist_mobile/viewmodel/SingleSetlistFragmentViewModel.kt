package com.kochkov.evgeny.setlist_mobile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kochkov.evgeny.setlist_mobile.App
import com.kochkov.evgeny.setlist_mobile.data.entity.Setlist
import com.kochkov.evgeny.setlist_mobile.data.entity.Venue
import com.kochkov.evgeny.setlist_mobile.setlists.SongListItem
import com.kochkov.evgeny.setlist_mobile.utils.SetlistHelper

class SingleSetlistFragmentViewModel(private val setlist: Setlist): ViewModel() {

    var songListItemLiveData = MutableLiveData<List<SongListItem>>()
    var setlistInfoLiveData = MutableLiveData<Setlist>()

    init {
        App.instance.dagger.inject(this)
        songListItemLiveData.postValue(SetlistHelper.fromSetlistToSongList(setlist))
        setlistInfoLiveData.postValue(setlist)

    }

    fun getVenue(): Venue? {
        return setlist.venue
    }

    fun getSetlist(): Setlist {
        return setlist
    }
}