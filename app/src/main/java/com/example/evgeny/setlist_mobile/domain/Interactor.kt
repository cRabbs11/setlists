package com.example.evgeny.setlist_mobile.domain

import com.example.evgeny.setlist_mobile.data.Artist
import com.example.evgeny.setlist_mobile.data.SearchQuery
import com.example.evgeny.setlist_mobile.data.SetlistsRepository
import com.example.evgeny.setlist_mobile.data.entity.Setlist
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

class Interactor(private val repository: SetlistsRepository) {

    fun saveSearchQuery(searchQuery: SearchQuery): Single<Long> {
        return repository.saveSearchQueryArtists(searchQuery)
            .subscribeOn(Schedulers.io())
    }

    fun setNewArtist() {
        repository.setNewArtist()
    }

    fun searchArtist(artistName: String): Observable<List<Artist>> {
        return repository.searchArtist(artistName)
    }

    fun isHaveSetlists(artist: Artist): Observable<Boolean> {
        return repository.isSetlistsHave(artist)
    }

    fun getSetlists(artist: Artist): Observable<List<Setlist>> {
        return if (artist!=null) {
            repository.getSetlists(artist)
        } else {
            Observable.empty<List<Setlist>>()
        }
    }

    fun getSearchQueryArtists(): Observable<List<String>>  {
        return repository.getSearchQueryArtists()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { list ->
                val result = arrayListOf<String>()
                list.forEach {
                    result.add(it.queryText)
                }
                result
            }
    }

    fun addArtistInWatched(artist: Artist) {
        artist.isWatched = true
        repository.addArtistInwathed(artist)
            .subscribeOn(Schedulers.io())
            .subscribe { result, throwable ->
                Timber.d("result = $result")
            }
    }

    fun getRecentlyWatchedArtists(): Observable<List<Artist>> {
        return repository.getWatchedArtists()
    }
}