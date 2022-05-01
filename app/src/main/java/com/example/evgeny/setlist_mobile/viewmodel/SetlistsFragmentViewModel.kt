package com.example.evgeny.setlist_mobile.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.evgeny.setlist_mobile.App
import com.example.evgeny.setlist_mobile.data.entity.SetlistsDataDTO
import com.example.evgeny.setlist_mobile.setlists.Setlist
import com.example.evgeny.setlist_mobile.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class SetlistsFragmentViewModel : ViewModel() {

    var setlistsLiveData = MutableLiveData<List<Setlist>>()
    private var isLoading = false

    @Inject
    lateinit var setlistsRepository: SetlistsRepository

    @Inject
    lateinit var retrofitInterface: SetlistsRetrofitInterface

    init {
        App.instance.dagger.inject(this)
        getSetlists()
    }

    fun getSetlists() {
        if (setlistsRepository.getSetlists().size!=0 &&
                setlistsRepository.getSetlists()[0].artist==setlistsRepository.getCurrentArtist()) {
            //getView()?.showSetlistList(setlistsRepository.getSetlists())
            setlistsLiveData.postValue(setlistsRepository.getSetlists())
        } else {
            val artist = setlistsRepository.getCurrentArtist()
            if (artist!=null) {
                retrofitInterface.getArtistSetlists(
                        artistMbid = artist.mbid,
                        apiKey = ApiKeys.SETLISTS_API_KEY,
                        accept = SetlistsAPIConstants.ACCEPT_HEADER,
                        page = setlistsRepository.getSetlistPage()
                ).enqueue(object: Callback<SetlistsDataDTO> {
                    override fun onResponse(call: Call<SetlistsDataDTO>, response: Response<SetlistsDataDTO>) {
                        val list = Converter.convertSetlistDTOListToSetlistList(response.body()?.setlist)
                        if (list.isNotEmpty()) {
                            setlistsRepository.setNewSetlists(list)
                            setlistsLiveData.postValue(list)
                            //getView()?.showSetlistList(list)
                        } else {
                            //getView()?.showToast("сетлистов для ${artist.name} не найдено")
                        }

                    }

                    override fun onFailure(call: Call<SetlistsDataDTO>, t: Throwable) {
                        //getView()?.showToast("поиск не удался")
                    }

                })
            }
        }
    }

    fun onRecyclerViewScrolled(lastVisiblePos: Int, totalPosCount: Int) {
        //Log.d(TAG, "onRecyclerViewScrolled")
        //Log.d(TAG, "lastVisiblePos=$lastVisiblePos, size= ${setlistsRepository.getSetlists().size}")
        if (!isLoading && lastVisiblePos>=totalPosCount-1) {
            isLoading=true
            retrofitInterface.getArtistSetlists(
                    artistMbid = setlistsRepository.getCurrentArtist()!!.mbid,
                    apiKey = ApiKeys.SETLISTS_API_KEY,
                    accept = SetlistsAPIConstants.ACCEPT_HEADER,
                    page = setlistsRepository.getSetlistPage()
            ).enqueue(object: Callback<SetlistsDataDTO> {
                override fun onResponse(call: Call<SetlistsDataDTO>, response: Response<SetlistsDataDTO>) {
                    val list = Converter.convertSetlistDTOListToSetlistList(response.body()?.setlist)
                    if (list.isNotEmpty()) {
                        setlistsRepository.addToSetlists(list)
                        //getView()?.showSetlistList(setlistsRepository.getSetlists())
                        setlistsLiveData.postValue(list)
                        setlistsRepository.increaseSetlistPage()
                    } else {
                        //getView()?.showToast("сетлистов для ${setlistsRepository.getCurrentArtist()?.name} не найдено")
                    }
                    isLoading=false
                }

                override fun onFailure(call: Call<SetlistsDataDTO>, t: Throwable) {
                    //getView()?.showToast("поиск не удался")
                    isLoading=false
                }

            })
        }
    }

    fun setCurrentSetlist(setlist: Setlist) {
        setlistsRepository.setCurrentSetlist(setlist)
    }
}