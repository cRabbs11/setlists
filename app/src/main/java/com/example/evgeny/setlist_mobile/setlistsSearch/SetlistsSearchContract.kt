package com.example.evgeny.setlist_mobile.setlistsSearch
import com.example.evgeny.setlist_mobile.mvp.MVPView
import com.example.evgeny.setlist_mobile.mvp.MvpPresenter

import com.example.evgeny.setlist_mobile.setlists.Setlist

interface SetlistsSearchContract {
 
    interface View: MVPView {
		fun showSetlistList(list: List<Setlist> )
		fun updateSetlistList(list: List<Setlist> )
        fun showToast(meaasage: String)
        fun openSetlist()
    }
 
    interface Presenter: MvpPresenter<View> {
		fun onListItemClicked(setlist: Setlist)
		fun onRecyclerViewScrolled(lastVisiblePos: Int)
    }
}