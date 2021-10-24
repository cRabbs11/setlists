package com.example.evgeny.setlist_mobile.utils


import android.content.Context
import android.database.DataSetObserver
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.evgeny.setlist_mobile.R
import com.example.evgeny.setlist_mobile.setlists.Setlist


class SongListAdapter(context: Context, setlist: Setlist) : ExpandableListAdapter {

    private val TAG = ArtistListAdapter::class.simpleName + " BMTH"
    private var setlist : Setlist

    private var context: Context

    init {
        Log.d(TAG, "init")
        this.context = context
        this.setlist = setlist
        Log.d(TAG, setlist.toString())
    }

    override fun registerDataSetObserver(p0: DataSetObserver?) {

    }

    override fun unregisterDataSetObserver(p0: DataSetObserver?) {

    }

    override fun getGroupCount(): Int {
        return setlist.sets.size
    }

    override fun getChildrenCount(setNum: Int): Int {
        return setlist.sets[setNum].songs.size
    }

    override fun getGroup(setNum: Int): Any {
        return setlist.sets[setNum]
    }

    override fun getChild(setNum: Int, songNum: Int): Any {
        return setlist.sets[setNum].songs[songNum]
    }

    override fun getGroupId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getChildId(p0: Int, p1: Int): Long {
        return p1.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(group: Int, b: Boolean, groupView: View?, viewGroup: ViewGroup?): View {

        val inf = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val groupView = inf.inflate(R.layout.set_group_item, null)

        val setName = groupView?.findViewById(R.id.setName) as TextView
        val set = setlist.sets.get(group)

        if (set.name!=null) {
            setName.setText(set.name)
        }

        if (set.encore!=null) {
            setName.setText(set.encore)
        }

        return groupView;
    }

    override fun getChildView(group: Int, child: Int, b: Boolean, childView: View?, viewGroup: ViewGroup?): View {

        val inf = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val childView = inf.inflate(R.layout.song_layout_item, null)

        val songName = childView.findViewById(R.id.songName) as TextView
        val tape = childView.findViewById(R.id.tape) as ImageView
        val descriptionView = childView.findViewById(R.id.description) as TextView
        val song = setlist.sets[group].songs[child]

        var cover = " ";
        var info = " ";
        var name = song.name
        var descrition = " ";
        if (song.cover!=null) {
            cover =  song.cover.name
            descrition = "$descrition ($cover song)"
        }

        if (!song.info.equals("")) {
            info = song.info
            descrition = "$descrition ($info )"
        }

        if (song.tape) {
            tape.setVisibility(View.VISIBLE)
        } else {
            name = "${song.number}. $name"
        }
        songName.setText(name)

        if (!descriptionView.equals(" ")) {
            descriptionView.setText(descrition)
        }
        return childView
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return false
    }

    override fun areAllItemsEnabled(): Boolean {
        return false
    }

    override fun isEmpty(): Boolean {
        return false
    }

    override fun onGroupExpanded(p0: Int) {

    }

    override fun onGroupCollapsed(p0: Int) {

    }

    override fun getCombinedChildId(p0: Long, p1: Long): Long {
        return p1
    }

    override fun getCombinedGroupId(p0: Long): Long {
        return p0
    }

}

