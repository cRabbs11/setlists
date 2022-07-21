package com.example.evgeny.setlist_mobile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.evgeny.setlist_mobile.App
import com.example.evgeny.setlist_mobile.data.Artist
import com.example.evgeny.setlist_mobile.data.entity.Setlist
import com.example.evgeny.setlist_mobile.setlists.SongListItem
import com.example.evgeny.setlist_mobile.utils.SetlistHelper

class SingleSetlistFragmentViewModel(private val setlist: Setlist): ViewModel() {

    var songListItemLiveData = MutableLiveData<List<SongListItem>>()
    var setlistInfoLiveData = MutableLiveData<Setlist>()

    init {
        App.instance.dagger.inject(this)
        songListItemLiveData.postValue(SetlistHelper.fromSetlistToSongList(setlist))
        setlistInfoLiveData.postValue(setlist)

    }

    fun isVenueHave(): Boolean {
        return setlist.venue!=null
    }

    fun getArtist(): Artist {
        return setlist.artist!!
    }
}