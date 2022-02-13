package com.example.evgeny.setlist_mobile.utils

import com.example.evgeny.setlist_mobile.data.entity.ArtistDataDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SetlistsRetrofitInterface {

    @GET("search/artists")
    fun searchArtists(
            @Header("x-api-key") apiKey: String,
            @Header("Accept") accept: String,
            @Query("artistName") artistName: String,
            @Query("page") page: Int,
            @Query("sort") sort: String
    ): Call<ArtistDataDTO>
}