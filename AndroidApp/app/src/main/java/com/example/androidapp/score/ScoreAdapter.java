package com.example.androidapp.score;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;

import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreViewHolder>{
    private Context context;
    private List<Score> list;
    private int theme = R.layout.score_item;

    public ScoreAdapter(Context context, List<Score> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ScoreViewHolder(LayoutInflater.from(context).inflate(this.theme, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        holder.textNumber.setText(String.valueOf(list.get(position).getNum()));
        holder.textName.setText(list.get(position).getName());
        holder.textScore.setText(String.valueOf(list.get(position).getScore()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setTheme(int theme) {
        if (theme == 1) {
            this.theme = R.layout.score_item;
        } else if (theme == 2){
            this.theme = R.layout.score_item_theme2;
        }
    }
}
