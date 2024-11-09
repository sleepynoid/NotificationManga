package com.example.notificationmanga;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MangaInfoActivity extends AppCompatActivity {
    ArrayList<Manga> mangaList = new ArrayList<>();
    MangaInfoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manga_item_activity);

        RecyclerView recyclerView = findViewById(R.id.mRecyclerViewMangaInfo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MangaInfoAdapter(this, mangaList);
        recyclerView.setAdapter(adapter);

//        mangaList.add(new Manga("1","test","mencari bug"));

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            try {
                // Fetch and convert manga JSON data
                String mangaJson = Manga.fetchManga("https://pastebin.com/raw/EV3wZywe");
                ArrayList<Manga> mangaArrayList = Manga.convertToArrayList(mangaJson);

                // Update UI on the main thread
                runOnUiThread(() -> {
                    MangaInfoActivity.this.mangaList.clear();
                    MangaInfoActivity.this.mangaList.addAll(mangaArrayList);
                    MangaInfoActivity.this.adapter.notifyDataSetChanged();
                });
            } catch (IOException e) {
                e.printStackTrace();
                // Optional: handle error on the main thread if needed
            }
        });
    }
}