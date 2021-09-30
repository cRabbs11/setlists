package com.example.evgeny.setlist_mobile.setlistsSearch

import android.content.ContentValues.TAG
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.ekochkov.intervallearning.mvp.PresenterBase
import com.example.evgeny.setlist_mobile.setlists.Artist
import com.example.evgeny.setlist_mobile.setlists.Setlist
import com.example.evgeny.setlist_mobile.utils.AnswerListener
import com.example.evgeny.setlist_mobile.utils.SetlistsRepository


public class SetlistsSearchPresenter(setlistsRepository: SetlistsRepository):
        PresenterBase<SetlistsSearchContract.View>(), SetlistsSearchContract.Presenter {

	private fun showAddWordDialog() {
		Log.d(LOG_TAG, "showAddWordDialog")

	}

	val LOG_TAG = SetlistsSearchPresenter::class.java.name + " BMTH "
	val setlistsRepository: SetlistsRepository

	override fun onListItemClicked(setlist: Setlist) {
		setlistsRepository.setCurrentSetlist(setlist)
		getView()?.openSetlist()
	}


	override fun viewIsReady() {
		val artist = setlistsRepository.getCurrentArtist()
		if (artist!=null) {
			searchSetlists(artist.mbid, object : AnswerListener<List<Setlist>> {
				override fun getAnswer(t: List<Setlist>) {
					getView()?.showSetlistList(t)
					if (t.isEmpty()) {
						getView()?.showToast("сетлистов для ${artist.name} не найдено")
					}
				}
			})
		}
    }

    init {
		this.setlistsRepository = setlistsRepository
    }

	private fun searchSetlists(artistMbid: String, answerListener: AnswerListener<List<Setlist>>) {
		val setlists = ArrayList<Setlist>()
		val handler = object : Handler(Looper.getMainLooper()) {
			override fun handleMessage(msg: Message) {
				answerListener.getAnswer(setlists)
			}
		}

		val runnable = Runnable {
			kotlin.run {
				setlistsRepository.searchSetlists(artistMbid).forEach {
					setlists.add(it)
				}
				handler.sendEmptyMessage(1)
			}
		}

		val thread = Thread(runnable)
		thread.start()
	}


}