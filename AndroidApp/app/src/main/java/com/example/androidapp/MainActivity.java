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
    private MainAdapterName mainAdapterName;
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
    private String pairingCode = "";
    private int appTheme = 1;
    private boolean connected = false;
    private boolean nameEntered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("THEME","2");
//        editor.putString("CONNECTED", "0");
//        editor.putString("NAME", "");
//        editor.apply();
        if (sharedPreferences != null) {
            if (sharedPreferences.getString("CONNECTED", null) != null) {
                if (sharedPreferences.getString("CONNECTED", null).equals("1")) {
                    this.connected = true;
                }
            }
            if (sharedPreferences.getString("NAME", null) != null) {
                if (!sharedPreferences.getString("NAME", null).isEmpty()) {
                    nameEntered = true;
                }
            }
            if (sharedPreferences.getString("THEME", null) != null) {
                if (sharedPreferences.getString("THEME", null).equals("1")) {
                    setTheme(R.style.Cobra);
                    this.appTheme = 1;
                } else if (sharedPreferences.getString("THEME", null).equals("2")) {
                    setTheme(R.style.JohanEnDeEenhoorn);
                    this.appTheme = 2;
                }
            } else {
                setTheme(R.style.Cobra);
                this.appTheme = 1;
            }
        } else {
            System.out.println("null");
            setTheme(R.style.Cobra);
            this.appTheme = 1;
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
        System.out.println("connected: " + this.connected);
        System.out.println("connected to TOPIC: " + mqttClient.getTOPIC());
        if (this.connected) {
            mqttClient.setTOPIC("MobieleBelevingA5/score");
        } else {
            mqttClient.setTOPIC("MobieleBelevingA5/pair");
        }
        System.out.println("connected to TOPIC: " + mqttClient.getTOPIC());
        System.out.println(this.nameEntered);
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
        sortScores();
        recyclerView = findViewById(R.id.recycleViewForMainScreen);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mainAdapter = new MainAdapter(getApplicationContext(), ownScores);
        mainAdapter.setTheme(this.appTheme); // thema 1 = Cobra, thema 2 = Johan en de eenhoorn
        recyclerView.setAdapter(mainAdapter);

        recyclerView2 = findViewById(R.id.mainScreenInfo);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        System.out.println("name entered: " + this.nameEntered);
        if (!this.nameEntered) {
            mainAdapterName = new MainAdapterName(getApplicationContext(), this);
            mainAdapterName.setTheme(this.appTheme); // thema 1 = Cobra, thema 2 = Johan en de eenhoorn
            recyclerView2.setAdapter(mainAdapterName);
        } else {
            mainAdapter2 = new MainAdapter2(getApplicationContext(), this);
            mainAdapter2.setTheme(this.appTheme); // thema 1 = Cobra, thema 2 = Johan en de eenhoorn
            recyclerView2.setAdapter(mainAdapter2);
        }
    }


    @Override
    public void messageReceived(String topic, MqttMessage payload) {
        try {
            String message = payload.toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("TEST", message);
            editor.apply();
            this.messageRecieved = true;
            this.lastRecieved = sharedPreferences.getString("TEST", null);
            System.out.println(mqttClient.getTOPIC());
            if (mqttClient.getTOPIC().equals("MobieleBelevingA5/pair")) {
                this.pairingCode = message;
            } else if (mqttClient.getTOPIC().equals("MobieleBelevingA5/score")) {
                // todo: scores opslaan in het geheugen zodat die in het begin geladen kunnen worden
                ownScores.add(new Score(0, "Test", Integer.parseInt(message)));
                displayItems();
                System.out.println("score message: " + message);
                disconnect();

            }
        } catch (Exception e) {
            System.out.println("Message Received went wrong");
            e.printStackTrace();
        }
    }

    private void disconnect() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("CONNECTED", "0");
        editor.apply();
        mqttClient.unSubscribe("MobieleBelevingA5/connect");
        mqttClient.unSubscribe("MobieleBelevingA5/score");
        mqttClient.subscribe("MobieleBelevingA5/pair");

    }


    public void connectToTopic() {
        if (mainAdapter2 != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String number = String.valueOf(mainAdapter2.getMainViewHolder2().editText.getText());
            System.out.println(number);
            if (number.equals(this.pairingCode)) {
                mqttClient.setTOPIC("MobieleBelevingA5/connect");
                mqttClient.publishMessage("connect");
                editor.putString("CONNECTED", "1");
                editor.apply();
                getThemeFromTopic(this.pairingCode);
            }
        }
    }


    public void getThemeFromTopic(String pairingCodeTheme) {
        System.out.println("thema");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int theme = Integer.parseInt(pairingCodeTheme.substring(0, 1));
        System.out.println(theme);
        if (theme < 5) {
            editor.putString("THEME", "1");
        } else {
            editor.putString("THEME", "2");
        }
        editor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
//        mqttClient.unSubscribe("MobieleBelevingA5/pair");
//        mqttClient.unSubscribe("MobieleBelevingA5/connect");
//        mqttClient.unSubscribe("MobieleBelevingA5/score");
//        recreate();
    }

    public void enteredName(){
        if (mainAdapterName != null) {
            System.out.println("name clicked");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("NAME", String.valueOf(mainAdapterName.getMainViewHolderName().editTextName.getText()));
            editor.apply();
            this.nameEntered = true;
            displayItems();
        }
    }

    @Override
    public void onItemClicked() {
        connectToTopic();
    }

    @Override
    public void onItemClicked2() {
        enteredName();
    }
}