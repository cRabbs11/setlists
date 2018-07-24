package com.example.evgeny.setlist_mobile.utils;

import android.util.Log;

import com.example.evgeny.setlist_mobile.model.Artist;
import com.example.evgeny.setlist_mobile.model.City;
import com.example.evgeny.setlist_mobile.model.Coords;
import com.example.evgeny.setlist_mobile.model.Country;
import com.example.evgeny.setlist_mobile.model.Setlist;
import com.example.evgeny.setlist_mobile.model.Song;
import com.example.evgeny.setlist_mobile.model.Tour;
import com.example.evgeny.setlist_mobile.model.Venue;

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
                mArtists.add(artist);
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
                //JSONObject tourJson = setlistJson.getJSONObject("tour");
                //Tour tour = getTour(tourJson);
                JSONObject sets = setlistJson.getJSONObject("sets");
                List<Song> mSongs = getSets(sets);
                //String info = artistJson.getString("info");
                //String url = artistJson.getString("url");
                String id = setlistJson.getString("id");
                String versionId = setlistJson.getString("versionId");
                //String lastFmEventId = artistJson.getString("lastFmEventIdo");
                String eventDate = setlistJson.getString("eventDate");
                String lastUpdated = setlistJson.getString("lastUpdated");
                Setlist setlist = new Setlist();
               // setlist.info = info;
                //setlist.url = url;
                //setlist.id = id;
                //setlist.versionId = versionId;
                //setlist.lastFmEventId = lastFmEventId;
                setlist.eventDate = eventDate;
                setlist.lastUpdated = lastUpdated;
                setlist.artist = artist;
                setlist.venue = venue;
                //setlist.tour = tour;
                setlist.set.songs = mSongs;
                mSetlists.add(setlist);
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
                //Log.d("BMTH", "mSongs.size(): " + mSongs.size());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mSongs;
    }

    private Song getSong(JSONObject jsonObject) {
        Song song = new Song();
        try {
            String name = jsonObject.getString("name");
            song.name = name;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return song;
    }

    private Artist getArtist(JSONObject jsonObject) {
        Artist artist = new Artist();
        try {
        String mbid = jsonObject.getString("mbid");
        String name = jsonObject.getString("name");
        String sortName = jsonObject.getString("sortName");
        String url = jsonObject.getString("url");
        artist.mbid = mbid;
        artist.name = name;
        artist.sortName = sortName;
        artist.url = url;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return artist;
    }

    private Tour getTour(JSONObject jsonObject) {
        Tour tour = new Tour();
        try {
            String name = jsonObject.getString("name");
            tour.name = name;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tour;
    }

    private Venue getVenue(JSONObject jsonObject) {
        Venue venue = new Venue();
        try {

            JSONObject cityJson = jsonObject.getJSONObject("city");
            City city = getCity(cityJson);

            String id = jsonObject.getString("id");
            String name = jsonObject.getString("name");
            String url = jsonObject.getString("url");
            venue.id = id;
            venue.name = name;
            venue.url = url;
            venue.city = city;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return venue;
    }

    private City getCity(JSONObject jsonObject) {
        City city = new City();
        try {

            JSONObject countryJson = jsonObject.getJSONObject("country");
            Country country = getCountry(countryJson);

            JSONObject coordsJson = jsonObject.getJSONObject("coords");
            Coords coords = getCoords(coordsJson);

            String id = jsonObject.getString("id");
            String name = jsonObject.getString("name");
            String state = jsonObject.getString("state");
            String stateCode = jsonObject.getString("stateCode");
            city.id = id;
            city.name = name;
            city.state = state;
            city.stateCode = stateCode;
            city.country = country;
            city.coords = coords;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return city;
    }

    private Coords getCoords(JSONObject jsonObject) {
        Coords coords = new Coords();
        try {
            String coord_lat = jsonObject.getString("lat");
            String coord_long = jsonObject.getString("long");
            coords.coord_lat = coord_lat;
            coords.coord_long = coord_long;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return coords;
    }

    private Country getCountry(JSONObject jsonObject) {
        Country country = new Country();
        try {
            String code = jsonObject.getString("code");
            String name = jsonObject.getString("name");
            country.code = code;
            country.name = name;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return country;
    }
}
