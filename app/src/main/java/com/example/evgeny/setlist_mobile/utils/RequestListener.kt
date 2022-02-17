package com.example.evgeny.setlist_mobile.utils

interface RequestListener<T> {
    fun onSuccessResponse(t: T)
    fun onNullResponse()
}