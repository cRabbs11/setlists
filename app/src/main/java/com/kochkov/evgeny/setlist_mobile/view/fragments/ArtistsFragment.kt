package com.kochkov.evgeny.setlist_mobile.view.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kochkov.evgeny.setlist_mobile.data.Artist
import com.kochkov.evgeny.setlist_mobile.utils.OnItemClickListener
import com.kochkov.evgeny.setlist_mobile.view.activities.MainActivity
import com.kochkov.evgeny.setlist_mobile.ui.MyTheme
import com.kochkov.evgeny.setlist_mobile.ui.SearchArtistCompose
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
                MyTheme {
                    SearchArtistCompose(
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
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "DefaultPreviewLight"
)
@Composable
fun ReplyAppPreview() {
    MyTheme {
        Button(onClick = {}) {
            Text("some text")
        }
    }
}
