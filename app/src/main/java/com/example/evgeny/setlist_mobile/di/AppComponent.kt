package com.example.evgeny.setlist_mobile.di

import com.example.evgeny.setlist_mobile.di.modules.DataModule
import com.example.evgeny.setlist_mobile.di.modules.RemoteModule
import com.example.evgeny.setlist_mobile.singleSetlist.SingleSetlistFragment
import com.example.evgeny.setlist_mobile.viewmodel.ArtistSearchFragmentViewModel
import com.example.evgeny.setlist_mobile.viewmodel.SetlistsFragmentViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component (modules = [
    DataModule::class,
    RemoteModule::class])
interface AppComponent {

    fun inject(setlistsFragmentViewModel: SetlistsFragmentViewModel)
    fun inject(singleSetlistFragment: SingleSetlistFragment)
    fun inject(artistSearchFragmentViewModel: ArtistSearchFragmentViewModel)
}