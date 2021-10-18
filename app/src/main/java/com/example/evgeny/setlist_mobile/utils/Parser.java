package com.example.evgeny.setlist_mobile.utils;

import android.util.Log;

import com.example.evgeny.setlist_mobile.setlists.Artist;
import com.example.evgeny.setlist_mobile.setlists.City;
import com.example.evgeny.setlist_mobile.setlists.Coords;
import com.example.evgeny.setlist_mobile.setlists.Country;
import com.example.evgeny.setlist_mobile.setlists.Set;
import com.example.evgeny.setlist_mobile.setlists.Setlist;
import com.example.evgeny.setlist_mobile.setlists.Song;
import com.example.evgeny.setlist_mobile.setlists.Tour;
import com.example.evgeny.setlist_mobile.setlists.Venue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private String TAG = "Setlist: " + Parser.class.getSimpleName();

    static Parser parser;

    private Parser() {}

    public static Parser getInstance() {

        if (parser==null) {
            parser = new Parser();
        }
        return parser;
    }

    /**
     * Распарсивание списка артитов
     * @param JSON ответ от сервера
     * @return список артистов
     */
    public List<Artist> parseArtists(String JSON) {
        List<Artist> mArtists = new ArrayList<>();
        JSONObject dataJsonObj = null;
        try {
            dataJsonObj = new JSONObject(JSON);
            JSONArray artists = dataJsonObj.getJSONArray("artist");
            // 2. перебираем и выводим контакты каждого друга
            for (int i = 0; i < artists.length(); i++) {
                JSONObject artistJson = artists.getJSONObject(i);
                Artist artist;
                artist = getArtist(artistJson);
                if (artist!=null) {
                    mArtists.add(artist);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "mArtists.size(): " + mArtists.size());
        return mArtists;
    }

    /**
     * Распарсивание списка сетлистов
     * @param JSON ответ от сервера
     * @return список сетлистов
     */
    public List<Setlist> parseSetlists(String JSON) {
        List<Setlist> mSetlists = new ArrayList<>();
        JSONObject dataJsonObj = null;
        try {
            dataJsonObj = new JSONObject(JSON);
            JSONArray setlists = dataJsonObj.getJSONArray("setlist");
            // 2. перебираем и выводим контакты каждого друга
            for (int i = 0; i < setlists.length(); i++) {

                JSONObject setlistJson = setlists.getJSONObject(i);
                JSONObject artistJson = setlistJson.getJSONObject("artist");
                Artist artist = getArtist(artistJson);
                JSONObject venueJson = setlistJson.getJSONObject("venue");
                Venue venue = getVenue(venueJson);
                Tour tour = null;
                //название тура
                if (setlistJson.has("tour")) {
                    JSONObject tourJson = setlistJson.getJSONObject("tour");
                    tour = getTour(tourJson);
                }

                JSONObject sets = setlistJson.getJSONObject("sets");
                List<Song> mSongs = getSets(sets);
                List<Set> mSets = getSets1(sets);
                //String info = artistJson.getString("info");
                //String url = artistJson.getString("url");
                String id = setlistJson.getString("id");
                String versionId = setlistJson.getString("versionId");
                //String lastFmEventId = artistJson.getString("lastFmEventIdo");
                String eventDate = setlistJson.getString("eventDate");
                String lastUpdated = setlistJson.getString("lastUpdated");

                //setlist.info = info;
                //setlist.url = url;
                //setlist.id = id;
                //setlist.versionId = versionId;
                //setlist.lastFmEventId = lastFmEventId;

                //setlist.set.songs = mSongs;

                mSetlists.add(new Setlist(artist, venue, tour, eventDate, lastUpdated, mSets));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "mSetlists.size(): " + mSetlists.size());
        return mSetlists;
    }

    private List<Song> getSets(JSONObject sets) {
        List<Song> mSongs = new ArrayList<>();
        try {
            JSONArray set = sets.getJSONArray("set");
            for (int i = 0; i < set.length(); i++) {
                JSONObject songs = set.getJSONObject(i);
                JSONArray song = songs.getJSONArray("song");
                for (int x = 0; x < song.length(); x++) {
                    mSongs.add(getSong(song.getJSONObject(x)));
                }

                if (songs.has("encore")) {

                }

                //Log.d("BMTH", "mSongs.size(): " + mSongs.size());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mSongs;
    }

    private List<Set> getSets1(JSONObject sets) {
        List<Set> mSets = new ArrayList<>();

        try {
            JSONArray jsonSet = sets.getJSONArray("set");
            for (int i = 0; i < jsonSet.length(); i++) {
                String encore = "...";
                String name = "...";
                List<Song> songs1 = new ArrayList<Song>();
                JSONObject songs = jsonSet.getJSONObject(i);
                JSONArray song = songs.getJSONArray("song");
                for (int x = 0; x < song.length(); x++) {
                    songs1.add(getSong(song.getJSONObject(x)));
                }

                if (songs.has("encore")) {
                    encore = "encore " + songs.getString("encore") + ": ";
                }

                if (songs.has("name")) {
                    name = songs.getString("name");
                }
                mSets.add(new Set(name, "numder", encore, songs1));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mSets;
    }

    private Song getSong(JSONObject jsonObject) {
        String name = "null";
        String info = null;
        Boolean tape = false;
        Artist cover = null;
        try {
            name = jsonObject.getString("name");

            if (jsonObject.has("info")) {
                info = jsonObject.getString("info");
            }

            if (jsonObject.has("cover")) {
                JSONObject jsonCover = jsonObject.getJSONObject("cover");
                cover = getArtist(jsonCover);
            }

            if (jsonObject.has("tape")) {
                tape = jsonObject.getBoolean("tape");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return new Song(name, info, tape, cover, 0);
    }

    //private Artist getArtist(JSONObject jsonObject) {
    //    Artist artist = new Artist();
    //    try {
    //    String mbid = jsonObject.getString("mbid");
    //    String name = jsonObject.getString("name");
    //    String sortName = jsonObject.getString("sortName");
    //    String url = jsonObject.getString("url");
    //    String error = jsonObject.getString("error");
    //    artist.mbid = mbid;
    //    artist.name = name;
    //    artist.sortName = sortName;
    //    artist.url = url;
    //    } catch (JSONException e) {
    //        e.printStackTrace();
    //    }
    //    return artist;
    //}

    private Artist getArtist(JSONObject jsonObject) {
        String mbid = null;
        String name = null;
        String sortName = null;
        String url = null;
        try {
            mbid = jsonObject.getString("mbid");
            name = jsonObject.getString("name");
            sortName = jsonObject.getString("sortName");
            url = jsonObject.getString("url");
            //String error = jsonObject.getString("error");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        //artist.mbid = mbid;
        //artist.name = name;
        //artist.sortName = sortName;
        //artist.url = url;
        return new Artist(mbid, name, sortName, url);
    }

    private Tour getTour(JSONObject jsonObject) {
        String name = null;
        try {
            name = jsonObject.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return new Tour(name);
    }

    private Venue getVenue(JSONObject jsonObject) {
        String id = null;
        String name = null;
        String url = null;
        City city = null;
        try {

            JSONObject cityJson = jsonObject.getJSONObject("city");
            city = getCity(cityJson);

            id = jsonObject.getString("id");
            name = jsonObject.getString("name");
            url = jsonObject.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return new Venue(id, name, url, city);
    }

    private City getCity(JSONObject jsonObject) {
        String id = null;
        String name = null;
        String state = null;
        String stateCode = null;
        Country country = null;
        Coords coords = null;

        try {
            JSONObject countryJson = jsonObject.getJSONObject("country");
            country = getCountry(countryJson);

            JSONObject coordsJson = jsonObject.getJSONObject("coords");
            coords = getCoords(coordsJson);

            id = jsonObject.getString("id");
            name = jsonObject.getString("name");
            state = jsonObject.getString("state");
            stateCode = jsonObject.getString("stateCode");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return new City(id, name, state, stateCode, coords, country);
    }

    private Coords getCoords(JSONObject jsonObject) {
        String coord_lat = null;
        String coord_long = null;
        try {
            coord_lat = jsonObject.getString("lat");
            coord_long = jsonObject.getString("long");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return new Coords(coord_lat, coord_long);
    }

    private Country getCountry(JSONObject jsonObject) {
        String code = null;
        String name = null;
        try {
            code = jsonObject.getString("code");
            name = jsonObject.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return new Country(code, name);
    }
}
