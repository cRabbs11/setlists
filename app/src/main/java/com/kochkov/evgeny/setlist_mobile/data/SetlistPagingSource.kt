package com.kochkov.evgeny.setlist_mobile.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kochkov.evgeny.setlist_mobile.data.entity.Setlist
import com.kochkov.evgeny.setlist_mobile.data.entity.toSetlistList
import com.kochkov.evgeny.setlist_mobile.utils.SetlistsRetrofitInterface

class SetlistPagingSource(private val retrofit: SetlistsRetrofitInterface, val artist: String): PagingSource<Int, Setlist>() {

    val STARTING_KEY = 1
    override fun getRefreshKey(state: PagingState<Int, Setlist>): Int? {
        // In our case we grab the item closest to the anchor position
        // then return its id - (state.config.pageSize / 2) as a buffer
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition)?: return null
        return page.prevKey?.plus(1)?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Setlist> {
        val pageNumber = params.key?: STARTING_KEY
        return try {
            val setlists = retrofit.getSetlistsByArtist(artist, pageNumber).body()!!.toSetlistList()
            val nextKey = if (setlists.size<20) null else pageNumber + 1
            val prevKey = if (pageNumber==1) null else pageNumber - 1
            LoadResult.Page(
                data = setlists,
                prevKey = prevKey,
                nextKey = nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}