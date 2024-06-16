package com.example.androidapp.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.score.Score;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainViewHolder>{
    private Context context;
    private List<Score> list;
    private int theme = R.layout.own_score_item;

    public MainAdapter(Context context, List<Score> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(context).inflate(this.theme, parent, false));
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

    public void setTheme(int theme) {
        if (theme == 1) {
            this.theme = R.layout.own_score_item;
        } else if (theme == 2){
            this.theme = R.layout.own_score_item_theme2;
        }
    }
}
