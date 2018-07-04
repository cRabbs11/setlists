package com.example.evgeny.setlist_mobile;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Evgeny on 25.05.2018.
 */

public class SetlistConnection extends AsyncTask {

    private final String stringURL = "https://api.setlist.fm/rest/1.0/search/artists?artistName=Metallica&p=1&sort=sortName";

    private void getConnection() {
        try {
            URL url = new URL(stringURL);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty ("x-api-key", "5f26897a-5ead-4985-8c6e-b8d427eb757e");
            httpURLConnection.setRequestProperty ("Accept", "application/json");
            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.d("BMTH", "responseCode is OK: " + responseCode);

                InputStream inputStream = httpURLConnection.getInputStream();

                String response = getStringFromInputStream(inputStream);

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
    protected Object doInBackground(Object[] objects) {
        getConnection();
        return null;
    }
}
