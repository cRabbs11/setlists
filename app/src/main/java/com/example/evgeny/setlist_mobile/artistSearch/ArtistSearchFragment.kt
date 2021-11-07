package com.example.evgeny.setlist_mobile.artistSearch

import android.os.Bundle
import android.util.Log

import android.view.*
import android.view.animation.AnimationUtils
import android.widget.*

import android.widget.Toast.LENGTH_SHORT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.evgeny.setlist_mobile.R
import com.example.evgeny.setlist_mobile.databinding.FragmentSearchConstraintBinding

import com.example.evgeny.setlist_mobile.setlists.Artist
import com.example.evgeny.setlist_mobile.setlists.diffs.ArtistDiff
import com.example.evgeny.setlist_mobile.setlistsSearch.SetlistsSearchFragment
import com.example.evgeny.setlist_mobile.utils.*

class ArtistSearchFragment : Fragment(), ArtistSearchContract.View, OnItemClickListener<Artist> {

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

    val TAG = ArtistSearchFragment::class.java.name + "BMTH"

    lateinit var presenter: ArtistSearchPresenter
    lateinit var adapter: ArtistListAdapter
    lateinit var binding: FragmentSearchConstraintBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchConstraintBinding.inflate(inflater, container, false)
        val rootView = binding.root
        initView(rootView)
        return rootView
    }

    fun initView(rootView: View ) {
        Log.d(TAG, " запустили")
        //val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        //binding.recyclerView.layoutManager = linearLayoutManager

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                presenter.onSearchArtistClicked(binding.searchView.query.toString())
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        val setlistsRepository = SetlistsRepository
        adapter = ArtistListAdapter(this)
        binding.recyclerView.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(binding.recyclerView.context, LinearLayoutManager.VERTICAL)
        binding.recyclerView.addItemDecoration(dividerItemDecoration)

        presenter = ArtistSearchPresenter(setlistsRepository)
        presenter.attachView(this)
        presenter.viewIsReady()
    }

    override fun openSetlists() {
        Log.d(TAG, "openSetlists")
        var setlistsSearchFragment = SetlistsSearchFragment()
        //var mapFragment = MapOnSetlistFragment()

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