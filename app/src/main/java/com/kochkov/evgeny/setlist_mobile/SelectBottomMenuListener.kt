package com.kochkov.evgeny.setlist_mobile

import com.kochkov.evgeny.setlist_mobile.setlists.BaseModel

interface SelectBottomMenuListener {
    fun <T : BaseModel?> setMenuItem(item: String?, model: T)

}