package com.example.evgeny.setlist_mobile.artistSearch


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


import com.example.evgeny.setlist_mobile.setlists.Artist
import com.example.evgeny.setlist_mobile.setlistsSearch.SetlistsSearchFragment
import com.example.evgeny.setlist_mobile.utils.ArtistListAdapter
import com.example.evgeny.setlist_mobile.utils.OnItemClickListener
import com.example.evgeny.setlist_mobile.utils.SetlistsRepository

class ArtistSearchFragment : Fragment(), OnItemClickListener<Artist>, ArtistSearchContract.View {

    override fun showArtistList(list: List<Artist>) {
        Log.d(TAG, "artists count: ${list.size} ")
        adapter.clearItems()
        adapter.setItems(list)

        if (list.isEmpty()) {
            //emptyRecyclerMessageLayout.visibility= View.VISIBLE
        } else {
            //emptyRecyclerMessageLayout.visibility= View.GONE
        }
    }

    override fun updateArtistList(words: List<Artist>) {

    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, LENGTH_SHORT).show()
    }

    val TAG = ArtistSearchFragment::class.java.name + " BMTH "

    lateinit var recyclerView: RecyclerView
    lateinit var presenter: ArtistSearchPresenter
    lateinit var adapter: ArtistListAdapter
    //lateinit var emptyRecyclerMessageLayout: TextView
    lateinit var editSearch: EditText
    lateinit var btnSearch : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_search, container, false)
        initView(rootView)
        return rootView
    }

    fun initView(rootView: View ) {
        recyclerView = rootView.findViewById(R.id.recyclerView)
        var linearLayoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView!!.setLayoutManager(linearLayoutManager)
        //emptyRecyclerMessageLayout = rootView.findViewById(R.id.emptySearchText)
        editSearch = rootView.findViewById(R.id.edit_search)
        btnSearch = rootView.findViewById(R.id.btn_search)
        btnSearch.setOnClickListener {
            presenter.onSearchArtistClicked(editSearch.text.toString())
        }
        Log.d(TAG, " запустили")

        val setlistsRepository = SetlistsRepository
        adapter = ArtistListAdapter(this)
        recyclerView!!.setAdapter(adapter)
        var dividerItemDecoration = DividerItemDecoration(recyclerView!!.getContext(),
            LinearLayoutManager.VERTICAL)
        recyclerView!!.addItemDecoration(dividerItemDecoration)

        presenter = ArtistSearchPresenter(setlistsRepository)
        presenter.attachView(this)
        presenter.viewIsReady()
    }

    override fun openSetlists() {
        Log.d(TAG, "openSetlists")
        var setlistsSearchFragment = SetlistsSearchFragment()

        var fragmentManager = getFragmentManager()
        var fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, setlistsSearchFragment)
        fragmentTransaction.addToBackStack("")
        fragmentTransaction.commit()


        //searchSetlistsFragment = new SearchSetlistsFragment(artist);
        //ftrans = getActivity().getSupportFragmentManager().beginTransaction();
        //ftrans.replace(R.id.fragment_container, searchSetlistsFragment);
        //ftrans.addToBackStack("");
        //ftrans.commit();
    }

    override fun onItemClick(t: Artist) {
        Log.d(TAG, "onItemClick")
        presenter.onListItemClicked(t)
    }
}