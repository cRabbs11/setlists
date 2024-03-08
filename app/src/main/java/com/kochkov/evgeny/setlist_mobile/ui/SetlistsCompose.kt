package com.kochkov.evgeny.setlist_mobile.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.kochkov.evgeny.setlist_mobile.customView.ConcertDateView
import com.kochkov.evgeny.setlist_mobile.data.entity.Setlist
import com.kochkov.evgeny.setlist_mobile.utils.OnItemClickListener

import com.kochkov.evgeny.setlist_mobile.viewmodel.SetlistsFragmentViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SetlistsCompose(
    clickListener: OnItemClickListener<Setlist>,
    viewModel : SetlistsFragmentViewModel,
    //pager: Pager<Int, Setlist>
) {
    val setlists by viewModel.setlistsLiveData.observeAsState()
    LazyColumn(
        //verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
        modifier = Modifier
            .fillMaxSize()
            //.padding(top = 10.dp, start = 4.dp, end = 4.dp, bottom = 10.dp)
    ) {
        setlists?.let { data ->
            itemsIndexed(data) { index, it ->
                SetlistCompose(it, clickListener)
                if (index != data.size - 1) {
                    Divider()
                }

            }
        }
    }
}

@Composable
fun SetlistsComposePaging(
    clickListener: OnItemClickListener<Setlist>,
    viewModel : SetlistsFragmentViewModel,
    paddingValues: PaddingValues
) {
    val lazyPagingItems = viewModel.items.collectAsLazyPagingItems()
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
        modifier = Modifier
            .fillMaxSize().padding(paddingValues)
    ) {
        items(
            count = lazyPagingItems.itemCount,
            key = lazyPagingItems.itemKey {
                lazyPagingItems
                it.id
            }
        ) { index ->
            val setlist = lazyPagingItems[index]
            if (setlist!=null) {
                SetlistCompose(setlist, clickListener)
                Divider()
            }
        }
    }
}

@Composable
fun SetlistCompose(setlist: Setlist, clickListener: OnItemClickListener<Setlist>? = null) {
    Row(modifier = Modifier
        .padding(top = 1.dp, bottom = 1.dp)
        .clickable { clickListener?.onItemClick(setlist)},
        verticalAlignment = Alignment.CenterVertically
    ) {
        MigrateConcertDateView(setlist.eventDate)
        Column(verticalArrangement = Arrangement.Center) {
            Row() {
                Text(modifier = Modifier.width(70.dp), text = "Артист:", textAlign = TextAlign.End)
                Text(modifier = Modifier.padding(start = 5.dp), text = setlist.artist?.name?: "unknown artist")
            }
            Row() {
                Text(modifier = Modifier.width(70.dp), text = "Тур:", textAlign = TextAlign.End)
                Text(modifier = Modifier.padding(start = 5.dp),text = setlist.tour?.name?: "")
            }
            Row() {
                Text(modifier = Modifier.width(70.dp), text = "Место:", textAlign = TextAlign.End)
                val venue = setlist.venue?.name + ": " + setlist.venue?.city?.name + ", " + setlist.venue?.city?.country?.name
                Text(modifier = Modifier.padding(start = 5.dp),text = venue)
            }
        }

    }
}

@Preview
@Composable
fun MigrateConcertDateView(
    eventDate: String
) {
    AndroidView(
        modifier = Modifier
            .size(width = 70.dp, height = 70.dp),
            //.fillMaxSize(),
        factory = { context ->
            ConcertDateView(context).apply {
                setOnClickListener {
                    Log.d("BMTH", "click")
                }
            }
        },
        update = { view ->
            val dt = SimpleDateFormat("dd-mm-yyyy", Locale.getDefault())
            val date = dt.parse(eventDate)
            date?.let {
                view.setDate(it)
            }
        }
    )
}
