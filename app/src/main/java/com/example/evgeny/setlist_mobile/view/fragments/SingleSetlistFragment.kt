package com.example.evgeny.setlist_mobile.view.fragments


import android.os.Bundle
import android.util.Log

import android.view.*
import android.widget.*

import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.transition.TransitionInflater
import com.example.evgeny.setlist_mobile.R
import com.example.evgeny.setlist_mobile.animators.ItemListAnimator
import com.example.evgeny.setlist_mobile.databinding.FragmentSetlistBinding
import com.example.evgeny.setlist_mobile.setlistOnMap.SetlistOnMapFragment
import com.example.evgeny.setlist_mobile.data.entity.Setlist
import com.example.evgeny.setlist_mobile.setlists.SongListItem
import com.example.evgeny.setlist_mobile.setlists.diffs.SongListItemDiff
import com.example.evgeny.setlist_mobile.utils.Constants
import com.example.evgeny.setlist_mobile.utils.SongListItemAdapter
import com.example.evgeny.setlist_mobile.viewmodel.SingleSetlistFragmentViewModel
import com.example.evgeny.setlist_mobile.viewmodel.factory
import java.text.ParseException
import java.text.SimpleDateFormat

class SingleSetlistFragment : Fragment() {

    private fun updateRecyclerView(songList: List<SongListItem>) {
        val diff = SongListItemDiff(adapter.songList, songList)
        val diffResult = DiffUtil.calculateDiff(diff)
        adapter.songList.clear()
        adapter.songList.addAll(songList)
        diffResult.dispatchUpdatesTo(adapter)
    }

    private fun showSetlistInfo(setlist: Setlist) {
        binding.setlistInfoLayout.artistName.text = setlist.artist?.name
        val placeNameText = "at ${setlist.venue?.name}, ${setlist.venue?.city?.name}"
        val tour = "Tour: ${setlist.tour?.name}"
        binding.setlistInfoLayout.placeName.text = placeNameText
        binding.setlistInfoLayout.tourName.text = tour
        //binding.toolbar.title = setlist.artist?.name

        val dt = SimpleDateFormat("dd-mm-yyyy")

        try {
            val date = dt.parse(setlist.eventDate);
            val month = SimpleDateFormat("mm")
            val sMonth = identMonth(month.format(date))
            val day = SimpleDateFormat("dd")
            val sDay = day.format(date)
            val year = SimpleDateFormat("yyyy")
            val sYear = year.format(date)

            binding.setlistInfoLayout.dateLayout.month.setText(sMonth)
            binding.setlistInfoLayout.dateLayout.day.setText(sDay)
            binding.setlistInfoLayout.dateLayout.year.setText(sYear)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    private fun identMonth(format: String): String? {
        return when (format) {
            "01" -> requireContext().resources.getString(R.string.jan)
            "02" -> requireContext().resources.getString(R.string.feb)
            "03" -> requireContext().resources.getString(R.string.mar)
            "04" -> requireContext().resources.getString(R.string.apr)
            "05" -> requireContext().resources.getString(R.string.may)
            "06" -> requireContext().resources.getString(R.string.jun)
            "07" -> requireContext().resources.getString(R.string.jul)
            "08" -> requireContext().resources.getString(R.string.aug)
            "09" -> requireContext().resources.getString(R.string.sep)
            "10" -> requireContext().resources.getString(R.string.oct)
            "11" -> requireContext().resources.getString(R.string.nov)
            "12" -> requireContext().resources.getString(R.string.nov)
            else -> requireContext().resources.getString(R.string.jan)
        }
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, LENGTH_SHORT).show()
    }

    private fun openMap() {
        var mapFragment = SetlistOnMapFragment()

        var fragmentManager = getFragmentManager()
        var fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, mapFragment)
        fragmentTransaction.addToBackStack("")
        fragmentTransaction.commit()
    }

    val TAG = SingleSetlistFragment::class.java.name + " BMTH "

    lateinit var adapter: SongListItemAdapter
    lateinit var binding: FragmentSetlistBinding
    //lateinit var emptyRecyclerMessageLayout: TextView
    private val viewModel: SingleSetlistFragmentViewModel by viewModels{ factory(setlist = arguments?.get(Constants.KEY_BUNDLE_SETLIST) as Setlist)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSetlistBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.songListItemLiveData.observe(viewLifecycleOwner) {
            updateRecyclerView(it)
        }

        viewModel.setlistInfoLiveData.observe(viewLifecycleOwner) {
            showSetlistInfo(it)
        }

        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)

    }

    fun initView() {
        adapter = SongListItemAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.itemAnimator = ItemListAnimator(requireContext())

        binding.setlistInfoLayout.dateLayout.dateLayout.transitionName = arguments?.getString("transition")
        //var linearLayoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        //binding.toMapView.setOnClickListener {
        //    presenter.onMapClicked()
        //}

        //for (Set set: setlist.getSets()) {
        //    int groupPosition = setlist.getSets().indexOf(set);
        //    expListView.expandGroup(groupPosition);
        //}
//
        //expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
        //    @Override
        //    public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
        //        return true;
        //    }
        //})

        Log.d(TAG, " запустили")
    }
}