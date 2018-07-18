package com.example.evgeny.setlist_mobile.net;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.evgeny.setlist_mobile.Artist;
import com.example.evgeny.setlist_mobile.search.SearchSetlist;

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

public class SetlistConnect {

    static SetlistConnect setlistConnect;

    private String artistName="artist";

    String response="";

    private List<Artist> mArtists = new ArrayList<>();

    SetListListener artitstListListener;

    private SetlistConnect(SetListListener artitstListListener) {
        this.artitstListListener = artitstListListener;
    }

    public static SetlistConnect getInstance(SetListListener artitstListListener) {
        if (setlistConnect!=null) {
           return setlistConnect;
        } else {
            setlistConnect = new SetlistConnect(artitstListListener);
            return  setlistConnect;
        }
    }

    public void setArtistName(String artistName) {
        this.artistName=artistName;
    }

    private String getStringURL() {
        return "https://api.setlist.fm/rest/1.0/search/artists?artistName=" + artistName + "&p=1&sort=sortName";
    }

    //private final String stringURL = "https://api.setlist.fm/rest/1.0/search/artists?artistName=Metallica&p=1&sort=sortName";

    public void getConnection() {
        try {
            //URL url = new URL(stringURL);

            URL url = new URL(getStringURL());

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty ("x-api-key", "5f26897a-5ead-4985-8c6e-b8d427eb757e");
            httpURLConnection.setRequestProperty ("Accept", "application/json");
            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.d("BMTH1", "responseCode is OK: " + responseCode);

                InputStream inputStream = httpURLConnection.getInputStream();

                String response = getStringFromInputStream(inputStream);

                Log.d("BMTH1", "response: " + response);

                this.response = response;



            } else {
                Log.d("BMTH1", "responseCode is NOT OK: " + responseCode);
            }
            httpURLConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
     * @param response ответ от сервера
     */
    private void unParse(String response) {
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

    }

    public interface SetListListener {
        void getSetList(List<Artist> artists);
    }
}
