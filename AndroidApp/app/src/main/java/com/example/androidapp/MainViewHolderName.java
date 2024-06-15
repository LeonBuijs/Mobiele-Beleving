package com.example.androidapp;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainViewHolderName extends RecyclerView.ViewHolder{
    public EditText editText;
    public Button button;
    public MainViewHolderName(@NonNull View itemView) {
        super(itemView);
        editText = itemView.findViewById(R.id.editTextName);
        button = itemView.findViewById(R.id.confirmButton);
    }
}
