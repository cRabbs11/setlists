package com.kochkov.evgeny.setlist_mobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kochkov.evgeny.setlist_mobile.R
import com.kochkov.evgeny.setlist_mobile.data.Artist
import com.kochkov.evgeny.setlist_mobile.utils.OnItemClickListener
import com.kochkov.evgeny.setlist_mobile.viewmodel.ArtistSearchFragmentViewModel
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.tooling.preview.Preview

class SearchCompose {

    @OptIn(ExperimentalMaterial3Api::class)
    @Preview
    @Composable
    fun SearchArtistCompose(
        clickListener: OnItemClickListener<Artist>,
        viewModel : ArtistSearchFragmentViewModel
    ) {
        var text by rememberSaveable { mutableStateOf("")}
        var active by rememberSaveable { mutableStateOf(false) }
        val suggestions by viewModel.queryArtistLiveData.observeAsState()
        val artists by viewModel.artistsLiveData.observeAsState()
        val loadingIndicator by viewModel.loadingIndicatorLiveData.observeAsState()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            SearchBar(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 10.dp),
                query = text,
                onQueryChange = { text = it },
                onSearch = {
                    active = false
                    if (text.isNotEmpty()) {
                        viewModel.searchArtistWithSetlists(text)
                    }
                           },
                active = active,
                onActiveChange = { active = it },
                placeholder = { Text(stringResource(R.string.artist_hint)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (active) {
                        Icon(
                            Icons.Default.Close, contentDescription = null,
                            Modifier.clickable {
                                text = ""
                                active = false
                            })
                    }
                               },
            ) {
                suggestions?.forEach { suggest ->
                    if (suggest.lowercase().contains(text.lowercase())) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                //.padding(all = 14.dp)
                                .clickable { text = suggest }
                        ) {
                            Icon(
                                Icons.Default.History,
                                contentDescription = null,
                            modifier = Modifier
                                .padding(start = 14.dp, end = 4.dp, top = 8.dp, bottom = 8.dp))
                            Text(
                                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                                fontSize = 14.sp, text = suggest)
                        }
                    }
                }
            }
            loadingIndicator?.let { if (it) { LoadingIndicator() } }
            LazyColumn(Modifier
                .fillMaxSize()
                .padding(top = 10.dp, start = 4.dp, end = 4.dp, bottom = 10.dp)) {
                artists?.let { data ->
                    itemsIndexed(data) { index, it ->
                        ArtistItem(it, clickListener)
                        if (index != data.size - 1) {
                            Divider()
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun LoadingIndicator() {
        CircularProgressIndicator(
            modifier = Modifier.padding(top = 48.dp),
            color = Color.Green,
            strokeWidth = 2.dp
        )
    }

    @Composable
    fun ArtistItem(artist: Artist, clickListener: OnItemClickListener<Artist>) {
        Row(modifier = Modifier
            .fillMaxWidth()
            //.wrapContentHeight()
            .height(height = 48.dp)
            //.clip(shape = RoundedCornerShape(6.dp))
            //.background(Color.Gray)
            //.padding(start = 1.dp, end = 2.dp)
            .clickable { clickListener.onItemClick(artist) }
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 14.dp, top = 12.dp, bottom = 12.dp),
                fontSize = 14.sp,
                text = artist.name)
        }
    }
}
