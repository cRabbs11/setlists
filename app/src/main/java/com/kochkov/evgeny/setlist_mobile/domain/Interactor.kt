package com.kochkov.evgeny.setlist_mobile.domain

import com.kochkov.evgeny.setlist_mobile.data.Artist
import com.kochkov.evgeny.setlist_mobile.data.SetlistsRepository
import com.kochkov.evgeny.setlist_mobile.data.entity.Setlist
import io.reactivex.rxjava3.core.Observable

class Interactor(private val repository: SetlistsRepository) {

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
}