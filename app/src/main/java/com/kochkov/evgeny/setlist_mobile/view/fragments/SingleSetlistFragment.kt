package com.kochkov.evgeny.setlist_mobile.view.fragments


import android.os.Bundle

import android.view.*
import android.widget.*

import android.widget.Toast.LENGTH_SHORT
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.transition.TransitionInflater
import com.kochkov.evgeny.setlist_mobile.R
import com.kochkov.evgeny.setlist_mobile.animators.ItemListAnimator
import com.kochkov.evgeny.setlist_mobile.data.entity.Setlist
import com.kochkov.evgeny.setlist_mobile.databinding.FragmentSetlistBinding
import com.kochkov.evgeny.setlist_mobile.setlists.SongListItem
import com.kochkov.evgeny.setlist_mobile.setlists.diffs.SongListItemDiff
import com.kochkov.evgeny.setlist_mobile.ui.SetlistScreen
import com.kochkov.evgeny.setlist_mobile.ui.SetlistsComposePaging
import com.kochkov.evgeny.setlist_mobile.utils.Constants
import com.kochkov.evgeny.setlist_mobile.utils.Constants.KEY_BUNDLE_TRANSITION
import com.kochkov.evgeny.setlist_mobile.utils.OnItemClickListener
import com.kochkov.evgeny.setlist_mobile.utils.SongListItemAdapter
import com.kochkov.evgeny.setlist_mobile.view.activities.MainActivity
import com.kochkov.evgeny.setlist_mobile.viewmodel.SingleSetlistFragmentViewModel
import com.kochkov.evgeny.setlist_mobile.viewmodel.factory
import java.text.SimpleDateFormat
import java.util.*

class SingleSetlistFragment : Fragment() {

    private fun updateRecyclerView(songList: List<SongListItem>) {
        val diff = SongListItemDiff(adapter.songList, songList)
        val diffResult = DiffUtil.calculateDiff(diff)
        adapter.songList.clear()
        adapter.songList.addAll(songList)
        diffResult.dispatchUpdatesTo(adapter)
    }

    private fun showSetlistInfo(setlist: Setlist) {
        var tourName = " not tour name"
        if (setlist.tour?.name!=null) {
            tourName = setlist.tour!!.name
        } else {
            binding.setlistInfoLayout.tourName.visibility = View.GONE
        }
        val tour = tourName
        val venue = setlist.venue?.name + ": " + setlist.venue?.city?.name + ", " + setlist.venue?.city?.country?.name
        binding.setlistInfoLayout.artistName.text = setlist.artist?.name
        binding.setlistInfoLayout.tourName.text = tour
        binding.setlistInfoLayout.venueName.text = venue

        val dt = SimpleDateFormat("dd-mm-yyyy", Locale.getDefault())
        val date = dt.parse(setlist.eventDate)
        date?.let {
            val month = SimpleDateFormat("mm", Locale.getDefault())
            val sMonth = identMonth(month.format(date))
            val day = SimpleDateFormat("dd", Locale.getDefault())
            val sDay = day.format(date)
            val year = SimpleDateFormat("yyyy", Locale.getDefault())
            val sYear = year.format(date)

            binding.setlistInfoLayout.concertDateView.setDate(sDay, sMonth!!, sYear)
        }
    }

    private fun identMonth(format: String): String {
        return when (format) {
            "01" -> getString(R.string.jan)
            "02" -> getString(R.string.feb)
            "03" -> getString(R.string.mar)
            "04" -> getString(R.string.apr)
            "05" -> getString(R.string.may)
            "06" -> getString(R.string.jun)
            "07" -> getString(R.string.jul)
            "08" -> getString(R.string.aug)
            "09" -> getString(R.string.sep)
            "10" -> getString(R.string.oct)
            "11" -> getString(R.string.nov)
            "12" -> getString(R.string.nov)
            else -> getString(R.string.jan)
        }
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, LENGTH_SHORT).show()
    }

    val TAG = SingleSetlistFragment::class.java.name + " BMTH "

    lateinit var adapter: SongListItemAdapter
    lateinit var binding: FragmentSetlistBinding
    private val viewModel: SingleSetlistFragmentViewModel by viewModels{ factory(setlist = arguments?.get(Constants.KEY_BUNDLE_SETLIST) as Setlist)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //binding = FragmentSetlistBinding.inflate(inflater, container, false)
        //initView()
        //return binding.root
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                SetlistScreen(viewModel = viewModel, onMapClickListener = object: OnItemClickListener<Setlist>{
                    override fun onItemClick(setlist: Setlist) { (activity as MainActivity).openMapFragment(setlist) }
                })
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //(activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //viewModel.songListItemLiveData.observe(viewLifecycleOwner) {
        //    updateRecyclerView(it)
        //}
//
        //viewModel.setlistInfoLiveData.observe(viewLifecycleOwner) {
        //    showSetlistInfo(it)
        //}
//
        //sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
//
        //(activity as MainActivity).supportActionBar?.let {
        //    it.title = (arguments?.get(Constants.KEY_BUNDLE_SETLIST) as Setlist).tour?.name?: getString(R.string.title_single_setlist)
        //}
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                (activity as MainActivity).closeFragment()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun initView() {
        adapter = SongListItemAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.itemAnimator = ItemListAnimator(requireContext())
        binding.setlistInfoLayout.concertDateView.transitionName = arguments?.getString(KEY_BUNDLE_TRANSITION)

        binding.fabToMap.setOnClickListener { view ->
            val setlist = viewModel.getSetlist()
            if (setlist.venue!=null) {
                //(activity as MainActivity).openMapFragment(venue)
                (activity as MainActivity).openMapFragment(setlist)
            } else {
                showToast(Constants.VENUE_DATA_NOT_FOUND)
            }
        }
    }
}