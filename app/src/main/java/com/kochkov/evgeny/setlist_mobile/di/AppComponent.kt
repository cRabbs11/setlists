package com.kochkov.evgeny.setlist_mobile.di

import com.kochkov.evgeny.setlist_mobile.di.modules.DataModule
import com.kochkov.evgeny.setlist_mobile.di.modules.DomainModule
import com.kochkov.evgeny.setlist_mobile.di.modules.RemoteModule
import com.kochkov.evgeny.setlist_mobile.viewmodel.ArtistSearchFragmentViewModel
import com.kochkov.evgeny.setlist_mobile.viewmodel.MapFragmentViewModel
import com.kochkov.evgeny.setlist_mobile.viewmodel.SetlistsFragmentViewModel
import com.kochkov.evgeny.setlist_mobile.viewmodel.SingleSetlistFragmentViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component (modules = [
    DataModule::class,
    RemoteModule::class,
    DomainModule::class])
interface AppComponent {

    fun inject(setlistsFragmentViewModel: SetlistsFragmentViewModel)
    fun inject(singleSetlistFragmentViewModel: SingleSetlistFragmentViewModel)
    fun inject(artistSearchFragmentViewModel: ArtistSearchFragmentViewModel)
    fun inject(mapFragmentViewModel: MapFragmentViewModel)
}