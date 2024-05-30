package com.example.androidapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementViewHolder>{
    private Context context;
    private List<Achievement> list;

    public AchievementAdapter(Context context, List<Achievement> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AchievementViewHolder(LayoutInflater.from(context).inflate(R.layout.achievement_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {
        holder.imageView.setImageResource(list.get(position).getImage());
        holder.textName.setText(list.get(position).getName());
        holder.textDescription.setText(String.valueOf(list.get(position).getDescription()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
