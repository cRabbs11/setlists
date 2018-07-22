package com.example.evgeny.setlist_mobile.net;

import android.os.Bundle;
import android.util.Log;

import com.example.evgeny.setlist_mobile.Artist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evgeny on 25.05.2018.
 */

public class SetlistConnectNew implements Runnable {

    private Thread thread;
    Bundle data;

    String response="";

    private List<Artist> mArtists = new ArrayList<>();

    /**
     * Создание нового потока для запроса на сервер setlist
     * @param request тип запроса
     * @param data данные для запроса
     */
    public SetlistConnectNew(String request, Bundle data) {
        Log.d("BMTH", "new Thread... ");
        this.data = data;
        identifyRequest(request);
        thread = new Thread(this, "новый поток");
        thread.start();
    }

    /**
     * Строит URL в зависимости от запроса
     * @param request запрос
     */
    private void identifyRequest(String request) {
        Log.d("BMTH", "identifyRequest: " + request);
        switch(request) {
            case "getArtists":
                getArtists();
                break;
            case "getSetlists":
                getSetlists();
                break;
        }
    }

    /**
     * Строит URL для поиска сетлистов
     */
    private String getSetlists() {
        //the Musicbrainz MBID of the artist
        String mbid="b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d";
        //the number of the result page
        String page="1";
        //Request URL
        String URL;
        if (!data.isEmpty()) {
            mbid = data.getString("mbid");
            page = data.getString("page");
        }
        URL = "https://api.setlist.fm/rest/1.0/artist/" + mbid + "/setlists?p=" + page + "\"";
        return URL;
    }

    /**
     * Строит URL для поиска artist
     */
    private String getArtists() {

        // the artist's Musicbrainz Identifier (mbid)
        String artistMbid = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d";

        // the artist's name
        String artistName = "The Beatles";

        // the artist's Ticketmaster Identifier (tmid)
        String artistTmid = "735610";

        // the number of the result page you'd like to have
        String p = "1";

        // the sort of the result, either sortName (default) or relevance
        String sort = "sortName";

        Log.d("BMTH", "getArtists()... ");
        String artist;
        String URL;
        if (!data.isEmpty()) {
            artist = data.getString("artist");
            URL = "https://api.setlist.fm/rest/1.0/search/artists?artistName=" + artist + "&p=1&sort=sortName";
        } else {
            URL = "https://api.setlist.fm/rest/1.0/search/artists?artistName=" + "Pink Floyd" + "&p=1&sort=sortName";
        }
        return URL;
    }

    public String getConnection() {
        Log.d("BMTH", "getConnection... ");
        String response="0";
        try {
            //URL url = new URL(stringURL);

            URL url = new URL(getArtists());

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty ("x-api-key", "5f26897a-5ead-4985-8c6e-b8d427eb757e");
            httpURLConnection.setRequestProperty ("Accept", "application/json");
            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.d("BMTH", "responseCode is OK: " + responseCode);

                InputStream inputStream = httpURLConnection.getInputStream();

                response = getStringFromInputStream(inputStream);

                Log.d("BMTH", "response: " + response);

            } else {
                Log.d("BMTH", "responseCode is NOT OK: " + responseCode);
            }
            httpURLConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private String getStringFromInputStream(InputStream inputStream) {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = null;

        try {
            in = new InputStreamReader(inputStream, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        for (; ; ) {
            int rsz = 0;

            try {
                rsz = in.read(buffer, 0, buffer.length);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out.toString();
    }

    /**
     * распарсивание полученного ответа
     */
    public List<Artist> unParse(String response) {
        JSONObject dataJsonObj = null;

        try {
            dataJsonObj = new JSONObject(response);
            JSONArray artists = dataJsonObj.getJSONArray("artist");

            // 2. перебираем и выводим контакты каждого друга
            for (int i = 0; i < artists.length(); i++) {
                JSONObject artistJson = artists.getJSONObject(i);

                String name = artistJson.getString("name");
                String sortName = artistJson.getString("sortName");
                String url = artistJson.getString("url");
//                JSONObject contacts = artist.getJSONObject("contacts");
//
//
//                String phone = contacts.getString("mobile");
//                String email = contacts.getString("email");
//                String skype = contacts.getString("skype");

                Log.d("BMTH", "name: " + name);
                Log.d("BMTH", "sortName: " + sortName);
                Log.d("BMTH", "url: " + url);

                Artist artist = new Artist();
                artist.name = name;
                artist.sortName = sortName;
                artist.url = url;

                mArtists.add(artist);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mArtists;
    }

    @Override
    public void run() {
        getConnection();
    }
}
