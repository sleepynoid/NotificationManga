package com.example.notificationmanga;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button MenuOne = findViewById(R.id.MenuOnebutton);
        MenuOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intentMenuOne = new Intent(MainActivity.this, MangaActivity.class);
                Intent intentMenuOne = new Intent(MainActivity.this, MangaActivity.class);
                startActivity(intentMenuOne);
            }
        });

        Button MenuTwo = findViewById(R.id.MenuTwoButton);
        MenuTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMenuTwo = new Intent(MainActivity.this, MangaInfoActivity.class);
                startActivity(intentMenuTwo);
            }
        });
    }
}
