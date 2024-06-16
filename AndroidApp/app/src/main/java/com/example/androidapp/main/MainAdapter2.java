package com.example.androidapp.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.SelectListener;

public class MainAdapter2 extends RecyclerView.Adapter<MainViewHolder2>{
    private Context context;
    private int theme = R.layout.connection_item1;
    private MainViewHolder2 mainViewHolder2;
    private SelectListener listener;

    public MainAdapter2(Context context, SelectListener listener) {
        this.context = context;
        this.listener = listener;
    }
    @NonNull
    @Override
    public MainViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mainViewHolder2 = new MainViewHolder2(LayoutInflater.from(context).inflate(this.theme, parent, false));
        return mainViewHolder2;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder2 holder, int position) {
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void setTheme(int theme) {
        if (theme == 1) {
            this.theme = R.layout.connection_item1;
        } else if (theme == 2){
            this.theme = R.layout.connection_item1_theme2;
        }
    }

    public MainViewHolder2 getMainViewHolder2() {
        return mainViewHolder2;
    }

    public void setMainViewHolder2(MainViewHolder2 mainViewHolder2) {
        this.mainViewHolder2 = mainViewHolder2;
    }

}
