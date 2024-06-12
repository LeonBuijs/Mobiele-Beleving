package com.example.androidapp;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MainViewHolder2 extends RecyclerView.ViewHolder{
    public EditText editText;
    public MainViewHolder2(@NonNull View itemView) {
        super(itemView);
        editText = itemView.findViewById(R.id.editTextNumber);
    }
}
