package com.example.evgeny.setlist_mobile.singleSetlist
import com.example.evgeny.setlist_mobile.mvp.MVPView
import com.example.evgeny.setlist_mobile.mvp.MvpPresenter
import com.example.evgeny.setlist_mobile.setlists.Setlist
import com.example.evgeny.setlist_mobile.setlists.Song
import com.example.evgeny.setlist_mobile.setlists.SongListItem

interface SingleSetlistContract {
 
    interface View: MVPView {
		fun showSetlist(songList: ArrayList<SongListItem>)
		fun showSetlistInfo(setlist: Setlist )
        fun showToast(meaasage: String)
        fun openMap()
    }
 
    interface Presenter: MvpPresenter<View> {
		fun onListItemClicked(song: Song)
        fun onMapClicked()
    }
}