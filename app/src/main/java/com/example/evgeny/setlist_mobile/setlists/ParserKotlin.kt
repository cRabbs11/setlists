package com.example.evgeny.setlist_mobile.setlists


import android.util.Log
import org.json.JSONException
import org.json.JSONObject
class ParserKotlin {

    val TAG = ParserKotlin::class.simpleName + " BMTH"

    /**
     * Распарсивание списка артитов
     * @param JSON ответ от сервера
     * @return список артистов
     */
    fun parseArtists(JSON: String) :List<Artist>  {
        val artists = ArrayList<Artist>()
        try {
            val dataJsonObj = JSONObject(JSON)
            val jsonArtists = dataJsonObj.getJSONArray("artist")
            for (i in 0 until jsonArtists.length()) {
                val artistJson = jsonArtists.getJSONObject(i)
                val artist = getArtist(artistJson)
                if (artist!=null) {
                    artists.add(artist)
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            return artists
        }
        Log.d(TAG, "artists.size(): " + artists.size)
        return artists
    }

    /**
     * Распарсивание списка сетлистов
     * @param JSON ответ от сервера
     * @return список сетлистов
     */
    fun parseSetlists(JSON: String): List<Setlist>  {
        val setlists = ArrayList<Setlist>()
        try {
            val dataJsonObj = JSONObject(JSON)
            val jsonSetlists = dataJsonObj.getJSONArray("setlist")
            for (i in 0 until jsonSetlists.length()) {
                val setlistJson = jsonSetlists.getJSONObject(i)
                val setlist = getSetlist(setlistJson)
                if (setlist!=null) {
                    setlists.add(setlist)
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        Log.d(TAG, "mSetlists.size(): " + setlists.size)
        return setlists
    }


    private fun getArtist(jsonObject: JSONObject) : Artist? {
        try {
            val mbid = jsonObject.getString("mbid")
            val name = jsonObject.getString("name")
            val sortName = jsonObject.getString("sortName")
            val url = jsonObject.getString("url")
            return Artist(mbid, name, sortName, url)
        } catch (e: JSONException) {
            e.printStackTrace()
            return null
        }
    }

    private fun  getCity(jsonObject: JSONObject) : City? {
        try {
            val id = jsonObject.getString("id")
            val name = jsonObject.getString("name")
            val state = jsonObject.getString("state")
            val stateCode = jsonObject.getString("stateCode")

            val country = getCountry(jsonObject.getJSONObject("country"))
            val coords = getCoords(jsonObject.getJSONObject("coords"))

            return City(id, name, state, stateCode, coords, country)
        } catch (e: JSONException) {
            e.printStackTrace()
            return null
        }
    }

    private fun getCoords(jsonObject: JSONObject) : Coords? {
        try {
            val coord_lat = jsonObject.getString("lat")
            val coord_long = jsonObject.getString("long")
            return Coords(coord_lat, coord_long)
        } catch (e: JSONException) {
            e.printStackTrace()
            return null
        }
    }

    private fun getCountry(jsonObject: JSONObject) : Country? {
        try {
            val code = jsonObject.getString("code")
            val name = jsonObject.getString("name")
            return Country(code, name)
        } catch (e: JSONException) {
            e.printStackTrace()
            return null
        }
    }

    private fun getSet(jsonObject: JSONObject) : Set? {
        try {
            val name = if (jsonObject.has("name")) {
                jsonObject.getString("name") } else { "" }

            val number = if (jsonObject.has("number")) {
                jsonObject.getString("number") } else { "" }

            val encore = if (jsonObject.has("encore")) {
                jsonObject.getString("encore") } else { "" }

            val songs = ArrayList<Song>()
            val jsonSongList = jsonObject.getJSONArray("song")
            var songNumber = 1
            for (i in 0 until jsonSongList.length()) {
                val song = getSong(jsonSongList.getJSONObject(i))
                if (song!=null) {
                    if (!song.tape) {
                        song.number=songNumber++
                    }
                    songs.add(song)
                }
            }
            return Set(name, number, encore, songs)
        } catch (e: JSONException) {
            e.printStackTrace()
            return null
        }
    }

    private fun getSetlist(jsonObject: JSONObject) : Setlist? {
        try {
            val artist = getArtist(jsonObject.getJSONObject("artist"))
            val venue = getVenue(jsonObject.getJSONObject("venue"))
            val tour = getTour(jsonObject.getJSONObject("tour"))
            val eventDate = jsonObject.getString("eventDate")
            val lastUpdated = jsonObject.getString("lastUpdated")


            val sets = ArrayList<Set>()

            val jsonSets = jsonObject.getJSONObject("sets")
            val jsonSetArray = jsonSets.getJSONArray("set")

            for (i in 0 until jsonSetArray.length()) {
                val set = getSet(jsonSetArray.getJSONObject(i))
                if (set!=null) {
                    sets.add(set)
                }
            }
            return Setlist(artist, venue, tour, eventDate, lastUpdated, sets)
        } catch (e: JSONException) {
            e.printStackTrace()
            return null
        }
    }

    private fun getSong(jsonObject: JSONObject) : Song? {
        try {
            val name = jsonObject.getString("name")

            val info = if (jsonObject.has("info")) {
                jsonObject.getString("info") } else { "" }

            val tape = if (jsonObject.has("tape")) {
                jsonObject.getBoolean("tape") } else { false }


            val cover = if (jsonObject.has("cover")) {
                getArtist(jsonObject.getJSONObject("cover"))
            } else { null }

            val with = if (jsonObject.has("with")) {
                getArtist(jsonObject.getJSONObject("with"))
            } else { null }


            return Song(name, info, tape, cover, with,0)
        } catch (e: JSONException) {
            e.printStackTrace()
            return null
        }
    }

    private fun getTour(jsonObject: JSONObject) : Tour? {
        try {
            val name = jsonObject.getString("name")
            return Tour(name)
        } catch (e: JSONException) {
            e.printStackTrace()
            return null
        }
    }

    private fun getVenue(jsonObject: JSONObject) : Venue? {
        try {
            val id = jsonObject.getString("id")
            val name = jsonObject.getString("name")
            val url = jsonObject.getString("url")
            val city = if (jsonObject.has("city")) {
                getCity(jsonObject.getJSONObject("city"))
            } else { null }

            return Venue(id, name, url, city)
        } catch (e: JSONException) {
            e.printStackTrace()
            return null
        }
    }





}