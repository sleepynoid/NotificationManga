package com.example.notificationmanga;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class tester extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        RecyclerView recyclerView = findViewById(R.id.mRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample data for testing
        ArrayList<Manga> mangaList = new ArrayList<>();
        mangaList.add(new Manga("1", "Title 1", "Description 1"));
        mangaList.add(new Manga("2", "Title 2", "Description 2"));

        testAdapter adapter = new testAdapter(this, mangaList);
        recyclerView.setAdapter(adapter);
    }
}
