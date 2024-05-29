package com.example.androidapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class scoreview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setContentView(R.layout.activity_scoreview);
        Spinner spinner = findViewById(R.id.dropdown_menu);
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
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        //todo maak een layout voor de dropdown menu inplaats van de vooraf gemaakte simple_spinner_item.
        spinner.setAdapter(adapter);
    }
}