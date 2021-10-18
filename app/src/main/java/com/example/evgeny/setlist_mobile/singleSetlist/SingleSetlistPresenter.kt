package com.example.evgeny.setlist_mobile.singleSetlist



import com.example.evgeny.setlist_mobile.mvp.PresenterBase
import com.example.evgeny.setlist_mobile.setlists.Song
import com.example.evgeny.setlist_mobile.utils.SetlistsRepository


public class SingleSetlistPresenter(setlistsRepository: SetlistsRepository):
        PresenterBase<SingleSetlistContract.View>(), SingleSetlistContract.Presenter {

	val LOG_TAG = SingleSetlistPresenter::class.java.name + " BMTH "
	val setlistsRepository: SetlistsRepository


	override fun onListItemClicked(song: Song) {

	}

	override fun onMapClicked() {
		getView()?.openMap()
	}


	override fun viewIsReady() {
		val setlist = setlistsRepository.getCurrentSetlist()
		if (setlist!=null) {
			getView()?.showSetlist(setlist)
		}
    }

    init {
		this.setlistsRepository = setlistsRepository
    }

}