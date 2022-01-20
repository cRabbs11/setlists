package com.example.evgeny.setlist_mobile.artistSearch

import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log

import com.example.evgeny.setlist_mobile.mvp.PresenterBase
import com.example.evgeny.setlist_mobile.setlists.Artist
import com.example.evgeny.setlist_mobile.setlists.ParserKotlin
import com.example.evgeny.setlist_mobile.setlists.Setlist
import com.example.evgeny.setlist_mobile.setlists.SetlistsAPI
import com.example.evgeny.setlist_mobile.utils.AnswerListener
import com.example.evgeny.setlist_mobile.utils.SearchHistoryHelper
import com.example.evgeny.setlist_mobile.utils.SetlistsRepository


public class ArtistSearchPresenter(setlistsRepository: SetlistsRepository, val searchHistoryHelper: SearchHistoryHelper):
        PresenterBase<ArtistSearchContract.View>(), ArtistSearchContract.Presenter {

	private fun showAddWordDialog() {
		Log.d(LOG_TAG, "showAddWordDialog")

	}

	val LOG_TAG = ArtistSearchPresenter::class.java.name + " BMTH "
	val setlistsRepository: SetlistsRepository

	override fun onListItemClicked(artist: Artist) {
		searchSetlists(artist.mbid, object : AnswerListener<List<Setlist>> {
			override fun getAnswer(t: List<Setlist>) {
				if (t.isEmpty()) {
					getView()?.showToast("сетлистов для ${artist.name} не найдено")
				} else {
					setlistsRepository.setCurrentArtist(artist)
					getView()?.openSetlists()
				}
			}
		})
	}

	override fun onSearchArtistClicked(artistName: String) {
		if (artistName != "") {
			searchArtists(artistName, object : AnswerListener<List<Artist>> {
				override fun getAnswer(t: List<Artist>) {
					Log.d(TAG, "пришли")
					getView()?.showArtistList(t)
					if (t.isEmpty()) {
						getView()?.showToast("артистов не найдено")
					}
				}
			})
		} else {
			getView()?.showToast("поле пустое")
		}
	}

	override fun getHistorySearchList(): List<String> {
		return searchHistoryHelper.getHistorySearchList()
	}

	override fun viewIsReady() {

    }

    init {
		this.setlistsRepository = setlistsRepository
    }

	private fun searchArtists(artistName: String, answerListener: AnswerListener<List<Artist>>) {
		val artists = ArrayList<Artist>()
		val handler = object : Handler(Looper.getMainLooper()) {
			override fun handleMessage(msg: Message) {
				answerListener.getAnswer(artists)
			}
		}

		val runnable = Runnable {
			        kotlin.run {
						setlistsRepository.newSearchArtists(artistName).forEach {
							artists.add(it)
						}

						if (artists.isNotEmpty()) {
							var isInHistory = false
							searchHistoryHelper.getHistorySearchList().forEach {
								if (it == artistName) { isInHistory = true }
							}
							if (!isInHistory) { searchHistoryHelper.saveSearchQuery(artistName) }
						}
						handler.sendEmptyMessage(1)
			}
		}

		val thread = Thread(runnable)
		thread.start()
	}

	private fun searchSetlists(artistMbid: String, answerListener: AnswerListener<List<Setlist>>) {
		val handler = object : Handler(Looper.getMainLooper()) {
			override fun handleMessage(msg: Message) {
				answerListener.getAnswer(setlistsRepository.getSetlists())
			}
		}

		val runnable = Runnable {
			val setlists = ArrayList<Setlist>()
			kotlin.run {
				setlistsRepository.searchSetlists(artistMbid).forEach {
					setlists.add(it)
				}
				setlistsRepository.setNewSetlists(setlists)
				handler.sendEmptyMessage(1)
			}
		}

		val thread = Thread(runnable)
		thread.start()
	}


}