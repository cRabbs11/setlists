package com.example.evgeny.setlist_mobile.utils


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.evgeny.setlist_mobile.data.entity.Setlist
import com.example.evgeny.setlist_mobile.databinding.ItemSetlistLayoutBinding
import com.example.evgeny.setlist_mobile.view.holders.SetlistHolder

class SetlistListAdapter(clickListener: OnSharedTransitionClickListener<Setlist>) : RecyclerView.Adapter<SetlistHolder>() {

    private val TAG = ArtistListAdapter::class.simpleName + " BMTH"
    private var clickListener: OnSharedTransitionClickListener<Setlist>
    val setlists = arrayListOf<Setlist>()

    private lateinit var context: Context

    init {
        //Log.d(TAG, "init")
        this.clickListener=clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetlistHolder {
        val binding = ItemSetlistLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SetlistHolder(binding)
    }

    override fun getItemCount(): Int {
        return setlists.size
    }

    fun setItems(list: List<Setlist>) {
        clearItems()
        list.forEach {
            setlists.add(it)
        }
    }

    fun addItem(setlist: Setlist) {
        setlists.add(setlist)
    }

    fun addUniqItems(list: List<Setlist>) {
        list.forEach {
            if(!setlists.contains(it)) {
                setlists.add(it)
                //Log.d(TAG, "add: ${it.eventDate}")
            }
        }
    }

    fun clearItems() {
        setlists.clear()
    }

    override fun onBindViewHolder(holder: SetlistHolder, position: Int) {
        holder.bind(setlists.get(position), clickListener)
    }
}

interface OnSharedTransitionClickListener<T> {
    fun onItemClick(t: T, sharedView: View)
}