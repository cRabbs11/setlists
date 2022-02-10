package com.example.evgeny.setlist_mobile.utils

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SetlistsAPIInterface {

    @GET("search/artists")
    fun searchArtists(
            @Header("x-api-key") apiKey: String = SetlistAPI.KEY,
            @Header("Accept") accept: String = "application/json",
            @Query("artistName") artistName: String,
            @Query("page") page: Int,
            @Query("sort") sort: String
    ): Call<ArtistData>
}