package com.example.evgeny.setlist_mobile.setlists.diffs

import androidx.recyclerview.widget.DiffUtil
import com.example.evgeny.setlist_mobile.data.Artist

class ArtistDiff(val oldList: List<Artist>, val newList: List<Artist>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]==newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldArtist = oldList[oldItemPosition]
        val newArtist = newList[newItemPosition]
        return oldArtist.mbid==newArtist.mbid &&
                oldArtist.name==newArtist.name
    }
}