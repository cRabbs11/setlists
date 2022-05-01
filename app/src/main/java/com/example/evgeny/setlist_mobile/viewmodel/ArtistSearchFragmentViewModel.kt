package com.example.evgeny.setlist_mobile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.evgeny.setlist_mobile.App
import com.example.evgeny.setlist_mobile.data.Artist
import com.example.evgeny.setlist_mobile.data.entity.ArtistDataDTO
import com.example.evgeny.setlist_mobile.data.entity.SetlistsDataDTO
import com.example.evgeny.setlist_mobile.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ArtistSearchFragmentViewModel: ViewModel() {

    val artistsLiveData = MutableLiveData<List<Artist>>()
    val selectedArtistLiveData = SingleLiveEvent<Artist>()

    @Inject
    lateinit var setlistsRepository: SetlistsRepository

    @Inject
    lateinit var searchHistoryHelper: SearchHistoryHelper

    @Inject
    lateinit var setlistsRetrofit: SetlistsRetrofitInterface

    init {
        App.instance.dagger.inject(this)
    }

    fun searchArtist(artistName: String) {
        if (artistName.isNotEmpty()) {
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
                        //getView()?.showArtistList(artistList)
                        artistsLiveData.postValue(artistList)
                    } else {
                        //getView()?.showToast("артистов не найдено")
                    }
                }

                override fun onFailure(call: Call<ArtistDataDTO>, t: Throwable) {
                    //getView()?.showToast("поиск не удался")
                }
            })
        } else {
            //getView()?.showToast("поле пустое")
        }
    }

    fun getSetlists(artist: Artist) {
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
                    //getView()?.openSetlists()
                    selectedArtistLiveData.postValue(artist)
                } else {
                    //getView()?.showToast("сетлистов для ${artist.name} не найдено")
                }
            }

            override fun onFailure(call: Call<SetlistsDataDTO>, t: Throwable) {
                t.printStackTrace()
                //getView()?.showToast("поиск не удался")
            }
        })
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

    fun getHistorySearchList(): List<String> {
        return searchHistoryHelper.getHistorySearchList()
    }
}