package com.example.evgeny.setlist_mobile.utils

import com.example.evgeny.setlist_mobile.data.entity.ArtistDataDTO
import com.example.evgeny.setlist_mobile.data.entity.SetlistsDataDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface SetlistsRetrofitInterface {

    @GET("search/artists")
    fun searchArtists(
            @Query("artistName") artistName: String,
            @Query("page") page: Int,
            @Query("sort") sort: String
    ): Call<ArtistDataDTO>

    @GET("artist/{artistMbid}/setlists")
    fun getArtistSetlists(
            @Path("artistMbid") artistMbid: String,
            @Query("p") page: Int

    ): Call<SetlistsDataDTO>
}