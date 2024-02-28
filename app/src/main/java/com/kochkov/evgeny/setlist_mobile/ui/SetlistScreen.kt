package com.kochkov.evgeny.setlist_mobile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kochkov.evgeny.setlist_mobile.data.entity.Setlist
import com.kochkov.evgeny.setlist_mobile.setlists.SongListItem
import com.kochkov.evgeny.setlist_mobile.utils.OnItemClickListener
import com.kochkov.evgeny.setlist_mobile.viewmodel.SingleSetlistFragmentViewModel
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.kochkov.evgeny.setlist_mobile.R
import com.kochkov.evgeny.setlist_mobile.data.entity.Song
import com.kochkov.evgeny.setlist_mobile.data.entity.Set

@Composable
fun SetlistScreen(
    viewModel: SingleSetlistFragmentViewModel,
    onMapClickListener: OnItemClickListener<Setlist>
) {
    val setlist = viewModel.getSetlist()
    val songList by viewModel.songListItemLiveData.observeAsState()
    Box() {
        Column(modifier = Modifier.padding(start = 5.dp, top = 5.dp)) {
            SetlistCompose(setlist = setlist)
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp, start = 4.dp, end = 4.dp, bottom = 10.dp)) {
                songList?.let { data ->
                    itemsIndexed(data) { index, item ->
                        SetlistItem(songListItem = item)
                        if (index != data.size - 1) {
                            Divider()
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 24.dp, end= 24.dp),
            shape = CircleShape,
            onClick = { onMapClickListener.onItemClick(setlist) }) {
            Icon(Icons.Filled.Map, "venue map")
        }
    }
}

@Composable
fun SetlistItem(songListItem: SongListItem) {
    if (songListItem is Song) {
        SongItem(songListItem)
    } else if (songListItem is Set) {
        SetItem(songListItem)
    }
}

@Composable
fun SongItem(song: Song) {
    Row(modifier = Modifier
        //.height(48.dp)
        .defaultMinSize(minHeight = 48.dp) //проверить ркастягивание height по заполнению
        .fillMaxWidth()) {
        if (song.tape) {
            Image(painter = painterResource(id = R.drawable.ic_radio_black_24dp), contentDescription = "", modifier = Modifier.size(height = 24.dp, width = 24.dp))
        }
        Column {
            Text(text = if (song.tape) {
                song.name
            } else {
                "${song.number}. ${song.name}"
            }) //songName
            if (song.with!=null) { Text(text = "(with ${song.with.name})") }
            song.info?.let { if (song.info!="") { Text(text = song.info) } }
        }
    }

}

@Composable
fun SetItem(set: Set) {
    val SET_ENCORE_NAME = "Encore: "
    Row(modifier = Modifier
        .height(48.dp)
        .fillMaxWidth()) {
        val setName = if (set.name!="") {
            set.name
        } else if (set.encore==1) {
            SET_ENCORE_NAME
        } else {
            "$SET_ENCORE_NAME ${set.encore}"
        }
        Text(text = setName)
    }
}