package com.example.notificationmanga;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button to navigate to MangaActivity (hosts MangaFragment)
        Button menuOneButton = findViewById(R.id.MenuOnebutton);
        menuOneButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MangaActivity.class);
            startActivity(intent);
        });

        // Button to navigate to MangaInfoActivity
        Button menuTwoButton = findViewById(R.id.MenuTwoButton);
        menuTwoButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MangaInfoActivity.class);
            startActivity(intent);
        });
    }
}
