package com.example.notificationmanga;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.MyViewHolder> {
    Context context;
    ArrayList<Manga> mangaArrayList;

    public MangaAdapter(Context context, ArrayList<Manga> mangaArrayList) {
        this.context = context;
        this.mangaArrayList = mangaArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.manga_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Manga manga = mangaArrayList.get(position);
        holder.textView.setText(manga.title);

        holder.textView2.setText(String.format("Chapter %s %s", manga.latestChap.chapter, manga.latestChap.name));
//        holder.textView3.setText(manga.id);

        // Assuming coverArt is a drawable resource ID; if it's a URL, use a library like Glide/Picasso
//        int imageResource = context.getResources().getIdentifier(manga.coverArt, "drawable", context.getPackageName());
        holder.imageView.setImageResource(R.drawable.ic_launcher_background);

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, "Clicked: " + manga.title, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(context, MangaDetail.class);
            intent.putExtra("MANGA_ID", manga.id);
            intent.putExtra("MANGA_TITLE", manga.title);
            intent.putExtra("MANGA_DESCRIPTION", manga.description);
            intent.putExtra("MANGA_IMAGE",R.drawable.ic_launcher_background);

            context.startActivity(intent);
        });

        holder.readButton.setOnClickListener(v -> {
            Toast.makeText(context, "Read " + manga.title, Toast.LENGTH_SHORT).show();
            // Remove item from the list and notify the adapter
            mangaArrayList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mangaArrayList.size());
        });
    }

    @Override
    public int getItemCount() {
        return mangaArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView, textView2, textView3;
        Button readButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
//            textView3 = itemView.findViewById(R.id.textView3);
            readButton = itemView.findViewById(R.id.readbutton);
        }
    }
}
