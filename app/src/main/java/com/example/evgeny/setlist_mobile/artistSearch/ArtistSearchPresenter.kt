package com.example.evgeny.setlist_mobile.artistSearch

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.example.evgeny.setlist_mobile.data.Artist
import com.example.evgeny.setlist_mobile.data.entity.ArtistDataDTO

import com.example.evgeny.setlist_mobile.mvp.PresenterBase
import com.example.evgeny.setlist_mobile.setlists.Setlist
import com.example.evgeny.setlist_mobile.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArtistSearchPresenter(setlistsRepository: SetlistsRepository, val searchHistoryHelper: SearchHistoryHelper, val setlistsAPI: SetlistsRetrofitInterface):
        PresenterBase<ArtistSearchContract.View>(), ArtistSearchContract.Presenter {

	private fun showAddWordDialog() {
		Log.d(LOG_TAG, "showAddWordDialog")

	}

	val LOG_TAG = ArtistSearchPresenter::class.java.name + " BMTH "
	val setlistsRepository: SetlistsRepository
	private val emptySearchText = ""

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
		if (artistName != emptySearchText) {
			setlistsAPI.searchArtists(
					apiKey = RetrofitApiKeys.SETLISTS_API_KEY,
					accept = SetlistsAPIConstants.ACCEPT_HEADER,
					artistName = artistName, 1,
					sort = SetlistsAPIConstants.SORT_TYPE_NAME).enqueue(object: Callback<ArtistDataDTO> {

				override fun onResponse(call: Call<ArtistDataDTO>, response: Response<ArtistDataDTO>) {
					val artistList = Converter.convertArtistDTOListToArtistList(response.body()?.artist)
					if (artistList.isNotEmpty()) {
						setlistsRepository.setLastSearchArtists(artistList)
						if (!isArtistInHistory(artistName)) { searchHistoryHelper.saveSearchQuery(artistName) }
						getView()?.showArtistList(artistList)
					} else {
						getView()?.showToast("артистов не найдено")
					}
				}

				override fun onFailure(call: Call<ArtistDataDTO>, t: Throwable) {
					getView()?.showToast("поиск не удался")
				}
			})
		} else {
			getView()?.showToast("поле пустое")
		}
	}

	private fun isArtistInHistory(artistName: String): Boolean {
		var isInHistory = false
		val searchList = searchHistoryHelper.getHistorySearchList()
		var size = searchList.size
		do {
			size-=1
			if (searchList[size] == artistName) { isInHistory = true }
		} while (isInHistory || size==0)
		return isInHistory
	}

	override fun getHistorySearchList(): List<String> {
		return searchHistoryHelper.getHistorySearchList()
	}

	override fun viewIsReady() {

    }

    init {
		this.setlistsRepository = setlistsRepository
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