package com.kochkov.evgeny.setlist_mobile.utils

interface RequestListener<T> {
    fun onSuccessResponse(t: T)
    fun onNullResponse()
}