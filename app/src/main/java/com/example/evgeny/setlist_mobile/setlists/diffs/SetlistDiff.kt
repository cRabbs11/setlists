package com.example.evgeny.setlist_mobile.setlists.diffs

import androidx.recyclerview.widget.DiffUtil
import com.example.evgeny.setlist_mobile.data.entity.Setlist

class SetlistDiff(val oldList: List<Setlist>, val newList: List<Setlist>): DiffUtil.Callback() {
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
        val oldSetlist = oldList[oldItemPosition]
        val newSetlist = newList[newItemPosition]
        val isSame = oldSetlist.artist==newSetlist.artist &&
                oldSetlist.tour==newSetlist.tour &&
                oldSetlist.eventDate==newSetlist.eventDate
        return isSame
    }
}