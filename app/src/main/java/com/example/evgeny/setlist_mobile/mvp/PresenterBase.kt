package com.example.evgeny.setlist_mobile.mvp

abstract class PresenterBase<T: MVPView>: MvpPresenter<T> {
 
    private var view: T? = null

    override fun attachView(mvpView: T) {
        view = mvpView
    }
 
    override fun detachView() {
        view = null
    }
 
    fun getView(): T? {
        return view
    }
 
    override fun destroy() {
 
    }
}