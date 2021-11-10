package com.example.evgeny.setlist_mobile.singleSetlist


import android.os.Bundle
import android.util.Log

import android.view.*
import android.widget.*

import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.evgeny.setlist_mobile.R
import com.example.evgeny.setlist_mobile.databinding.FragmentSingleSetlistBinding
import com.example.evgeny.setlist_mobile.setlistOnMap.SetlistOnMapFragment


import com.example.evgeny.setlist_mobile.setlists.Setlist
import com.example.evgeny.setlist_mobile.setlists.SetlistsAPI
import com.example.evgeny.setlist_mobile.setlistsSearch.SetlistsSearchFragment
import com.example.evgeny.setlist_mobile.utils.OnItemClickListener
import com.example.evgeny.setlist_mobile.utils.SetlistsRepository
import com.example.evgeny.setlist_mobile.utils.SongListAdapter

class SingleSetlistFragment : Fragment(), OnItemClickListener<Setlist>, SingleSetlistContract.View {
    override fun showSetlist(setlist: Setlist) {
        adapter = SongListAdapter(requireContext(), setlist)
        binding.expListView.setGroupIndicator(null)
        binding.expListView.setAdapter(adapter)
        setlist.sets.forEach {
            val groupPosition = setlist.sets.indexOf(it)
            binding.expListView.expandGroup(groupPosition)
        }

        binding.artistName.text = setlist.artist?.name
        val placeNameText = setlist.venue?.name + setlist.venue?.city?.name
        binding.placeName.text = placeNameText
    }

    override fun updateSetlist(setlist: Setlist) {

    }


    override fun showToast(message: String) {
        Toast.makeText(context, message, LENGTH_SHORT).show()
    }

    override fun openMap() {
        var mapFragment = SetlistOnMapFragment()

        var fragmentManager = getFragmentManager()
        var fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, mapFragment)
        fragmentTransaction.addToBackStack("")
        fragmentTransaction.commit()
    }

    val LOG_TAG = SingleSetlistFragment::class.java.name + " BMTH "

    lateinit var presenter: SingleSetlistPresenter
    lateinit var adapter: SongListAdapter
    lateinit var binding: FragmentSingleSetlistBinding
    //lateinit var emptyRecyclerMessageLayout: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSingleSetlistBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    fun initView() {
        //var linearLayoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        binding.toMapView.setOnClickListener {
            presenter.onMapClicked()
        }

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

        Log.d(LOG_TAG, " запустили")

        val setlistsRepository = SetlistsRepository

        presenter = SingleSetlistPresenter(setlistsRepository)
        presenter.attachView(this)
        presenter.viewIsReady()
    }

    override fun onItemClick(t: Setlist) {

    }
}