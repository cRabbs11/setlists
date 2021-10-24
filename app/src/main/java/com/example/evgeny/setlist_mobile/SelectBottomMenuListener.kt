package com.example.evgeny.setlist_mobile

import com.example.evgeny.setlist_mobile.setlists.BaseModel

interface SelectBottomMenuListener {
    fun <T : BaseModel?> setMenuItem(item: String?, model: T)

}