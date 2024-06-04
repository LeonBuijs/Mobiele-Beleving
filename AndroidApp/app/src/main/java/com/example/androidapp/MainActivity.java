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

import com.example.androidapp.Mqtt.MQTTClient;
import com.example.androidapp.Mqtt.MqttMR;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity implements MqttMR {

    private final String USERNAME = "MobieleBelevingA5";
    private final String PASSWORD = "liefsSybeA5";
    private final String TOPIC = "MobieleBelevingA5";
    private MQTTClient mqttClient;
    private  SharedPreferences sharedPreferences;

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
        mqttClient = new MQTTClient(getApplicationContext(),this);
       sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


    }

    public void TESTMQTTTEMP(View view) {
        mqttClient.publishMessage("THIS IS A TEST");

    }

    public void setMainScreen(View view){
    }
    public void setScoreScreen(View view){
        startActivity(new Intent(this, Scoreview.class));
    }
    public void setAchievementScreen(View view){
        startActivity(new Intent(this, Achievements.class));
    }


    @Override
    public void messageReceived(String topic, MqttMessage payload) {
        try {
            String message = payload.toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("TEST",message);
            editor.apply();
            Toast.makeText(this,sharedPreferences.getString("TEST",null),Toast.LENGTH_LONG).show();
        }catch (Exception e){
            System.out.println("Message Received went wrong");
            e.printStackTrace();
        }


    }
}