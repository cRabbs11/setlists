package com.example.evgeny.setlist_mobile.setlists.diffs

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.example.evgeny.setlist_mobile.setlists.Setlist

class SetlistDiff(val oldList: ArrayList<Setlist>, val newList: ArrayList<Setlist>): DiffUtil.Callback() {
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
        Log.d("BMTH", "eventDate=${newSetlist.eventDate} same is $isSame")
        return isSame
    }
}