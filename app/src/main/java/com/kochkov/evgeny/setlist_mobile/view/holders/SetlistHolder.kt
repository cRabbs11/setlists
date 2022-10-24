package com.kochkov.evgeny.setlist_mobile.view.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kochkov.evgeny.setlist_mobile.R
import com.kochkov.evgeny.setlist_mobile.data.entity.Setlist
import com.kochkov.evgeny.setlist_mobile.databinding.ItemSetlistLayoutBinding
import com.kochkov.evgeny.setlist_mobile.utils.OnSharedTransitionClickListener
import java.text.SimpleDateFormat
import java.util.*

class SetlistHolder(private val binding: ItemSetlistLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(setlist: Setlist, clickListener: OnSharedTransitionClickListener<Setlist>) {
        val name = setlist.artist?.name

        val dt = SimpleDateFormat("dd-mm-yyyy", Locale.getDefault())
        val date = dt.parse(setlist.eventDate)
        date?.let {
            val month = SimpleDateFormat("mm", Locale.getDefault())
            val sMonth = identMonth(month.format(date))
            val day = SimpleDateFormat("dd", Locale.getDefault())
            val sDay = day.format(date)
            val year = SimpleDateFormat("yyyy", Locale.getDefault())
            val sYear = year.format(date)

            binding.concertDateView.setDate(sDay, sMonth!!, sYear)
        }

        var tourName = " not tour name"
        if (setlist.tour?.name!=null) {
            tourName = setlist.tour!!.name
        } else {
            binding.tourName.visibility = View.GONE
        }
        val tour = tourName
        val venue = setlist.venue?.name + ": " + setlist.venue?.city?.name + ", " + setlist.venue?.city?.country?.name
        val header = "$name at: $venue"
        binding.artistName.text = name
        binding.concertDateView.transitionName = "trans${position}"

        binding.root.setOnClickListener {
            clickListener.onItemClick(setlist, binding.concertDateView)
        }

        binding.tourName.text = tour
        binding.venueName.text = venue

        binding.concertDateView.requestLayout()
    }

    private fun identMonth(format: String): String? {
        return when (format) {
            "01" -> binding.root.context.resources.getString(R.string.jan)
            "02" -> binding.root.context.resources.getString(R.string.feb)
            "03" -> binding.root.context.resources.getString(R.string.mar)
            "04" -> binding.root.context.resources.getString(R.string.apr)
            "05" -> binding.root.context.resources.getString(R.string.may)
            "06" -> binding.root.context.resources.getString(R.string.jun)
            "07" -> binding.root.context.resources.getString(R.string.jul)
            "08" -> binding.root.context.resources.getString(R.string.aug)
            "09" -> binding.root.context.resources.getString(R.string.sep)
            "10" -> binding.root.context.resources.getString(R.string.oct)
            "11" -> binding.root.context.resources.getString(R.string.nov)
            "12" -> binding.root.context.resources.getString(R.string.nov)
            else -> binding.root.context.resources.getString(R.string.jan)
        }
    }
}