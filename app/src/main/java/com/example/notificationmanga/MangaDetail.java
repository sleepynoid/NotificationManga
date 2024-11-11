package com.example.notificationmanga;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MangaDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manga_detail);

        int imageResId = getIntent().getIntExtra("MANGA_IMAGE", -1);
        ImageView img = findViewById(R.id.imgDetailManga);
        img.setImageResource(imageResId);

        TextView title = findViewById(R.id.titleDetailManga);
        TextView description = findViewById(R.id.descriptionDetailManga);

        title.setText(getIntent().getStringExtra("MANGA_TITLE"));
        description.setText(getIntent().getStringExtra("MANGA_DESCRIPTION"));

        Button read = findViewById(R.id.LinkReadButton);
        Button share = findViewById(R.id.LinkShareButton);

        // Set up Share button to share the manga title and description
        share.setOnClickListener(v -> shareMangaDetails(getIntent().getStringExtra("MANGA_TITLE"), getIntent().getStringExtra("MANGA_DESCRIPTION")));

        // Set up Read button to open the link in a browser
        read.setOnClickListener(v -> openReadLink("https://mangadex.org/"));
    }
    private void shareMangaDetails(String mangaTitle, String mangaDescription) {
        String shareText = "Check out this manga: " + mangaTitle + "\n\n" + mangaDescription;

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

        // Check if there's an app that can handle this intent before starting it
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(shareIntent, "Share Manga"));
        }
    }

    private void openReadLink(String readLink) {
        if (readLink != null && !readLink.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(readLink));
            intent.setPackage("com.android.chrome");
            startActivity(intent);
        } else {
            Toast.makeText(this, "No link available", Toast.LENGTH_SHORT).show();
        }
    }
}