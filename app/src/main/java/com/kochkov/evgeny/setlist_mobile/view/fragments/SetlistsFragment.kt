package com.kochkov.evgeny.setlist_mobile.view.fragments

import android.R
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import android.widget.Toast.LENGTH_SHORT
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kochkov.evgeny.setlist_mobile.data.Artist
import com.kochkov.evgeny.setlist_mobile.data.entity.Setlist
import com.kochkov.evgeny.setlist_mobile.databinding.FragmentSetlistsBinding
import com.kochkov.evgeny.setlist_mobile.ui.MyTheme
import com.kochkov.evgeny.setlist_mobile.utils.*
import com.kochkov.evgeny.setlist_mobile.utils.Constants.KEY_BUNDLE_ARTIST
import com.kochkov.evgeny.setlist_mobile.view.activities.MainActivity
import com.kochkov.evgeny.setlist_mobile.ui.SetlistsCompose
import com.kochkov.evgeny.setlist_mobile.ui.SetlistsComposePaging
import com.kochkov.evgeny.setlist_mobile.viewmodel.SetlistsFragmentViewModel
import com.kochkov.evgeny.setlist_mobile.viewmodel.factory

class SetlistsFragment : Fragment() {

    private fun updateRecyclerView(setlists: List<Setlist>) {
        //val diff = SetlistDiff(adapter.setlists, setlists)
        //val diffResult = DiffUtil.calculateDiff(diff)
        //val scrollPosition = adapter.setlists.size
        //adapter.setlists.clear()
        //adapter.setlists.addAll(setlists)
        //diffResult.dispatchUpdatesTo(adapter)
//
        //if (scrollPosition<adapter.setlists.size) {
        //    binding.recyclerView.scrollToPosition(scrollPosition)
        //}
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, LENGTH_SHORT).show()
    }

    val TAG = SetlistsFragment::class.java.name + " BMTH "

    //lateinit var emptyRecyclerMessageLayout: TextView
    private lateinit var binding: FragmentSetlistsBinding
    lateinit var sharedView: View
    private val viewModel: SetlistsFragmentViewModel by viewModels { factory(artist = arguments?.get(KEY_BUNDLE_ARTIST) as Artist)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //binding = FragmentSetlistsBinding.inflate(inflater, container, false)
        //initView()
        //return binding.root
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MyTheme {
                    val artist = viewModel.artistLiveData.observeAsState()
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                //colors = TopAppBarDefaults.topAppBarColors(
                                //    //containerColor = MaterialTheme.colorScheme.primaryContainer,
                                //    //titleContentColor = MaterialTheme.colorScheme.primary,
                                //),
                                title = {
                                    artist.value?.let {
                                        Text(it.name)
                                    }
                                },
                                navigationIcon = {
                                    IconButton(onClick = { activity?.onBackPressed() })
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.ArrowBack,
                                            contentDescription = "Localized description"
                                        )
                                    }
                                },)
                        },
                    ) { innerPadding ->
                        SetlistsComposePaging(
                            clickListener = object : OnItemClickListener<Setlist> {
                                override fun onItemClick(setlist: Setlist) {
                                    (activity as MainActivity).openSingleSetlistFragment(setlist)
                                }
                            },
                            viewModel = viewModel,
                            paddingValues = innerPadding
                        )
                    }
                }
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //(activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //viewModel.setlistsLiveData.observe(viewLifecycleOwner) { list ->
        //    updateRecyclerView(list)
        //}
//
        //postponeEnterTransition()
//
        //(activity as MainActivity).supportActionBar?.let {
        //    it.title = (arguments?.get(KEY_BUNDLE_ARTIST) as Artist).name?: getString(com.kochkov.evgeny.setlist_mobile.R.string.title_single_setlist)
        //}
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                (activity as MainActivity).closeFragment()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun initView() {
        Log.d(TAG, " запустили")

        //    adapter = SetlistListAdapter(object: OnSharedTransitionClickListener<Setlist> {
        //    override fun onItemClick(setlist: Setlist, shared: View) {
        //        sharedView = shared
        //                    (activity as MainActivity).openSingleSetlistFragment(shared, setlist)
        //                }
        //})
        //    binding.recyclerView.adapter = adapter
        //    var dividerItemDecoration = DividerItemDecoration(binding.recyclerView.getContext(),
        //    LinearLayoutManager.VERTICAL)
        //binding.recyclerView.addItemDecoration(dividerItemDecoration)
        //    binding.recyclerView.itemAnimator = ItemListAnimator(requireContext())
        //val itemTouchHelper = ItemTouchHelper(SetlistTouchHelperCallback(adapter))
        //itemTouchHelper.attachToRecyclerView(binding.recyclerView)
        //val linearSnapHelper = LinearSnapHelper()
        //linearSnapHelper.attachToRecyclerView(binding.recyclerView)
        //val pageSnapHelper = PagerSnapHelper()
        //pageSnapHelper.attachToRecyclerView(binding.recyclerView)
        //binding.recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
        //    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        //        super.onScrollStateChanged(recyclerView, newState)
        //        val layoutManager = (recyclerView.layoutManager as LinearLayoutManager)
        //        val totalItemsCount = layoutManager.itemCount
        //        val lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition()
        //        viewModel.onRecyclerViewScrolled(lastVisiblePosition, totalItemsCount)
        //    }
        //})
//
        //binding.recyclerView.post {
        //    startPostponedEnterTransition()
        //}
    }
}