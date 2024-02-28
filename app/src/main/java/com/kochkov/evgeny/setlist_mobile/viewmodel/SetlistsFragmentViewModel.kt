package com.kochkov.evgeny.setlist_mobile.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kochkov.evgeny.setlist_mobile.App
import com.kochkov.evgeny.setlist_mobile.data.Artist
import com.kochkov.evgeny.setlist_mobile.data.entity.Setlist
import com.kochkov.evgeny.setlist_mobile.domain.Interactor
import com.kochkov.evgeny.setlist_mobile.utils.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ITEMS_STARTING_PAGE = 1
class SetlistsFragmentViewModel(private val artist: Artist) : ViewModel() {


    val items: Flow<PagingData<Setlist>> = Pager(
        config = PagingConfig(pageSize = ITEMS_STARTING_PAGE, enablePlaceholders = false),
        pagingSourceFactory = { interactor.setlistPagingSource(artist.mbid) }
    )
        .flow
        .cachedIn(viewModelScope)

    val artistLiveData = MutableLiveData<Artist>()


    val setlistsLiveData = MutableLiveData<List<Setlist>>()
    val toastEventLiveData = SingleLiveEvent<String>()
    private var isLoading = false
    private var setlistPage = 1

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        toastEventLiveData.postValue(Constants.NETWORK_IS_NOT_OK)
        isLoading = false
        Log.d("BMTH", "throwable: ${throwable.printStackTrace()}")
    }

    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            getSetlists(artist, setlistPage)
        }
        artistLiveData.postValue(artist)
    }

    fun onRecyclerViewScrolled(lastVisiblePos: Int, totalPosCount: Int) {
        if (!isLoading && lastVisiblePos>=totalPosCount-1) {
            isLoading=true
            viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
                getSetlists(artist, setlistPage)
            }
        }
    }

    suspend fun getSetlists(artist: Artist, page: Int) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val list = interactor.getSetlists(artist, page)
            list?.let {
                if (it.isNotEmpty()) {
                    setlistPage++
                    isLoading = false
                    var newList = arrayListOf<Setlist>()
                    newList.addAll(setlistsLiveData.value?: arrayListOf())
                    newList.addAll(it)
                    setlistsLiveData.postValue(newList)
                }
            }
        }
    }

    suspend fun getSetlistsFromDBCoroutines(artist: Artist, page: Int) {

    }
}