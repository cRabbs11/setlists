package com.kochkov.evgeny.setlist_mobile.utils

import com.kochkov.evgeny.setlist_mobile.data.entity.ArtistDataDTO
import com.kochkov.evgeny.setlist_mobile.data.entity.SetlistsDataDTO
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SetlistsRetrofitInterface {

    @GET("search/artists")
    suspend fun searchArtists(
        @Query("artistName") artistName: String,
        @Query("page") page: Int,
        @Query("sort") sort: String
    ): Response<ArtistDataDTO>

    @GET("artist/{artistMbid}/setlists")
    fun getSetlistsByArtist(
            @Path("artistMbid") artistMbid: String,
            @Query("p") page: Int

    ): Call<SetlistsDataDTO>

    @GET("artist/{artistMbid}/setlists")
    fun getSetlistsByArtistObservable(
        @Path("artistMbid") artistMbid: String,
        @Query("p") page: Int

    ): Observable<SetlistsDataDTO>
}