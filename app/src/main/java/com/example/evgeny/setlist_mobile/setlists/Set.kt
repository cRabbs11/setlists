package com.example.evgeny.setlist_mobile.setlists

data class Set(val name: String, val encore: Int, val songs: List<Song> = arrayListOf()) : BaseModel(), SongListItem {

    override fun toString(): String {
        return "Set(name='$name', encore='$encore', songs=$songs)"
    }
}