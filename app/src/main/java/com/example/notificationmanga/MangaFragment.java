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

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MangaFragment extends Fragment {
    private ArrayList<Manga> mangaList = new ArrayList<>();
    private MangaAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manga, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.mRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setVisibility(View.GONE);

        adapter = new MangaAdapter(getActivity(), mangaList);
        recyclerView.setAdapter(adapter);

        Button updateButton = view.findViewById(R.id.updateButton);
        updateButton.setOnClickListener(v -> {
            ExecutorService service = Executors.newSingleThreadExecutor();
            service.execute(new MangaFragment.Fetch(this));
            recyclerView.setVisibility(View.VISIBLE);
        });

        Button readAllButton = view.findViewById(R.id.readAllButton);
        readAllButton.setOnClickListener(v -> {
            if (mangaList.isEmpty()) {
                Toast.makeText(getActivity(), "There is no Manga", Toast.LENGTH_SHORT).show();
                return;
            }
            mangaList.clear();
            adapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    public static class Fetch implements Runnable {
        final MangaFragment mangaFragment;

        public Fetch(MangaFragment fragment) {
            this.mangaFragment = fragment;
        }

        @Override
        public void run() {
            ArrayList<Manga> mangaArrayList;
            try {
                String mangaJson = Manga.fetchManga("https://pastebin.com/raw/EV3wZywe");
                mangaArrayList = Manga.convertToArrayList(mangaJson);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            ArrayList<Manga> finalMangaArrayList = mangaArrayList;
            mangaFragment.getActivity().runOnUiThread(() -> {
                mangaFragment.mangaList.clear();
                mangaFragment.mangaList.addAll(finalMangaArrayList);
                mangaFragment.adapter.notifyDataSetChanged();
            });
        }
    }
}
