package com.example.evgeny.setlist_mobile.di

import com.example.evgeny.setlist_mobile.artistSearch.ArtistSearchFragment
import com.example.evgeny.setlist_mobile.di.modules.DataModule
import com.example.evgeny.setlist_mobile.di.modules.RemoteModule
import com.example.evgeny.setlist_mobile.setlistsSearch.SetlistsSearchFragment
import com.example.evgeny.setlist_mobile.singleSetlist.SingleSetlistFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component (modules = [
    DataModule::class,
    RemoteModule::class])
interface AppComponent {

    fun inject(artistSearchFragment: ArtistSearchFragment)
    fun inject(setlistsSearchFragment: SetlistsSearchFragment)
    fun inject(singleSetlistFragment: SingleSetlistFragment)
}