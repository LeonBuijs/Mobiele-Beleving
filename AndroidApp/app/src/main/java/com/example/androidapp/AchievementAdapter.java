package com.example.androidapp;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementViewHolder>{
    private Context context;
    private List<Achievement> list;
    private int theme;

    public AchievementAdapter(Context context, List<Achievement> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AchievementViewHolder(LayoutInflater.from(context).inflate(this.theme, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {
        holder.imageView.setImageResource(list.get(position).getImage());
        if (!list.get(position).isAchieved()){
            ImageView imageView = holder.imageView;

            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);

            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

            imageView.setColorFilter(filter);
        }

        holder.textName.setText(list.get(position).getName());
        holder.textDescription.setText(String.valueOf(list.get(position).getDescription()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void setTheme(int theme) {
        if (theme == 1) {
            this.theme = R.layout.achievement_item;
        } else if (theme == 2){
            this.theme = R.layout.achievement_item_theme2;
        }
    }
}
