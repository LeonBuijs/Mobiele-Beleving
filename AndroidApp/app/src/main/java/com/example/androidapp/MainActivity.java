package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Score> ownScores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        displayItems();
    }

    public void setMainScreen(View view){
    }
    public void setScoreScreen(View view){
        startActivity(new Intent(this, Scoreview.class));
    }
    public void setAchievementScreen(View view){
        startActivity(new Intent(this, Achievements.class));
    }

    private void sortScores(){
        ownScores.sort(new ScoreComparator());
        for (Score score : ownScores) {
            score.setNum(ownScores.indexOf(score)+1);
        }
    }

    private void displayItems() {
        ownScores.add(new Score(1, "Test", 1000));
        ownScores.add(new Score(1, "Test", 873));
        ownScores.add(new Score(1, "Test", 328));
        ownScores.add(new Score(1, "Test", 255));
        ownScores.add(new Score(1, "Test", 987));
        sortScores();
        recyclerView = findViewById(R.id.recycleViewForMainScreen);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(new MainAdapter(getApplicationContext(), ownScores));
    }
}