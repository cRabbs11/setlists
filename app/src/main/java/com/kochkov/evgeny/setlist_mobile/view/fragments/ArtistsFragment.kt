package com.kochkov.evgeny.setlist_mobile.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kochkov.evgeny.setlist_mobile.data.Artist
import com.kochkov.evgeny.setlist_mobile.utils.OnItemClickListener
import com.kochkov.evgeny.setlist_mobile.view.activities.MainActivity
import com.kochkov.evgeny.setlist_mobile.ui.SearchCompose
import com.kochkov.evgeny.setlist_mobile.viewmodel.ArtistSearchFragmentViewModel

class ArtistsFragment: Fragment() {

    private val viewModel: ArtistSearchFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val search = SearchCompose()
                search.SearchArtistCompose(
                    clickListener = object : OnItemClickListener<Artist> {
                        override fun onItemClick(artist: Artist) {
                            (activity as MainActivity).openSetlistsSearchFragment(artist)
                        }
                    },
                    viewModel = viewModel
                )
            }

        }
    }
}