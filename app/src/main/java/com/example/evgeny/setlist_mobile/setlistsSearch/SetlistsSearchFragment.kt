package com.example.evgeny.setlist_mobile.setlistsSearch


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
import com.example.evgeny.setlist_mobile.singleSetlist.SingleSetlistFragment
import com.example.evgeny.setlist_mobile.utils.OnItemClickListener
import com.example.evgeny.setlist_mobile.utils.SetlistListAdapter
import com.example.evgeny.setlist_mobile.utils.SetlistsRepository

class SetlistsSearchFragment : Fragment(), OnItemClickListener<Setlist>, SetlistsSearchContract.View {

    override fun showSetlistList(list: List<Setlist>) {
        Log.d(TAG, "artists count: ${list.size} ")
        adapter.clearItems()
        adapter.setItems(list)

        if (list.isEmpty()) {
            //emptyRecyclerMessageLayout.visibility= View.VISIBLE
        } else {
            //emptyRecyclerMessageLayout.visibility= View.GONE
        }
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, LENGTH_SHORT).show()
    }

    override fun openSetlist() {
        Log.d(TAG, "openSetlist")
        var singleSetlistFragment = SingleSetlistFragment()

        var fragmentManager = getFragmentManager()
        var fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, singleSetlistFragment)
        fragmentTransaction.addToBackStack("")
        fragmentTransaction.commit()
    }

    val TAG = SetlistsSearchFragment::class.java.name + " BMTH "

    lateinit var recyclerView: RecyclerView
    lateinit var presenter: SetlistsSearchPresenter
    lateinit var adapter: SetlistListAdapter
    //lateinit var emptyRecyclerMessageLayout: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_setlists, container, false)
        initView(rootView)
        return rootView
    }

    fun initView(rootView: View ) {
        recyclerView = rootView.findViewById(R.id.recyclerView)
        var linearLayoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView!!.setLayoutManager(linearLayoutManager)
        //emptyRecyclerMessageLayout = rootView.findViewById(R.id.emptySearchText)
        Log.d(TAG, " запустили")

        val setlistsAPI = SetlistsAPI()

        val setlistsRepository = SetlistsRepository
        adapter = SetlistListAdapter(this)
        recyclerView!!.setAdapter(adapter)
        var dividerItemDecoration = DividerItemDecoration(recyclerView!!.getContext(),
            LinearLayoutManager.VERTICAL)
        recyclerView!!.addItemDecoration(dividerItemDecoration)

        presenter = SetlistsSearchPresenter(setlistsRepository)
        presenter.attachView(this)
        presenter.viewIsReady()
    }

    override fun updateSetlistList(words: List<Setlist>) {

    }

    override fun onItemClick(t: Setlist) {
        presenter.onListItemClicked(t)
    }
}