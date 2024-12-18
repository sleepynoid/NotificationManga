package com.example.notificationmanga;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MangaFragment extends Fragment {
    private ArrayList<Manga> mangaList = new ArrayList<>();
    private MangaAdapter adapter;

    public MangaFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manga, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.mRecyclerView);
        Button updateButton = rootView.findViewById(R.id.updateButton);
        Button readAllButton = rootView.findViewById(R.id.readAllButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setVisibility(View.GONE);

        adapter = new MangaAdapter(requireContext(), mangaList);
        recyclerView.setAdapter(adapter);

        // Fetch Manga Data
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Fetch());

        // Update Button Logic
        updateButton.setOnClickListener(v -> {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new Fetch());
            recyclerView.setVisibility(View.VISIBLE);
        });

        // Read All Button Logic
        readAllButton.setOnClickListener(v -> {
            if (mangaList.isEmpty()) {
                Toast.makeText(requireContext(), "There is no Manga", Toast.LENGTH_SHORT).show();
                return;
            }
            mangaList.clear();
            adapter.notifyDataSetChanged();
            Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show();
        });

        return rootView;
    }

    private class Fetch implements Runnable {
        @Override
        public void run() {
            String mangaJson;
            ArrayList<Manga> fetchedMangaList;
            try {
                mangaJson = Manga.fetchManga("https://pastebin.com/raw/EV3wZywe");
                fetchedMangaList = Manga.convertToArrayList(mangaJson);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            requireActivity().runOnUiThread(() -> {
                mangaList.clear();
                mangaList.addAll(fetchedMangaList);
                adapter.notifyDataSetChanged();
            });
        }
    }
}
