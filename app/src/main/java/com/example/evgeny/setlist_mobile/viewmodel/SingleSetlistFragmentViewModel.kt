package com.example.evgeny.setlist_mobile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.evgeny.setlist_mobile.App
import com.example.evgeny.setlist_mobile.setlists.Setlist
import com.example.evgeny.setlist_mobile.setlists.SongListItem
import com.example.evgeny.setlist_mobile.data.SetlistsRepository
import javax.inject.Inject

class SingleSetlistFragmentViewModel: ViewModel() {

    var songListItemLiveData = MutableLiveData<List<SongListItem>>()
    var setlistInfoLiveData = MutableLiveData<Setlist>()

    @Inject
    lateinit var setlistsRepository: SetlistsRepository

    init {
        App.instance.dagger.inject(this)

        val setlist = setlistsRepository.getCurrentSetlist()
        if (setlist!=null) {
            setlistsRepository.setSongList(setlist)
            songListItemLiveData.postValue(setlistsRepository.getSongList())
            setlistInfoLiveData.postValue(setlist)
        }
    }
}