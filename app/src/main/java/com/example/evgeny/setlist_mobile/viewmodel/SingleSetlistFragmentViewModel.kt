package com.example.evgeny.setlist_mobile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.evgeny.setlist_mobile.App
import com.example.evgeny.setlist_mobile.data.entity.Setlist
import com.example.evgeny.setlist_mobile.setlists.SongListItem
import com.example.evgeny.setlist_mobile.data.SetlistsRepository
import com.example.evgeny.setlist_mobile.utils.SetlistHelper
import javax.inject.Inject

class SingleSetlistFragmentViewModel(private val setlist: Setlist): ViewModel() {

    var songListItemLiveData = MutableLiveData<List<SongListItem>>()
    var setlistInfoLiveData = MutableLiveData<Setlist>()

    @Inject
    lateinit var setlistsRepository: SetlistsRepository

    init {
        App.instance.dagger.inject(this)
        songListItemLiveData.postValue(SetlistHelper.fromSetlistToSongList(setlist))
        setlistInfoLiveData.postValue(setlist)

    }
}