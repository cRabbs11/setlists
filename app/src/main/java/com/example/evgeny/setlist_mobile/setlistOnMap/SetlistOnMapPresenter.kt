package com.example.evgeny.setlist_mobile.setlistOnMap
import com.example.evgeny.setlist_mobile.mvp.PresenterBase
import com.example.evgeny.setlist_mobile.setlists.Song
import com.example.evgeny.setlist_mobile.data.SetlistsRepository

public class SetlistOnMapPresenter(setlistsRepository: SetlistsRepository):
        PresenterBase<SetlistOnMapContract.View>(), SetlistOnMapContract.Presenter {

	val TAG = SetlistOnMapPresenter::class.java.name + " BMTH "
	val setlistsRepository: SetlistsRepository


	override fun onItemClicked(song: Song) {

	}

	override fun mapIsReady() {
		val coords = setlistsRepository.getCurrentSetlist()?.venue?.city?.coords
		if (coords!=null) {
			getView()?.addMarker(coords)
		}
	}


	override fun viewIsReady() {
		//val setlist = setlistsRepository.getCurrentSetlist()
				//if (setlist!=null) {
		//	getView()?.showSetlist(setlist)
							//}
    }

    init {
		this.setlistsRepository = setlistsRepository
    }

}