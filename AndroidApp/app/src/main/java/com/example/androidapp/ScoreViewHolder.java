package com.example.androidapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ScoreViewHolder extends RecyclerView.ViewHolder{
    public TextView textNumber, textName, textScore;
    public CardView cardView;
    public ScoreViewHolder(@NonNull View itemView) {
        super(itemView);
        textNumber = itemView.findViewById(R.id.textNumber);
        textName = itemView.findViewById(R.id.textName);
        textScore = itemView.findViewById(R.id.textScore);
        cardView = itemView.findViewById(R.id.main_container);
    }
}
