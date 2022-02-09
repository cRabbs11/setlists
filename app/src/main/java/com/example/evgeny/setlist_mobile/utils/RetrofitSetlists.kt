package com.example.evgeny.setlist_mobile.utils

import com.example.evgeny.setlist_mobile.setlists.Artist
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitSetlists {

    val TAG = RetrofitSetlists::class.java.name + " BMTH "
    private val BASE_URL = "https://api.setlist.fm/rest/1.0/"

    fun searchArtists(artistName: String, listener: RequestListener<List<Artist>>) {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create(RetrofitInterface::class.java)

        service.searchArtists(SetlistAPI.KEY, "application/json", artistName, 1, "sortName").enqueue(object: Callback<ArtistData> {
            override fun onResponse(call: Call<ArtistData>, response: Response<ArtistData>) {
                if (response.body()!=null) {
                    listener.onSuccessResponse(response.body()!!.artist)
                } else {
                    listener.onNullResponse()
                }
            }

            override fun onFailure(call: Call<ArtistData>, t: Throwable) {
                t.printStackTrace()
                listener.onNullResponse()
            }

        })
    }

}