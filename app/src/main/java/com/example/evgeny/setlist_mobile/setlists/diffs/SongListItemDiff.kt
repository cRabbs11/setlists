package com.example.evgeny.setlist_mobile.setlists.diffs

import androidx.recyclerview.widget.DiffUtil
import com.example.evgeny.setlist_mobile.setlists.Song
import com.example.evgeny.setlist_mobile.setlists.Set
import com.example.evgeny.setlist_mobile.setlists.SongListItem

data class SongListItemDiff(val oldList: ArrayList<SongListItem>, val newList: ArrayList<SongListItem>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldSongListItem = oldList[oldItemPosition]
        val newSongListItem = newList[newItemPosition]
        if (oldSongListItem is Song && newSongListItem is Song) {
            return oldSongListItem.name == newSongListItem.name &&
                    oldSongListItem.number == newSongListItem.number
        } else if(oldSongListItem is Set && newSongListItem is Set) {
            return oldSongListItem.name == newSongListItem.name &&
                    oldSongListItem.number == newSongListItem.number &&
                    oldSongListItem.encore == newSongListItem.encore
        } else {
            return false
        }
    }
}