package com.example.evgeny.setlist_mobile.data.entity

import com.google.gson.annotations.SerializedName

class SetsDTO(
        @SerializedName("set")
        val set: List<SetDTO>) {

}