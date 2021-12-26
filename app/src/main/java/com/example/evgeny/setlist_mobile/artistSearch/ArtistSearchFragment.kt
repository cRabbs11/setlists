package com.example.evgeny.setlist_mobile.artistSearch

import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log

import android.view.*
import android.view.animation.AnimationUtils
import android.widget.*

import android.widget.Toast.LENGTH_SHORT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.evgeny.setlist_mobile.MainActivity
import com.example.evgeny.setlist_mobile.R
import com.example.evgeny.setlist_mobile.animators.ItemListAnimator
import com.example.evgeny.setlist_mobile.databinding.FragmentSearchConstraintBinding

import com.example.evgeny.setlist_mobile.setlists.Artist
import com.example.evgeny.setlist_mobile.setlists.diffs.ArtistDiff
import com.example.evgeny.setlist_mobile.setlistsSearch.SetlistsSearchFragment
import com.example.evgeny.setlist_mobile.utils.*

class ArtistSearchFragment : Fragment(), ArtistSearchContract.View, OnItemClickListener<Artist> {

    override fun showArtistList(list: List<Artist>) {
        Log.d(TAG, "artists count: ${list.size} ")
        if (list.isNotEmpty()) {
            val diff = ArtistDiff(adapter.artists, list as ArrayList<Artist>)
            val diffResult = DiffUtil.calculateDiff(diff)
            adapter.clearItems()
            adapter.setItems(list)
            diffResult.dispatchUpdatesTo(adapter)
            //emptyRecyclerMessageLayout.visibility= View.GONE
        } else {
            //emptyRecyclerMessageLayout.visibility= View.VISIBLE
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
    lateinit var suggestionAdapter: SimpleCursorAdapter
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
        binding.recyclerView.itemAnimator = ItemListAnimator(requireContext())
        //val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        //binding.recyclerView.layoutManager = linearLayoutManager

        //val anim = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_slide_from_top_left)
        //binding.recyclerView.layoutAnimation=anim
        //binding.recyclerView.scheduleLayoutAnimation()

        val from = arrayOf("items")
        var to = intArrayOf(android.R.id.text1)

        suggestionAdapter = SimpleCursorAdapter(requireContext(),
                android.R.layout.simple_list_item_1,
        null,
        from,
        to,
        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)

        binding.searchView.suggestionsAdapter = suggestionAdapter

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                presenter.onSearchArtistClicked(binding.searchView.query.toString())
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!=null) populateAdapter(newText)
                return true
            }
        })

        binding.searchView.setOnSuggestionListener(object: SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return true
            }

            override fun onSuggestionClick(position: Int): Boolean {
                val cursor = suggestionAdapter.getItem(position) as Cursor
                val txt = cursor.getString(cursor.getColumnIndex("items"))
                binding.searchView.setQuery(txt, true)
                binding.searchView.clearFocus()
                return true
            }

        })

        val setlistsRepository = SetlistsRepository
        val searchHistoryHelper = SearchHistoryHelper(requireContext())
        adapter = ArtistListAdapter(this)
        binding.recyclerView.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(binding.recyclerView.context, LinearLayoutManager.VERTICAL)
        binding.recyclerView.addItemDecoration(dividerItemDecoration)

        presenter = ArtistSearchPresenter(setlistsRepository, searchHistoryHelper)
        presenter.attachView(this)
        presenter.viewIsReady()
        //presenter.onSearchArtistClicked("bring me the")
    }

    override fun openSetlists() {
        (activity as MainActivity).openSetlistsSearchFragment()
    }

    override fun onItemClick(t: Artist) {
        Log.d(TAG, "onItemClick")
        presenter.onListItemClicked(t)
    }

    private fun populateAdapter(query: String) {
        val c = MatrixCursor(arrayOf(BaseColumns._ID, "items"))
        val suggestions = presenter.getHistorySearchList()
        for (i in suggestions.indices) {
            if (suggestions[i].toLowerCase().contains(query.toLowerCase())) c.addRow(arrayOf(i, suggestions[i]))
        }

        suggestionAdapter.changeCursor(c)
    }
}