package com.example.androidapp.main;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;

public class MainViewHolder3 extends RecyclerView.ViewHolder{
    public TextView textView;
    public MainViewHolder3(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.ScoreViewItem);
    }
}
