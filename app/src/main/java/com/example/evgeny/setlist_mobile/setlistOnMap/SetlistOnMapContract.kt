package com.example.evgeny.setlist_mobile.setlistOnMap
import com.example.evgeny.setlist_mobile.mvp.MVPView
import com.example.evgeny.setlist_mobile.mvp.MvpPresenter
import com.example.evgeny.setlist_mobile.setlists.Coords
import com.example.evgeny.setlist_mobile.data.entity.Setlist
import com.example.evgeny.setlist_mobile.setlists.Song

interface SetlistOnMapContract {
 
    interface View: MVPView {
		fun showSetlist(setlist: Setlist)
		fun updateSetlist(setlist: Setlist)
        fun showToast(meaasage: String)
        fun addMarker(coords: Coords)
    }
 
    interface Presenter: MvpPresenter<View> {
		fun onItemClicked(song: Song)
		fun mapIsReady()
    }
}