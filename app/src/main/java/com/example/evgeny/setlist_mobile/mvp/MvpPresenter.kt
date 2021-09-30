package com.example.evgeny.setlist_mobile.mvp

interface MvpPresenter<V: MVPView> {

    fun attachView(mvpView: V)
 
    fun viewIsReady()
 
    fun detachView()
 
    fun destroy()
}