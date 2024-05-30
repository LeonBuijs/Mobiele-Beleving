package com.example.androidapp;

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Achievements extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Achievement> achievements = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_achievements);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scores), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        displayItems();
    }

    public void setMainScreen(View view){
        startActivity(new Intent(this, MainActivity.class));
    }
    public void setScoreScreen(View view){
        startActivity(new Intent(this, Scoreview.class));
    }
    public void setAchievementScreen(View view){
    }

    private void displayItems() {
        achievements.add(new Achievement(R.drawable.eerste_slag1, "Test", "Test", false));
        achievements.add(new Achievement(R.drawable.verbonden_krijger2, "Test2", "Test2", true));
        achievements.add(new Achievement(R.drawable.verbonden_krijger2, "Test3", "Test3", false));

        recyclerView = findViewById(R.id.recyclerViewAchievements);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(new AchievementAdapter(getApplicationContext(), achievements));
    }
}