package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class achievements extends AppCompatActivity {

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
    }

    public void setMainScreen(View view){
        startActivity(new Intent(this, MainActivity.class));
    }
    public void setScoreScreen(View view){
        startActivity(new Intent(this, scoreview.class));
    }
    public void setAchievementScreen(View view){
    }
}