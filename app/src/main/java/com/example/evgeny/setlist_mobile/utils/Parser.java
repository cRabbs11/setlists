package com.example.evgeny.setlist_mobile.utils;

import android.util.Log;

import com.example.evgeny.setlist_mobile.Artist;

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
                String mbid = artistJson.getString("mdid");
                String name = artistJson.getString("name");
                String sortName = artistJson.getString("sortName");
                String url = artistJson.getString("url");
                String disambiguation = artistJson.getString("disambiguation");
//                Log.d(TAG, "name: " + name);
//                Log.d(TAG, "sortName: " + sortName);
//                Log.d(TAG, "url: " + url);
                Artist artist = new Artist();
                artist.mbid = mbid;
                artist.name = name;
                artist.sortName = sortName;
                artist.url = url;
                artist.disambiguation = disambiguation;
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
    public List<Artist> parseSetlists(String JSON) {
        List<Artist> mArtists = new ArrayList<>();
        JSONObject dataJsonObj = null;
        try {
            dataJsonObj = new JSONObject(JSON);
            JSONArray artists = dataJsonObj.getJSONArray("artist");
            // 2. перебираем и выводим контакты каждого друга
            for (int i = 0; i < artists.length(); i++) {
                JSONObject artistJson = artists.getJSONObject(i);
                String mbid = artistJson.getString("mdid");
                String name = artistJson.getString("name");
                String sortName = artistJson.getString("sortName");
                String url = artistJson.getString("url");
                String disambiguation = artistJson.getString("disambiguation");
//                Log.d(TAG, "name: " + name);
//                Log.d(TAG, "sortName: " + sortName);
//                Log.d(TAG, "url: " + url);
                Artist artist = new Artist();
                artist.mbid = mbid;
                artist.name = name;
                artist.sortName = sortName;
                artist.url = url;
                artist.disambiguation = disambiguation;
                mArtists.add(artist);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "mArtists.size(): " + mArtists.size());
        return mArtists;
    }
}
