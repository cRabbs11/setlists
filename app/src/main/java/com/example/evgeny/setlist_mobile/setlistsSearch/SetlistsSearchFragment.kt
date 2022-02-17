package com.example.evgeny.setlist_mobile.setlistsSearch


import android.os.Bundle
import android.util.Log

import android.view.*
import android.widget.*

import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.example.evgeny.setlist_mobile.MainActivity
import com.example.evgeny.setlist_mobile.animators.ItemListAnimator
import com.example.evgeny.setlist_mobile.databinding.FragmentSetlistsBinding

import com.example.evgeny.setlist_mobile.setlists.Setlist
import com.example.evgeny.setlist_mobile.setlists.diffs.SetlistDiff
import com.example.evgeny.setlist_mobile.utils.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SetlistsSearchFragment : Fragment(), OnSharedTransitionClickListener<Setlist>, SetlistsSearchContract.View {

    override fun showSetlistList(list: List<Setlist>) {
        Log.d(TAG, "setlists count: ${list.size} ")
        if (list.isNotEmpty()) {
            val diff = SetlistDiff(adapter.setlists, list as ArrayList<Setlist>)
            val scrollPosition = adapter.setlists.size
            val diffResult = DiffUtil.calculateDiff(diff)
            adapter.addUniqItems(list)
            diffResult.dispatchUpdatesTo(adapter)

            if (scrollPosition<adapter.setlists.size) {
                binding.recyclerView.scrollToPosition(scrollPosition)
            }
            //emptyRecyclerMessageLayout.visibility= View.GONE
        } else {
            //emptyRecyclerMessageLayout.visibility= View.VISIBLE
        }
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, LENGTH_SHORT).show()
    }

    override fun openSetlist() {
        Log.d(TAG, "openSetlist")
        (activity as MainActivity).openSingleSetlistFragment(sharedView)
    }

    val TAG = SetlistsSearchFragment::class.java.name + " BMTH "

    lateinit var presenter: SetlistsSearchPresenter
    lateinit var adapter: SetlistListAdapter
    //lateinit var emptyRecyclerMessageLayout: TextView
    private lateinit var binding: FragmentSetlistsBinding
    lateinit var sharedView: View

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
        postponeEnterTransition()
    }

    fun initView() {
        Log.d(TAG, " запустили")

        val setlistsRepository = SetlistsRepository
        adapter = SetlistListAdapter(this)
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
                presenter.onRecyclerViewScrolled(lastVisiblePosition, totalItemsCount)
            }
        })

        binding.recyclerView.post {
            startPostponedEnterTransition()
        }

        val retrofit = Retrofit.Builder()
                .baseUrl(SetlistsAPIConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val service = retrofit.create(SetlistsRetrofitInterface::class.java)

        presenter = SetlistsSearchPresenter(setlistsRepository, service)
        presenter.attachView(this)
        presenter.viewIsReady()
    }

    override fun updateSetlistList(words: List<Setlist>) {

    }

    override fun onItemClick(t: Setlist, sharedView: View) {
        this.sharedView = sharedView
        presenter.onListItemClicked(t)
    }
}