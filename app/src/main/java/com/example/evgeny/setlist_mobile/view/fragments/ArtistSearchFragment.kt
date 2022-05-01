package com.example.evgeny.setlist_mobile.view.fragments

import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log

import android.view.*
import android.widget.*

import android.widget.Toast.LENGTH_SHORT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.evgeny.setlist_mobile.view.activities.MainActivity
import com.example.evgeny.setlist_mobile.animators.ItemListAnimator
import com.example.evgeny.setlist_mobile.data.Artist
import com.example.evgeny.setlist_mobile.databinding.FragmentSearchConstraintBinding

import com.example.evgeny.setlist_mobile.setlists.diffs.ArtistDiff
import com.example.evgeny.setlist_mobile.utils.*
import com.example.evgeny.setlist_mobile.viewmodel.ArtistSearchFragmentViewModel

class ArtistSearchFragment : Fragment() {

    val TAG = ArtistSearchFragment::class.java.name + "BMTH"

    lateinit var adapter: ArtistListAdapter
    lateinit var suggestionAdapter: SimpleCursorAdapter
    lateinit var binding: FragmentSearchConstraintBinding

    private val viewModel: ArtistSearchFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.artistsLiveData.observe(viewLifecycleOwner, {
            updateRecyclerView(it)
        })

        viewModel.selectedArtistLiveData.observe(viewLifecycleOwner, {
            (activity as MainActivity).openSetlistsSearchFragment()
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchConstraintBinding.inflate(inflater, container, false)
        val rootView = binding.root
        initView()
        return rootView
    }

    fun initView() {
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
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.searchArtist(query)
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

        adapter = ArtistListAdapter(object: OnItemClickListener<Artist> {
            override fun onItemClick(t: Artist) {
                viewModel.getSetlists(t)
            }
        })
        binding.recyclerView.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(binding.recyclerView.context, LinearLayoutManager.VERTICAL)
        binding.recyclerView.addItemDecoration(dividerItemDecoration)
    }

    private fun updateRecyclerView(artists: List<Artist>) {
        val diff = ArtistDiff(adapter.artists, artists)
        val diffResult = DiffUtil.calculateDiff(diff)
        adapter.artists.clear()
        adapter.artists.addAll(artists)
        diffResult.dispatchUpdatesTo(adapter)
    }

    private fun populateAdapter(query: String) {
        val c = MatrixCursor(arrayOf(BaseColumns._ID, "items"))
        val suggestions = viewModel.getHistorySearchList()
        for (i in suggestions.indices) {
            if (suggestions[i].toLowerCase().contains(query.toLowerCase())) c.addRow(arrayOf(i, suggestions[i]))
        }

        suggestionAdapter.changeCursor(c)
    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, LENGTH_SHORT).show()
    }
}