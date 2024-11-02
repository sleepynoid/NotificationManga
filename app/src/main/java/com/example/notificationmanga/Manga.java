package com.example.notificationmanga;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class Manga {
    public String id;
    public String title;
    public String description;
    public String coverArt;
    public Chapter latestChap;
    public Manga(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public static String fetchManga(String url) throws IOException {
        HttpsURLConnection urlConnection = null;
        InputStream stream = null;
        String result = null;
        try {
            urlConnection = (HttpsURLConnection) new URL(url).openConnection();
            urlConnection.setRequestMethod("GET");

            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode != 200) throw new IOException("Http error code "+responseCode);

            stream = urlConnection.getInputStream();
            if (stream != null) {
                // unknown code, DO NOT CHANGE or ERROR
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        stream, StandardCharsets.UTF_8));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                result = builder.toString();
                Log.i("MangaFetching", result);
            }
        } finally {
            if (stream != null) stream.close();
            if (urlConnection != null)  urlConnection.disconnect();
        }
        return result;
    }

    public static ArrayList<Manga> convertToArrayList(String str) {
        if (str == null || str.trim().isEmpty()) {
            Log.e("MangaFetching", "Received an empty or blank JSON response.");
            return null; // or handle it as you need
        }
        ArrayList<Manga> mangaList = new ArrayList<Manga>();
        try {
            JSONObject jsonResponse = new JSONObject(str);
            JSONArray dataArray = jsonResponse.getJSONArray("data");
            StringBuilder displayText = new StringBuilder();
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject mangaObject = dataArray.getJSONObject(i);
                String title = mangaObject.getString("title");
                String description = mangaObject.getString("description");
//                String coverArtAlt = mangaObject.getJSONObject("cover_art").getString("alt");
                mangaList.add(new Manga(mangaObject.getString("id"),title,description));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return mangaList;
    }
}
