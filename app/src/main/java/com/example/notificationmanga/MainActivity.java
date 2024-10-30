package com.example.notificationmanga;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private TextView jsonTextView; // TextView to display JSON response

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jsonTextView = findViewById(R.id.jsonTextView);
        Button fetchDataButton = findViewById(R.id.fetchDataButton);

        // Set up button click listener
        fetchDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Run the HTTP request task on button click
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(new HTTPReqTask(MainActivity.this));
            }
        });
    }

    // Helper method to update TextView
    public void updateTextView(String text) {
        runOnUiThread(() -> jsonTextView.setText(text));
    }

    private static class HTTPReqTask implements Runnable {
        private final MainActivity activity;

        HTTPReqTask(MainActivity activity) {
            this.activity = activity;
        }

        @Override
        public void run() {
            HttpURLConnection urlConnection = null;
            StringBuilder response;

            // GET Request to DummyJSON
            try {
//                URL url = new URL("https://dummyjson.com/users/1"); // Example for a single user
//                URL url = new URL("https://api.mangadex.org/manga/3486c56e-47db-4d62-a9b4-71ea44acbaec"); // Example for a single user
                URL url = new URL("https://pastebin.com/raw/FWQaLvcX");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                int code = urlConnection.getResponseCode();
                if (code != 200) {
                    throw new IOException("Invalid response from server: " + code);
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(), StandardCharsets.UTF_8));
                response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // Parse JSON response
//                JSONObject jsonResponse = new JSONObject(response.toString());
//                Log.i("GET Response", jsonResponse.toString());
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray dataArray = jsonResponse.getJSONArray("data");

                StringBuilder displayText = new StringBuilder();
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject mangaObject = dataArray.getJSONObject(i);
                    String title = mangaObject.getString("title");
                    String description = mangaObject.getString("description");
                    String coverArtAlt = mangaObject.getJSONObject("cover_art").getString("alt");

                    displayText.append("Title: ").append(title).append("\n")
                            .append("Description: ").append(description).append("\n")
                            .append("Cover Alt: ").append(coverArtAlt).append("\n\n");

                    // Add LatestChapters if present
                    JSONArray latestChaptersArray = mangaObject.getJSONArray("latestChapters");
                    if (latestChaptersArray.length() > 0) {
                        displayText.append("Latest Chapters:\n");
                        for (int j = 0; j < latestChaptersArray.length(); j++) {
                            displayText.append("  - ").append(latestChaptersArray.getString(j)).append("\n");
                        }
                    } else {
                        displayText.append("Latest Chapters: None\n");
                    }

                    displayText.append("\n"); // Separate entries
                }

                // Update the TextView with GET response
                activity.updateTextView("GET Response:\n" + displayText.toString());

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            // POST Request to DummyJSON
            try {
                JSONObject postData = new JSONObject();
                postData.put("name", "John Doe");
                postData.put("email", "johndoe@example.com");

                URL url = new URL("https://dummyjson.com/users/add"); // Example POST endpoint
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                        urlConnection.getOutputStream(), StandardCharsets.UTF_8));
                writer.write(postData.toString());
                writer.flush();
                writer.close();

                int code = urlConnection.getResponseCode();
                if (code != 200) {
                    throw new IOException("Invalid response from server: " + code);
                }

                response = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(), StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // Parse JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                Log.i("POST Response", jsonResponse.toString());

                // Update the TextView with POST response
                activity.updateTextView("POST Response:\n" + jsonResponse.toString(4));

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }
    }
}
