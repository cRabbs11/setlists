package com.example.evgeny.setlist_mobile.singleSetlist
import com.example.evgeny.setlist_mobile.mvp.MVPView
import com.example.evgeny.setlist_mobile.mvp.MvpPresenter
import com.example.evgeny.setlist_mobile.setlists.Setlist
import com.example.evgeny.setlist_mobile.setlists.Song

interface SingleSetlistContract {
 
    interface View: MVPView {
		fun showSetlist(setlist: Setlist )
		fun updateSetlist(setlist: Setlist )
        fun showToast(meaasage: String)
    }
 
    interface Presenter: MvpPresenter<View> {
		fun onListItemClicked(song: Song)
    }
}