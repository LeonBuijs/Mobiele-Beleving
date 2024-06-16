package com.example.androidapp.achievements;
import androidx.recyclerview.widget.RecyclerView;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;

public class AchievementViewHolder extends RecyclerView.ViewHolder{
    public ImageView imageView;
    public TextView textName, textDescription;
    public CardView cardView;
    public AchievementViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.achievementimage);
        textName = itemView.findViewById(R.id.achievementName);
        textDescription = itemView.findViewById(R.id.achievementDescription);
        cardView = itemView.findViewById(R.id.achievement_container);
    }
}
