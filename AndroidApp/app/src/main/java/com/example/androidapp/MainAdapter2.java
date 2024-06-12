package com.example.androidapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainAdapter2 extends RecyclerView.Adapter<MainViewHolder2>{
    private Context context;
    private int theme = R.layout.connection_item1;

    public MainAdapter2(Context context) {
        this.context = context;
    }
    @NonNull
    @Override
    public MainViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder2(LayoutInflater.from(context).inflate(this.theme, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder2 holder, int position) {}

    @Override
    public int getItemCount() {
        return 1;
    }

    public void setTheme(int theme) {
        if (theme == 1) {
            this.theme = R.layout.connection_item1;
        } else if (theme == 2){
            this.theme = R.layout.own_score_item_theme2; //todo
        }
    }
}
