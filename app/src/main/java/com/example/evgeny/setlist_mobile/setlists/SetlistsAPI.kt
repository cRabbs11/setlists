package com.example.evgeny.setlist_mobile.setlists

import android.util.Log
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class SetlistsAPI {

    private val TAG = SetlistsAPI::class.simpleName + " BMTH"

    private val URL_BASE = "https://api.setlist.fm/rest/1.0/"


            //"https://api.setlist.fm/rest/1.0/artist/$artistMbid/setlists?p=$page"
            //"https://api.setlist.fm/rest/1.0/search/artists?artistName=$artistName&p=1&sort=sortName"

    /**
     * Создание нового потока для запроса на сервер setlist
     * @param request тип запроса
     * @param data данные для запроса
     */
    //public SetlistConnect(String request, Bundle data, AnswerListener answerListener) {
    //    Log.d("BMTH", "new Thread... ");
    //    this.answerListener = answerListener;
    //    this.request = request;
    //    this.data = data;
    //    identifyRequest(request);
    //    thread =  new Thread(this, "новый поток");
    //    thread.start();
    //}

    fun getArtists(artistName: String) : List<Artist> {
        Log.d(TAG, "searchArtists...")
        val response = setRequestToSetlistsAPI(getArtistsURL(artistName))
        //Log.d(TAG, response)
        val parserKotlin = ParserKotlin()
        return parserKotlin.parseArtists(response)
    }

    fun getSetlists(artistMbid: String, page: Int = 1) : List<Setlist> {
        Log.d(TAG, "searchArtists...")
        val response = setRequestToSetlistsAPI(getSetlistsURL(artistMbid, page))
        //Log.d(TAG, response)
        val parserKotlin = ParserKotlin()
        return parserKotlin.parseSetlists(response)
    }

    //fun searchSetlists(artistMbid: String, answerListener: AnswerListener<List<Setlist>>) {
    //    Log.d(TAG, "searchArtists...")
    //    val setlists = ArrayList<Setlist>()
//
    //    val handler = object : Handler(Looper.getMainLooper()) {
    //        override fun handleMessage(msg: Message) {
    //            answerListener.getAnswer(setlists)
    //        }
    //    }
//
    //    val runnable = Runnable {
    //        kotlin.run {
    //            val response = setRequestToSetlistsAPI(getSetlistsURL(artistMbid))
    //            Log.d(TAG, response)
    //            val parserKotlin = ParserKotlin()
    //            parserKotlin.parseSetlists(response).forEach {
    //                setlists.add(it)
    //            }
    //            handler.sendEmptyMessage(1)
    //        }
    //    }
//
    //    val thread = Thread(runnable)
    //    thread.start()
    //}

    private fun setRequestToSetlistsAPI(url: URL): String {
        Log.d(TAG, "getConnection... ")
        var response="0"
        try {

            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.setRequestProperty ("x-api-key", "5f26897a-5ead-4985-8c6e-b8d427eb757e")
            httpURLConnection.setRequestProperty ("Accept", "application/json")
            val responseCode = httpURLConnection.getResponseCode()

            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "responseCode is OK:$responseCode")

                val inputStream = httpURLConnection.getInputStream()

                response = getStringFromInputStream(inputStream)

                //Log.d(TAG, "response: $response")

            } else {
                Log.d(TAG, "responseCode is NOT OK:$responseCode")
                Log.d(TAG, "response: $response")
            }
            httpURLConnection.disconnect()
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return response
    }

    private fun getStringFromInputStream(inputStream: InputStream): String  {
        val bufferSize = 1024
        val buffer = CharArray(bufferSize)
        val out = StringBuilder()
        val inReader : Reader
        try {
            inReader = InputStreamReader(inputStream, "UTF-8")
            while (true) {
                var rsz = 0

                try {
                    rsz = inReader.read(buffer, 0, buffer.size)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                if (rsz < 0)
                    break
                out.append(buffer, 0, rsz)
            }
            inReader.close()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return out.toString()
    }

    /**
     * Строит URL для поиска сетлистов
     */
    private fun getSetlistsURL(artistMbid: String, page: Int = 1): URL {
        Log.d(TAG, "getSetlists... ")
        //the Musicbrainz MBID of the artist
        //var mbid="b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d" //default
        //the number of the result page
        //var page="1"
        //Request URL
        //if (!setlistsData.isEmpty()) {
        //    mbid = setlistsData.getString("mbid")
        //    page = setlistsData.getString("page")
        //}
        return URL(URL_BASE + "artist/$artistMbid/setlists?p=$page")
    }

    /**
     * Строит URL для поиска artist
     */
    private fun getArtistsURL(artistName: String): URL {
        val page = 1
        val sort = "sortName"
        // the artist's Musicbrainz Identifier (mbid)
        //String artistMbid = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d";
//
        //// the artist's name
        //String artistName = "The Beatles";
//
        //// the artist's Ticketmaster Identifier (tmid)
        //String artistTmid = "735610";
//
        //// the number of the result page you'd like to have
        //String p = "1";
//
        //// the sort of the result, either sortName (default) or relevance
        //String sort = "sortName";
//
        //Log.d("BMTH", "getArtists()... ");
        //String artist;
        //String URL;
        //if (!data.isEmpty()) {
        //    artist = data.getString("artist");
        //    URL =
        return URL(URL_BASE + "search/artists?artistName=$artistName&p=$page&sort=$sort")
    }

}