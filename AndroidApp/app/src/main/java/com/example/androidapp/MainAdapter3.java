package com.example.androidapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainAdapter3 extends RecyclerView.Adapter<MainViewHolder3>{
    private Context context;
    private int theme = R.layout.score_main_item;
    private MainViewHolder3 mainViewHolder3;
    private SelectListener listener;

    public MainAdapter3(Context context, SelectListener listener) {
        this.context = context;
        this.listener = listener;
    }
    @NonNull
    @Override
    public MainViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mainViewHolder3 = new MainViewHolder3(LayoutInflater.from(context).inflate(this.theme, parent, false));
        return mainViewHolder3;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder3 holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void setTheme(int theme) {
        if (theme == 1) {
            this.theme = R.layout.score_main_item;
        } else if (theme == 2){
            this.theme = R.layout.score_main_item_theme2;
        }
    }

    public void setScore(String score){
        mainViewHolder3.textView.setText(score);

    }

    public MainViewHolder3 getMainViewHolder3() {
        return mainViewHolder3;
    }

    public void setMainViewHolder3(MainViewHolder3 mainViewHolder3) {
        this.mainViewHolder3 = mainViewHolder3;
    }

}
