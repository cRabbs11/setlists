package com.example.evgeny.setlist_mobile.utils

import androidx.room.TypeConverter
import com.example.evgeny.setlist_mobile.setlists.Set
import com.google.gson.Gson

class SetsTypeConverter {

    @TypeConverter
    fun fromSetsToString(sets: List<Set>): String {
        return Gson().toJson(sets)
    }

    @TypeConverter
    fun fromTringToSets(string: String): List<Set> {
        val array = Gson().fromJson(string, Array<Set>::class.java)
        return array.toList()
    }
}

