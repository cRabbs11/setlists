package com.kochkov.evgeny.setlist_mobile.di.modules

import com.kochkov.evgeny.setlist_mobile.domain.Interactor
import com.kochkov.evgeny.setlist_mobile.domain.repository.LocalRepository
import com.kochkov.evgeny.setlist_mobile.domain.repository.RemoteRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule {

    @Singleton
    @Provides
    fun provideInteractor(
        remoteRepository: RemoteRepository,
        localRepository: LocalRepository): Interactor = Interactor(remoteRepository, localRepository)
}