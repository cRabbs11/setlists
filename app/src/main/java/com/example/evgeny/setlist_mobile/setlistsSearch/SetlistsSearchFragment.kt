package com.example.evgeny.setlist_mobile.setlistsSearch


import android.os.Bundle
import android.util.Log

import android.view.*
import android.widget.*

import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.example.evgeny.setlist_mobile.R
import com.example.evgeny.setlist_mobile.animators.SetlistListAnimator
import com.example.evgeny.setlist_mobile.databinding.FragmentSetlistsBinding

import com.example.evgeny.setlist_mobile.setlists.Setlist
import com.example.evgeny.setlist_mobile.setlists.SetlistsAPI
import com.example.evgeny.setlist_mobile.setlists.diffs.SetlistDiff
import com.example.evgeny.setlist_mobile.singleSetlist.SingleSetlistFragment
import com.example.evgeny.setlist_mobile.utils.OnItemClickListener
import com.example.evgeny.setlist_mobile.utils.SetlistListAdapter
import com.example.evgeny.setlist_mobile.utils.SetlistTouchHelperCallback
import com.example.evgeny.setlist_mobile.utils.SetlistsRepository

class SetlistsSearchFragment : Fragment(), OnItemClickListener<Setlist>, SetlistsSearchContract.View {

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
        var singleSetlistFragment = SingleSetlistFragment()

        var fragmentManager = getFragmentManager()
        var fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, singleSetlistFragment)
        fragmentTransaction.addToBackStack("")
        fragmentTransaction.commit()
    }

    val TAG = SetlistsSearchFragment::class.java.name + " BMTH "

    lateinit var presenter: SetlistsSearchPresenter
    lateinit var adapter: SetlistListAdapter
    //lateinit var emptyRecyclerMessageLayout: TextView
    private lateinit var binding: FragmentSetlistsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSetlistsBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    fun initView() {
        Log.d(TAG, " запустили")

        val setlistsAPI = SetlistsAPI()

        val setlistsRepository = SetlistsRepository
        adapter = SetlistListAdapter(this)
        binding.recyclerView.adapter = adapter
        var dividerItemDecoration = DividerItemDecoration(binding.recyclerView.getContext(),
            LinearLayoutManager.VERTICAL)
        binding.recyclerView.addItemDecoration(dividerItemDecoration)
        binding.recyclerView.itemAnimator = SetlistListAnimator(requireContext())
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