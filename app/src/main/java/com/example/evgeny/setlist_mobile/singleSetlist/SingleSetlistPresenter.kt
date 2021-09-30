package com.example.evgeny.setlist_mobile.singleSetlist

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.ekochkov.intervallearning.mvp.PresenterBase
import com.example.evgeny.setlist_mobile.setlists.Artist
import com.example.evgeny.setlist_mobile.setlists.Setlist
import com.example.evgeny.setlist_mobile.setlists.Song
import com.example.evgeny.setlist_mobile.utils.AnswerListener
import com.example.evgeny.setlist_mobile.utils.SetlistsRepository


public class SingleSetlistPresenter(setlistsRepository: SetlistsRepository):
        PresenterBase<SingleSetlistContract.View>(), SingleSetlistContract.Presenter {

	val LOG_TAG = SingleSetlistPresenter::class.java.name + " BMTH "
	val setlistsRepository: SetlistsRepository


	override fun onListItemClicked(song: Song) {

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