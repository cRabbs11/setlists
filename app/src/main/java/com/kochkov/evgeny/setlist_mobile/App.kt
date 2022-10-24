package com.kochkov.evgeny.setlist_mobile

import android.app.Application
import com.kochkov.evgeny.setlist_mobile.di.AppComponent
import com.kochkov.evgeny.setlist_mobile.di.DaggerAppComponent
import com.kochkov.evgeny.setlist_mobile.di.modules.DataModule
import com.kochkov.evgeny.setlist_mobile.di.modules.DomainModule
import com.kochkov.evgeny.setlist_mobile.di.modules.RemoteModule

class App: Application() {
    lateinit var dagger: AppComponent

    override fun onCreate() {
        super.onCreate()
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