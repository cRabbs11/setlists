package com.example.evgeny.setlist_mobile.net;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.evgeny.setlist_mobile.model.Artist;

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

public class SetlistConnect implements Runnable {

    private Thread thread;
    private Bundle data;

    private String response="";
    private String request;
    private String URL;

    private AnswerListener answerListener;

    private List<Artist> mArtists = new ArrayList<>();

    Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            answerListener.getAnswer(response);
        }
    };

    /**
     * Создание нового потока для запроса на сервер setlist
     * @param request тип запроса
     * @param data данные для запроса
     */
    public SetlistConnect(String request, Bundle data, AnswerListener answerListener) {
        Log.d("BMTH", "new Thread... ");
        this.answerListener = answerListener;
        this.request = request;
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
                URL=getArtists();
                break;
            case "getSetlists":
                URL=getSetlists();
                break;
        }
    }

    /**
     * Строит URL для поиска сетлистов
     */
    private String getSetlists() {
        Log.d("BMTH", "getSetlists... ");
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
        URL = "https://api.setlist.fm/rest/1.0/artist/" + mbid + "/setlists?p=" + page;
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

            URL url = new URL(URL);

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
                Log.d("BMTH", "response: " + response);
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

    @Override
    public void run() {
        response = getConnection();
        mHander.sendEmptyMessage(1);
    }

    public interface AnswerListener {
        void getAnswer(String answer);
    }
}
