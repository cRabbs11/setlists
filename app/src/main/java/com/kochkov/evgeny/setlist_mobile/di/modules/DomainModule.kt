package com.kochkov.evgeny.setlist_mobile.di.modules

import com.kochkov.evgeny.setlist_mobile.domain.Interactor
import com.kochkov.evgeny.setlist_mobile.data.SetlistsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule {

    @Singleton
    @Provides
    fun provideInteractor(repository: SetlistsRepository): Interactor = Interactor(repository)
}