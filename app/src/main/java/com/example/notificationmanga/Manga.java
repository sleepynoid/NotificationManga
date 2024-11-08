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

    public Manga(String id, String title, String description, Chapter latestChap) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.latestChap = latestChap;
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
//                Log.i("MangaFetching", result);
                System.out.println("fetching manga"+result);
            }
        } finally {
            if (stream != null) stream.close();
            if (urlConnection != null)  urlConnection.disconnect();
        }
        return result;
    }

    public static ArrayList<Manga> convertToArrayList(String str) {
        if (str == null || str.trim().isEmpty()) {
            System.out.println("Received an empty or blank JSON response.");
            return null; // or handle it as you need
        }
        ArrayList<Manga> mangaList = new ArrayList<>();
        try {
            JSONObject jsonResponse = new JSONObject(str);
            JSONArray dataArray = jsonResponse.getJSONArray("data");

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject mangaObject = dataArray.getJSONObject(i);
                String title = mangaObject.getString("title");
                String description = mangaObject.getString("description");
                String id = mangaObject.getString("id");

                // Check if latestChapters is an array and has at least one element
                Chapter latestChap = null;
                JSONArray latestChaptersArray = mangaObject.getJSONArray("latestChapters");
                if (latestChaptersArray.length() > 0) {
                    JSONObject chapObject = latestChaptersArray.getJSONObject(0);
                    String chapterId = chapObject.getString("id");
                    String chapterName = chapObject.getString("name");
                    String chapterNum = chapObject.getString("chapter");
                    latestChap = new Chapter(chapterId, chapterName, chapterNum);
                }

                // Create a Manga object with the latest chapter information
                mangaList.add(new Manga(id, title, description, latestChap));
            }
        } catch (JSONException e) {
            throw new RuntimeException("Error parsing JSON", e);
        }
        return mangaList;
    }
}