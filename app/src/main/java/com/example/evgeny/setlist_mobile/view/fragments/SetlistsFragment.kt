package com.example.evgeny.setlist_mobile.view.fragments

import android.os.Bundle
import android.util.Log

import android.view.*
import android.widget.*

import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.*
import com.example.evgeny.setlist_mobile.view.activities.MainActivity
import com.example.evgeny.setlist_mobile.animators.ItemListAnimator
import com.example.evgeny.setlist_mobile.data.Artist
import com.example.evgeny.setlist_mobile.databinding.FragmentSetlistsBinding

import com.example.evgeny.setlist_mobile.data.entity.Setlist
import com.example.evgeny.setlist_mobile.setlists.diffs.SetlistDiff
import com.example.evgeny.setlist_mobile.utils.*
import com.example.evgeny.setlist_mobile.utils.Constants.KEY_BUNDLE_ARTIST
import com.example.evgeny.setlist_mobile.viewmodel.SetlistsFragmentViewModel
import com.example.evgeny.setlist_mobile.viewmodel.factory

class SetlistsFragment : Fragment() {

    private fun updateRecyclerView(setlists: List<Setlist>) {
        val diff = SetlistDiff(adapter.setlists, setlists)
        val diffResult = DiffUtil.calculateDiff(diff)
        val scrollPosition = adapter.setlists.size
        adapter.setlists.clear()
        adapter.setlists.addAll(setlists)
        diffResult.dispatchUpdatesTo(adapter)

        if (scrollPosition<adapter.setlists.size) {
            binding.recyclerView.scrollToPosition(scrollPosition)
        }
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, LENGTH_SHORT).show()
    }

    val TAG = SetlistsFragment::class.java.name + " BMTH "

    lateinit var adapter: SetlistListAdapter
    //lateinit var emptyRecyclerMessageLayout: TextView
    private lateinit var binding: FragmentSetlistsBinding
    lateinit var sharedView: View
    private val viewModel: SetlistsFragmentViewModel by viewModels { factory(artist = arguments?.get(KEY_BUNDLE_ARTIST) as Artist)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSetlistsBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setlistsLiveData.observe(viewLifecycleOwner) { list ->
            updateRecyclerView(list)
        }

        postponeEnterTransition()
    }

    fun initView() {
        Log.d(TAG, " запустили")

        adapter = SetlistListAdapter(object: OnSharedTransitionClickListener<Setlist> {
            override fun onItemClick(setlist: Setlist, shared: View) {
                sharedView = shared
                (activity as MainActivity).openSingleSetlistFragment(shared, setlist)
            }
        })
        binding.recyclerView.adapter = adapter
        var dividerItemDecoration = DividerItemDecoration(binding.recyclerView.getContext(),
            LinearLayoutManager.VERTICAL)
        binding.recyclerView.addItemDecoration(dividerItemDecoration)
        binding.recyclerView.itemAnimator = ItemListAnimator(requireContext())
        //val itemTouchHelper = ItemTouchHelper(SetlistTouchHelperCallback(adapter))
        //itemTouchHelper.attachToRecyclerView(binding.recyclerView)
        //val linearSnapHelper = LinearSnapHelper()
        //linearSnapHelper.attachToRecyclerView(binding.recyclerView)
        //val pageSnapHelper = PagerSnapHelper()
        //pageSnapHelper.attachToRecyclerView(binding.recyclerView)
        binding.recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager = (recyclerView.layoutManager as LinearLayoutManager)
                val totalItemsCount = layoutManager.itemCount
                val lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition()
                viewModel.onRecyclerViewScrolled(lastVisiblePosition, totalItemsCount)
            }
        })

        binding.recyclerView.post {
            startPostponedEnterTransition()
        }
    }
}