package com.example.notificationmanga;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MangaActivity extends AppCompatActivity {
    ArrayList<Manga> mangaList = new ArrayList<>();
    MangaAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manga_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = findViewById(R.id.mRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVisibility(View.GONE);
        Button updateButton = findViewById(R.id.updateButton);

        // Sample data for testing
//        ArrayList<Manga> mangaList = new ArrayList<>();
//        mangaList.add(new Manga("1", "Centuria", "Chapter 28"));
//        mangaList.add(new Manga("2", "Rebuild World", "Chapter 63"));
//        mangaList.add(new Manga("3", "Super Ball Girl", "Chapter 38"));
//        ArrayList<Manga> mangaList = new ArrayList<>();
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Fetch(MangaActivity.this));

        adapter = new MangaAdapter(this, mangaList);
        recyclerView.setAdapter(adapter);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExecutorService service = Executors.newSingleThreadExecutor();
                service.execute(new Fetch(MangaActivity.this));
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }
    private static class Fetch implements Runnable {
        final MangaActivity mangaActivity;
        public Fetch(MangaActivity activity) {
            this.mangaActivity = activity;
        }
        @Override
        public void run() {
            String mangaJson;
            ArrayList<Manga> mangaArrayList;
            try {
                mangaJson = Manga.fetchManga("https://pastebin.com/raw/EV3wZywe");
                mangaArrayList = Manga.convertToArrayList(mangaJson);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ArrayList<Manga> finalMangaArrayList = mangaArrayList;
            mangaActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Clear the current list and add new items
                    mangaActivity.mangaList.clear();
                    mangaActivity.mangaList.addAll(finalMangaArrayList);

                    // Notify the adapter that the data has changed
                    mangaActivity.adapter.notifyDataSetChanged();
//                    Toast.makeText(mainActivity, "Success", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
