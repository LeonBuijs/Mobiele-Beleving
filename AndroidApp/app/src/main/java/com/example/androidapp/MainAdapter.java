package com.example.androidapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainViewHolder>{
    private Context context;
    private List<Score> list;

    public MainAdapter(Context context, List<Score> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(context).inflate(R.layout.own_score_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        holder.textOwnNumber.setText(String.valueOf(list.get(position).getNum()));
        holder.textDate.setText(list.get(position).getDateTime());
        holder.textOwnScore.setText(String.valueOf(list.get(position).getScore()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
