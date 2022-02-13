package com.example.evgeny.setlist_mobile.artistSearch
import com.example.evgeny.setlist_mobile.data.Artist
import com.example.evgeny.setlist_mobile.mvp.MVPView
import com.example.evgeny.setlist_mobile.mvp.MvpPresenter

interface ArtistSearchContract {
 
    interface View: MVPView {
		fun showArtistList(words: List<Artist> )
		fun updateArtistList(words: List<Artist> )
        fun showToast(meaasage: String)
        fun openSetlists()
    }
 
    interface Presenter: MvpPresenter<View> {
		fun onListItemClicked(artist: Artist)
        fun onSearchArtistClicked(artistName: String)
        fun getHistorySearchList(): List<String>
    }
}