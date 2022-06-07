package com.example.evgeny.setlist_mobile.di.modules

import com.example.evgeny.setlist_mobile.domain.Interactor
import com.example.evgeny.setlist_mobile.utils.SetlistsRepository
import com.example.evgeny.setlist_mobile.utils.SetlistsRetrofitInterface
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule {

    @Singleton
    @Provides
    fun provideInteractor(repository: SetlistsRepository,
                          retrofitInterface: SetlistsRetrofitInterface)
    : Interactor = Interactor(repository, retrofitInterface)
}