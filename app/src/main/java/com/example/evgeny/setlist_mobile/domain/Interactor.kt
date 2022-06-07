package com.example.evgeny.setlist_mobile.domain

import com.example.evgeny.setlist_mobile.data.AppDataBase
import com.example.evgeny.setlist_mobile.data.Artist
import com.example.evgeny.setlist_mobile.data.SearchQuery
import com.example.evgeny.setlist_mobile.data.entity.ArtistDataDTO
import com.example.evgeny.setlist_mobile.utils.Converter
import com.example.evgeny.setlist_mobile.utils.SetlistsAPIConstants
import com.example.evgeny.setlist_mobile.utils.SetlistsRepository
import com.example.evgeny.setlist_mobile.utils.SetlistsRetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors


class Interactor(val repository: SetlistsRepository, val retrofit: SetlistsRetrofitInterface) {

    fun searchArtist(artistName: String, callback: OnRetrofitCallback) {
        retrofit.searchArtists(
            artistName = artistName,
            page = 1,
            sort = SetlistsAPIConstants.SORT_TYPE_NAME).enqueue(object: Callback<ArtistDataDTO> {
            override fun onResponse(call: Call<ArtistDataDTO>, response: Response<ArtistDataDTO>) {
                val artistList = Converter.convertArtistDTOListToArtistList(response.body()?.artist)
                if (artistList.isNotEmpty()) {
                    repository.setLastSearchArtists(artistList)
                    //if (!isArtistInHistory(artistName)) {
                        val searchQuery = SearchQuery(queryText = artistName, searchType = AppDataBase.SEARCH_TYPE_ARTISTS)
                        Executors.newSingleThreadExecutor().execute {
                            repository.saveSearchQueryArtists(searchQuery)
                        }
                    //}
                    callback.onSuccess(artistList)
                    //artistsLiveData.postValue(artistList)
                } else {
                    callback.onFailure()
                }
            }

            override fun onFailure(call: Call<ArtistDataDTO>, t: Throwable) {
            }
        })

    }

    interface OnRetrofitCallback {
        fun onSuccess(list: List<Artist>)
        fun onFailure()
    }
}