package com.kochkov.evgeny.setlist_mobile.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kochkov.evgeny.setlist_mobile.R
import com.kochkov.evgeny.setlist_mobile.setlists.*
import com.kochkov.evgeny.setlist_mobile.data.entity.Set
import com.kochkov.evgeny.setlist_mobile.data.entity.Song

const val SONGLIST_ITEM_SONG = 0
const val SONGLIST_ITEM_SET = 1

class SongListItemAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = ArtistListAdapter::class.simpleName + " BMTH"
    val songList = ArrayList<SongListItem>()
    private val SET_ENCORE_NAME = "Encore: "

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if(viewType == SONGLIST_ITEM_SONG) {
            return SongHolder(inflater.inflate(R.layout.song_layout_item, parent, false))
        } else {
            return SetHolder(inflater.inflate(R.layout.set_layout_item, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (songList[position] is Song) {
            SONGLIST_ITEM_SONG
        } else {
            SONGLIST_ITEM_SET
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val songListItem = songList[position]
        if (songListItem is Song) {
            holder as SongHolder

            if (songListItem.tape) {
                holder.tape.setVisibility(View.VISIBLE)
                holder.songName.text= songListItem.name
            } else {
                holder.tape.setVisibility(View.GONE)
                val songName = "${songListItem.number}. ${songListItem.name}"
                holder.songName.text= songName
            }

            if (songListItem.with!=null) {
                holder.cover.visibility = View.VISIBLE
                val cover = "(with ${songListItem.with?.name})"
                holder.cover.text = cover
            } else {
                holder.cover.visibility = View.GONE
            }

            if (songListItem.info!="") {
                holder.descr.visibility=View.VISIBLE
                holder.descr.text = songListItem.info
            } else {
                holder.descr.visibility=View.GONE
            }


        } else if(songListItem is Set) {
            holder as SetHolder
            val setName = if (songListItem.name!="") {
                songListItem.name
            } else if (songListItem.encore==1) {
                SET_ENCORE_NAME
            } else {
                "$SET_ENCORE_NAME ${songListItem.encore}"
            }
            holder.setName.text = setName
        }
    }

    override fun getItemCount(): Int {
        return songList.size
    }

}

class SetHolder(view: View): RecyclerView.ViewHolder(view) {

    val setName: TextView

    init {
        setName = itemView.findViewById(R.id.setName)
    }
}

class SongHolder(view: View): RecyclerView.ViewHolder(view) {

    val songName: TextView
    val tape: ImageView
    val descr: TextView
    val cover: TextView

    init {
        songName = itemView.findViewById(R.id.songName)
        tape = itemView.findViewById(R.id.tape)
        descr = itemView.findViewById(R.id.description)
        cover = itemView.findViewById(R.id.cover)
    }
}
