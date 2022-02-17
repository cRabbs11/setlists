package com.example.evgeny.setlist_mobile.artistSearch

import android.util.Log
import com.example.evgeny.setlist_mobile.data.Artist
import com.example.evgeny.setlist_mobile.data.entity.ArtistDataDTO
import com.example.evgeny.setlist_mobile.data.entity.SetlistsDataDTO

import com.example.evgeny.setlist_mobile.mvp.PresenterBase
import com.example.evgeny.setlist_mobile.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArtistSearchPresenter(setlistsRepository: SetlistsRepository, val searchHistoryHelper: SearchHistoryHelper, val setlistsRetrofit: SetlistsRetrofitInterface):
        PresenterBase<ArtistSearchContract.View>(), ArtistSearchContract.Presenter {

	private fun showAddWordDialog() {
		Log.d(LOG_TAG, "showAddWordDialog")

	}

	val LOG_TAG = ArtistSearchPresenter::class.java.name + " BMTH "
	val setlistsRepository: SetlistsRepository
	private val emptySearchText = ""

	override fun onListItemClicked(artist: Artist) {
		setlistsRetrofit.getArtistSetlists(
				artistMbid = artist.mbid,
				apiKey = ApiKeys.SETLISTS_API_KEY,
				accept = SetlistsAPIConstants.ACCEPT_HEADER,
				page = 1
		).enqueue(object: Callback<SetlistsDataDTO> {
			override fun onResponse(call: Call<SetlistsDataDTO>, response: Response<SetlistsDataDTO>) {
				val setlistList = Converter.convertSetlistDTOListToSetlistList(response.body()?.setlist)
				println(response.body())
				if (setlistList.isNotEmpty()) {
					setlistsRepository.setCurrentArtist(artist)
					setlistsRepository.setNewSetlists(setlistList)
					getView()?.openSetlists()
				} else {
					getView()?.showToast("сетлистов для ${artist.name} не найдено")
				}
			}

			override fun onFailure(call: Call<SetlistsDataDTO>, t: Throwable) {
				t.printStackTrace()
				getView()?.showToast("поиск не удался")
			}
		})
	}

	override fun onSearchArtistClicked(artistName: String) {
		if (artistName != emptySearchText) {
			setlistsRetrofit.searchArtists(
					apiKey = ApiKeys.SETLISTS_API_KEY,
					accept = SetlistsAPIConstants.ACCEPT_HEADER,
					artistName = artistName,
					page = 1,
					sort = SetlistsAPIConstants.SORT_TYPE_NAME
			).enqueue(object: Callback<ArtistDataDTO> {
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
		if (searchList.isNotEmpty()) {
			var size = searchList.size
			do {
				size-=1
				if (searchList[size] == artistName) { isInHistory = true }
			} while (!isInHistory && size>0)
		}
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
}