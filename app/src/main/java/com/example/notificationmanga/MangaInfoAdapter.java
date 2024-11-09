package com.example.notificationmanga;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MangaInfoAdapter extends RecyclerView.Adapter<MangaInfoAdapter.MyViewHolder> {
    Context context;
    ArrayList<Manga> mangas;

    public MangaInfoAdapter(Context context, ArrayList<Manga> mangas) {
        this.context = context;
        this.mangas = mangas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.manga_item_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Manga manga = mangas.get(position);
        holder.title.setText(manga.title);
        holder.description.setText(manga.description);
        holder.img.setImageResource(R.drawable.ic_launcher_background);
    }

    @Override
    public int getItemCount() {
        return mangas.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title,description,chapter;
        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.titleInfoManga);
            this.description = itemView.findViewById(R.id.descriptionInfoManga);
            this.img = itemView.findViewById(R.id.imgInfoManga);
        }
    }
}
