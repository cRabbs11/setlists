package com.example.evgeny.setlist_mobile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.evgeny.setlist_mobile.App
import com.example.evgeny.setlist_mobile.data.Artist
import com.example.evgeny.setlist_mobile.data.SearchQuery
import com.example.evgeny.setlist_mobile.data.entity.SetlistsDataDTO
import com.example.evgeny.setlist_mobile.domain.Interactor
import com.example.evgeny.setlist_mobile.utils.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ArtistSearchFragmentViewModel: ViewModel() {

    val artistsLiveData = MutableLiveData<List<Artist>>()
    val selectedArtistLiveData = SingleLiveEvent<Artist>()
    val searchQueryArtistLiveData = MutableLiveData<List<SearchQuery>>()
    val toastEventLiveData = SingleLiveEvent<String>()
    val ARTIST_SEARCH_ON_FAILURE = "ничего не найдено"
    val ARTIST_SEARCH_FIELD_IS_EMPTY = "введите что-то в поле поиска"

    @Inject
    lateinit var interactor: Interactor

    @Inject
    lateinit var setlistsRepository: SetlistsRepository

    @Inject
    lateinit var setlistsRetrofit: SetlistsRetrofitInterface

    init {
        App.instance.dagger.inject(this)
    }

    fun searchArtist(artistName: String) {
        if (artistName.isNotEmpty()) {
            interactor.searchArtist(artistName, object : Interactor.OnRetrofitCallback {
                override fun onSuccess(list: List<Artist>) {
                    artistsLiveData.postValue(list)
                }

                override fun onFailure() {
                    toastEventLiveData.postValue(ARTIST_SEARCH_ON_FAILURE)
                }
            })
        } else {
            toastEventLiveData.postValue(ARTIST_SEARCH_FIELD_IS_EMPTY)
        }
    }

    fun getSetlists(artist: Artist) {
        setlistsRetrofit.getArtistSetlists(
                artistMbid = artist.mbid,
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

    fun getSearchQueryArtists(): List<String> {
        return setlistsRepository.getSearchQueryArtists()
    }
}