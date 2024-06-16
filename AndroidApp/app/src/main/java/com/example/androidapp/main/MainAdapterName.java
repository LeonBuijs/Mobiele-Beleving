package com.example.androidapp.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.SelectListener;

public class MainAdapterName extends RecyclerView.Adapter<MainViewHolderName> {
    private Context context;
    private int theme = R.layout.name_item;
    private MainViewHolderName mainViewHolderName;
    private SelectListener listener;

    public MainAdapterName(Context context, SelectListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MainViewHolderName onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mainViewHolderName = new MainViewHolderName(LayoutInflater.from(context).inflate(this.theme, parent, false));
        return mainViewHolderName;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolderName holder, int position) {
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked2();
            }
        });

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void setTheme(int theme) {
        if (theme == 1) {
            this.theme = R.layout.name_item;
        } else if (theme == 2) {
            this.theme = R.layout.name_item_theme2;
        }
    }

    public MainViewHolderName getMainViewHolderName() {
        return mainViewHolderName;
    }

    public void setMainViewHolder2(MainViewHolderName mainViewHolderName) {
        this.mainViewHolderName = mainViewHolderName;
    }

}
