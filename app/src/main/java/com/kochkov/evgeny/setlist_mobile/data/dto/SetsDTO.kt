package com.kochkov.evgeny.setlist_mobile.data.dto

import com.google.gson.annotations.SerializedName

class SetsDTO(
        @SerializedName("set")
        val set: List<SetDTO>) {

}