package com.example.notificationmanga;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // Set up buttons and their click listeners
        Button menuOneButton = view.findViewById(R.id.MenuOnebutton);
        menuOneButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MangaActivity.class);
            startActivity(intent);
        });

        Button menuTwoButton = view.findViewById(R.id.MenuTwoButton);
        menuTwoButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MangaInfoActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
