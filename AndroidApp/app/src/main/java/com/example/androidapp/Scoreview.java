package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Scoreview extends AppCompatActivity {
    RecyclerView recyclerView;
    private List<Score> scores = new ArrayList<>();
    private ScoreAdapter scoreAdapter;

    @Override
    protected void onCreate(Bundle savedInstancesState) {
        setTheme(R.style.JohanEnDeEenhoorn);
        super.onCreate(savedInstancesState);
        setContentView(R.layout.activity_scoreview);
        Spinner spinner = findViewById(R.id.dropdown_menu);
        displayItems();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayList<String> listOfItemsOfDropDown = new ArrayList<>();
        listOfItemsOfDropDown.add("Year");
        listOfItemsOfDropDown.add("Month");
        listOfItemsOfDropDown.add("Day");
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,listOfItemsOfDropDown);
//        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        //todo maak een layout voor de dropdown menu inplaats van de vooraf gemaakte simple_spinner_item.
        spinner.setAdapter(adapter);

    }

    public void setMainScreen(View view){
        startActivity(new Intent(this, MainActivity.class));
    }
    public void setScoreScreen(View view){
    }
    public void setAchievementScreen(View view){
        startActivity(new Intent(this, Achievements.class));
    }

    private void sortScores(){
        scores.sort(new ScoreComparator());
        for (Score score : scores) {
            score.setNum(scores.indexOf(score)+1);
        }
    }

    private void displayItems() {
        scores.add(new Score(1, "Leon Buijs", 1000));
        scores.add(new Score(1, "Jasper Trommelen", 999));
        scores.add(new Score(1, "Sybe Tienstra", 750));
        scores.add(new Score(1, "Tiemen Breugelmans", 888));
        scores.add(new Score(1, "Mohammed Lahlal", 739));
        sortScores();
        recyclerView = findViewById(R.id.recyclerViewScores);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.scoreAdapter = new ScoreAdapter(getApplicationContext(), scores);
        scoreAdapter.setTheme(2); // thema 1 = Cobra, thema 2 = Johan en de eenhoorn
        recyclerView.setAdapter(this.scoreAdapter);
    }
}