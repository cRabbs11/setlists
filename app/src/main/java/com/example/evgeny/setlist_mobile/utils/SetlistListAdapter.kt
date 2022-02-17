package com.example.evgeny.setlist_mobile.utils


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.evgeny.setlist_mobile.R
import com.example.evgeny.setlist_mobile.setlists.Setlist
import java.text.ParseException
import java.text.SimpleDateFormat

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
        //Log.d(TAG, "onCreateViewHolder")
        val inflater = LayoutInflater.from(parent.getContext())
        context = parent.context
        val view = inflater.inflate(R.layout.item_setlist_layout, parent, false)
        return SetlistHolder(view)
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
        //Log.d(TAG, "onBindViewHolder")
        val setlist = setlists.get(position)
        val name = setlist.artist?.name
        val eventDate = setlist.eventDate

        val dt = SimpleDateFormat("dd-mm-yyyy")

        try {
            val date = dt.parse(eventDate);
            val month = SimpleDateFormat("mm")
            val sMonth = identMonth(month.format(date))
            val day = SimpleDateFormat("dd")
            val sDay = day.format(date)
            val year = SimpleDateFormat("yyyy")
            val sYear = year.format(date)

            holder.month.setText(sMonth)
            holder.day.setText(sDay)
            holder.year.setText(sYear)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        var tourName = " not tour name"
        if (setlist.tour?.name!=null) {
            tourName = setlist.tour.name
        } else {
            holder.layout_tour.setVisibility(View.GONE);
        }
        val tour = tourName
        val venue = setlist.venue?.name + ": " + setlist.venue?.city?.name + ", " + setlist.venue?.city?.country?.name
        val header = "$name at: $venue"
        holder.artist.setText(name)
        holder.dateLayout.transitionName = "trans${position}"

        holder.setlist.setOnClickListener {
            clickListener.onItemClick(setlist, holder.dateLayout)
        }
        holder.tour.setText(tour);
        holder.venue.setText(venue);
    }

    private fun identMonth(format: String): String? {
        return when (format) {
            "01" -> context.resources.getString(R.string.jan)
            "02" -> context.resources.getString(R.string.feb)
            "03" -> context.resources.getString(R.string.mar)
            "04" -> context.resources.getString(R.string.apr)
            "05" -> context.resources.getString(R.string.may)
            "06" -> context.resources.getString(R.string.jun)
            "07" -> context.resources.getString(R.string.jul)
            "08" -> context.resources.getString(R.string.aug)
            "09" -> context.resources.getString(R.string.sep)
            "10" -> context.resources.getString(R.string.oct)
            "11" -> context.resources.getString(R.string.nov)
            "12" -> context.resources.getString(R.string.nov)
            else -> context.resources.getString(R.string.jan)
        }
    }
}

interface OnSharedTransitionClickListener<T> {
    fun onItemClick(t: T, sharedView: View)
}

class SetlistHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var month: TextView
    var day: TextView
    var year: TextView
    var artist: TextView
    var tour: TextView
    var venue: TextView

    var setlistView: View
    var setlist: LinearLayout
    var layout_tour: LinearLayout
    val dateLayout: ConstraintLayout

    init {
        artist = itemView.findViewById(R.id.artist)
        tour = itemView.findViewById(R.id.tour)
        venue = itemView.findViewById(R.id.venue)
        setlist = itemView.findViewById(R.id.setlist)
        layout_tour = itemView.findViewById(R.id.layout_tour)

        dateLayout = itemView.findViewById(R.id.date_layout)
        month = dateLayout.findViewById(R.id.month)
        day = dateLayout.findViewById(R.id.day)
        year = dateLayout.findViewById(R.id.year)

        setlistView = itemView
    }
}