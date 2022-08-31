package com.example.evgeny.setlist_mobile

import android.app.Application
import com.example.evgeny.setlist_mobile.di.AppComponent
import com.example.evgeny.setlist_mobile.di.DaggerAppComponent
import com.example.evgeny.setlist_mobile.di.modules.DataModule
import com.example.evgeny.setlist_mobile.di.modules.DomainModule
import com.example.evgeny.setlist_mobile.di.modules.RemoteModule
import timber.log.Timber
import timber.log.Timber.Forest.plant

class App: Application() {
    lateinit var dagger: AppComponent

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            plant(Timber.DebugTree())
        }
        instance = this
        dagger = DaggerAppComponent.builder()
            .dataModule(DataModule(this))
            .remoteModule(RemoteModule())
            .domainModule(DomainModule())
            .build()
    }

    companion object {
        //Здесь статически хранится ссылка на экземпляр App
        lateinit var instance: App
            //Приватный сеттер, чтобы нельзя было в эту переменную присвоить что-либо другое
            private set
    }
}