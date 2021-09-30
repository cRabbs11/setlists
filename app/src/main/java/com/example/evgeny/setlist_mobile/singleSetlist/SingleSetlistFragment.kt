package com.example.evgeny.setlist_mobile.singleSetlist


import android.os.Bundle
import android.util.Log

import android.view.*
import android.widget.*

import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.evgeny.setlist_mobile.R


import com.example.evgeny.setlist_mobile.setlists.Setlist
import com.example.evgeny.setlist_mobile.setlists.SetlistsAPI
import com.example.evgeny.setlist_mobile.utils.OnItemClickListener
import com.example.evgeny.setlist_mobile.utils.SetlistListAdapter
import com.example.evgeny.setlist_mobile.utils.SetlistsRepository
import com.example.evgeny.setlist_mobile.utils.SongListAdapter

class SingleSetlistFragment : Fragment(), OnItemClickListener<Setlist>, SingleSetlistContract.View {
    override fun showSetlist(setlist: Setlist) {
        adapter = SongListAdapter(context!!, setlist)
        expListView.setGroupIndicator(null)
        expListView.setAdapter(adapter)
        setlist.sets.forEach {
            val groupPosition = setlist.sets.indexOf(it)
            expListView.expandGroup(groupPosition)
        }
    }

    override fun updateSetlist(setlist: Setlist) {

    }


    override fun showToast(message: String) {
        Toast.makeText(context, message, LENGTH_SHORT).show()
    }

    val LOG_TAG = SingleSetlistFragment::class.java.name + " BMTH "

    lateinit var recyclerView: RecyclerView
    lateinit var presenter: SingleSetlistPresenter
    lateinit var adapter: SongListAdapter
    lateinit var expListView: ExpandableListView
    //lateinit var emptyRecyclerMessageLayout: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_setlists_new, container, false)
        initView(rootView)
        return rootView
    }

    fun initView(rootView: View ) {
        //recyclerView = rootView.findViewById(R.id.recyclerView)
        //var linearLayoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        //recyclerView!!.setLayoutManager(linearLayoutManager)
        expListView = rootView.findViewById(R.id.expListView) as ExpandableListView
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

        val setlistsAPI = SetlistsAPI()

        val setlistsRepository = SetlistsRepository

        presenter = SingleSetlistPresenter(setlistsRepository)
        presenter.attachView(this)
        presenter.viewIsReady()
    }

    override fun onItemClick(t: Setlist) {

    }
}