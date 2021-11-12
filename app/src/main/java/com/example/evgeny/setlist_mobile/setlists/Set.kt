package com.example.evgeny.setlist_mobile.setlists

data class Set(val name: String, val number: String, val encore: String, val songs: List<Song> = arrayListOf()) : BaseModel(), SongListItem {

    override fun toString(): String {
        return "Set(name='$name', number='$number', encore='$encore', songs=$songs)"
    }
}