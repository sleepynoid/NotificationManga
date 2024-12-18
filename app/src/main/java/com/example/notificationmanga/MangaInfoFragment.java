package com.example.notificationmanga;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MangaInfoFragment extends Fragment {
    private ArrayList<Manga> mangaList = new ArrayList<>();
    private MangaInfoAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manga_info, container, false);

        // Set up RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.mRecyclerViewMangaInfo);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MangaInfoAdapter(getActivity(), mangaList);
        recyclerView.setAdapter(adapter);

        // Fetch manga data
        fetchMangaData();

        return view;
    }

    private void fetchMangaData() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            try {
                // Fetch and convert manga JSON data
                String mangaJson = Manga.fetchManga("https://pastebin.com/raw/EV3wZywe");
                ArrayList<Manga> mangaArrayList = Manga.convertToArrayList(mangaJson);

                // Update UI on the main thread
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        mangaList.clear();
                        mangaList.addAll(mangaArrayList);
                        adapter.notifyDataSetChanged();
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Optional: handle error
            }
        });
    }
}
