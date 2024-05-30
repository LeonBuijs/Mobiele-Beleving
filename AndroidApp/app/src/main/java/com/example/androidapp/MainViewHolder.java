package com.example.androidapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MainViewHolder extends RecyclerView.ViewHolder{
    public TextView textOwnNumber, textDate, textOwnScore;
    public CardView cardView;
    public MainViewHolder(@NonNull View itemView) {
        super(itemView);
        textOwnNumber = itemView.findViewById(R.id.textOwnNumber);
        textDate = itemView.findViewById(R.id.textDate);
        textOwnScore = itemView.findViewById(R.id.textOwnScore);
        cardView = itemView.findViewById(R.id.own_score_item);
    }
}
