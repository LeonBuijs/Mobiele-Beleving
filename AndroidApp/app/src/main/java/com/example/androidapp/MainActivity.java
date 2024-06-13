package com.example.androidapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.Mqtt.MQTTClient;
import com.example.androidapp.Mqtt.MqttMR;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity implements MqttMR, SelectListener {
    private MainAdapter mainAdapter;
    private MainAdapter2 mainAdapter2;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private List<Score> ownScores = new ArrayList<>();

    private final String USERNAME = "MobieleBelevingA5";
    private final String PASSWORD = "liefsSybeA5";
    private final String TOPIC = "MobieleBelevingA5";
    private MQTTClient mqttClient;
    private SharedPreferences sharedPreferences;
    private boolean messageRecieved = false;
    private String lastRecieved = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("THEME","1");
        editor.apply();
        if (sharedPreferences != null) {
            if (sharedPreferences.getString("THEME", null).equals("1")) {
                setTheme(R.style.Cobra);
            } else if (sharedPreferences.getString("THEME", null).equals("2")) {
                setTheme(R.style.JohanEnDeEenhoorn);
            }
        } else {
            System.out.println("null");
            setTheme(R.style.Cobra);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        displayItems();

        mqttClient = new MQTTClient(getApplicationContext(), this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void TESTMQTTTEMP(View view) {
        mqttClient.publishMessage("THIS IS A TEST");

    }

    public void setMainScreen(View view) {
    }

    public void setScoreScreen(View view) {
        startActivity(new Intent(this, Scoreview.class));
    }

    public void setAchievementScreen(View view) {
        startActivity(new Intent(this, Achievements.class));
    }

    private void sortScores() {
        ownScores.sort(new ScoreComparator());
        for (Score score : ownScores) {
            score.setNum(ownScores.indexOf(score) + 1);
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

        mainAdapter = new MainAdapter(getApplicationContext(), ownScores);
        mainAdapter.setTheme(1); // thema 1 = Cobra, thema 2 = Johan en de eenhoorn
        recyclerView.setAdapter(mainAdapter);

        recyclerView2 = findViewById(R.id.mainScreenInfo);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        mainAdapter2 = new MainAdapter2(getApplicationContext(), this);
        mainAdapter2.setTheme(1); // thema 1 = Cobra, thema 2 = Johan en de eenhoorn
        recyclerView2.setAdapter(mainAdapter2);
    }


    @Override
    public void messageReceived(String topic, MqttMessage payload) {
        try {
            String message = payload.toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("TEST", message);
            editor.apply();
            Toast.makeText(this, sharedPreferences.getString("TEST", null), Toast.LENGTH_LONG).show();
            this.messageRecieved = true;
            this.lastRecieved = sharedPreferences.getString("TEST", null);
            if (message.equals("theme1") || message.equals("theme2")) {
                getThemeFromTopic(message);
            }
        } catch (Exception e) {
            System.out.println("Message Received went wrong");
            e.printStackTrace();
        }
    }


    public void connectToTopic() {
        String number = String.valueOf(mainAdapter2.getMainViewHolder2().editText.getText());
        System.out.println(number);
        mqttClient.subscribe(("MobieleBelevingA5/" + number));
        mqttClient.setTOPIC(("MobieleBelevingA5/" + number));
        mqttClient.publishMessage("getTheme");

    }

    public void getThemeFromTopic(String theme) {
//        if (theme.equals("theme1")) {
//            setTheme(R.style.Cobra);
//        } else if (theme.equals("theme2")) {
//            setTheme(R.style.JohanEnDeEenhoorn);
//        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (theme.equals("theme1")) {
            editor.putString("THEME", "1");
        } else if (theme.equals("theme2")) {
            editor.putString("THEME", "2");
        }
        editor.apply();
        recreate();

    }

    @Override
    public void onItemClicked() {
        connectToTopic();
    }
}