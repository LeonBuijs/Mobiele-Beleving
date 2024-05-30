package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private final String USERNAME = "MobieleBelevingA5";
    private final String PASSWORD = "liefsSybeA5";
    private final String TOPIC = "MobieleBelevingA5";
    private final String LOGTAG = MainActivity.class.getName();
    private final int QUALITY_OF_SERVICE = 0;
    private static final String CLIENT_ID = "MQTTExample_" + UUID.randomUUID().toString();
    private static final String BROKER_HOST_URL = "tcp://broker.hivemq.com:1883";
    private MqttAndroidClient mqttAndroidClient;
//    private MqttClient mqttClient;

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


    }
    public void TESTMQTTTEMP(View view) throws MqttException {
//        mqttClient = new MqttClient(BROKER_HOST_URL,CLIENT_ID);
//        mqttClient.setCallback(new MqttCallback() {
//            @Override
//            public void connectionLost(Throwable cause) {
//                Log.d(LOGTAG, "MQTT client lost connection to broker, cause: " + cause.getLocalizedMessage());
//            }
//
//            @Override
//            public void messageArrived(String topic, MqttMessage message) throws Exception {
//                Log.d(LOGTAG, "MQTT client received message " + message + " on topic " + topic);
//            }
//
//            @Override
//            public void deliveryComplete(IMqttDeliveryToken token) {
//                Log.d(LOGTAG, "MQTT client delivery complete");
//            }
//        });

        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(),BROKER_HOST_URL,CLIENT_ID);
        mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.d(LOGTAG, "MQTT client lost connection to broker, cause: " + cause.getLocalizedMessage());
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d(LOGTAG, "MQTT client received message " + message + " on topic " + topic);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.d(LOGTAG, "MQTT client delivery complete");
            }
        });
        connectToBroker(mqttAndroidClient,CLIENT_ID);
        subscribeToTopic(mqttAndroidClient,TOPIC);
        publishMessage(mqttAndroidClient,TOPIC,"testTestTest");

    }


    public void connectToBroker(MqttAndroidClient client,String clientID){
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(false);
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());
//        try {
//            client.connect(options);
//            Log.d(LOGTAG, "MQTT client is now connected to MQTT broker");
//        }catch (MqttException e){
//            Log.e(LOGTAG, "MQTT client failed to connect to MQTT broker: " +
//                    e.getLocalizedMessage());
//        }
        // Add more options if necessary
        try {
            // Try to connect to the MQTT broker

            IMqttToken token = client.connect(options);
            // Set up callbacks for the result
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(LOGTAG, "MQTT client is now connected to MQTT broker");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(LOGTAG, "MQTT client failed to connect to MQTT broker: " +
                            exception.getLocalizedMessage());
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Failed to connect to MQTT broker", Toast.LENGTH_LONG);
                    toast.show();
                }
            });
        } catch (MqttException e) {
            Log.e(LOGTAG, "MQTT exception while connecting to MQTT broker, reason: " +
                    e.getReasonCode() + ", msg: " + e.getMessage() + ", cause: " + e.getCause());
            e.printStackTrace();
        }
    }

private void publishMessage(MqttAndroidClient client, String topic, String msg) {
//        try {
//            // Convert the message to a UTF-8 encoded byte array
//            byte[] encodedPayload = msg.getBytes(StandardCharsets.UTF_8);
//            // Store it in an MqttMessage
//            MqttMessage message = new MqttMessage(encodedPayload);
//            // Set parameters for the message
//            message.setQos(QUALITY_OF_SERVICE);
//            message.setRetained(false);
//            // Publish the message via the MQTT broker
//            client.publish(topic, message);
//        } catch (MqttException e) {
//            Log.e(LOGTAG, "MQTT exception while publishing topic to MQTT broker, msg: " + e.getMessage() +
//                    ", cause: " + e.getCause());
//            e.printStackTrace();
//        }
        byte[] encodedPayload = new byte[0];
        try {
            // Convert the message to a UTF-8 encoded byte array
            encodedPayload = msg.getBytes("UTF-8");
            // Store it in an MqttMessage
            MqttMessage message = new MqttMessage(encodedPayload);
            // Set parameters for the message
            message.setQos(QUALITY_OF_SERVICE);
            message.setRetained(false);
            // Publish the message via the MQTT broker
            client.publish(topic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            Log.e(LOGTAG, "MQTT exception while publishing topic to MQTT broker, msg: " + e.getMessage() +
                    ", cause: " + e.getCause());
            e.printStackTrace();
        }
    }

    private void subscribeToTopic(MqttAndroidClient client, final String topic) {
//        try {
//            client.subscribe(topic,QUALITY_OF_SERVICE);
//            Log.d(LOGTAG, "MQTT client is now subscribed to topic " + topic);
//        }catch (MqttException e){
//            Log.e(LOGTAG, "MQTT failed to subscribe to topic " + topic + " because: " +
//                    e.getLocalizedMessage());
//            e.printStackTrace();
//        }

        try {
            // Try to subscribe to the topic
            IMqttToken token = client.subscribe(topic, QUALITY_OF_SERVICE);
            // Set up callbacks to handle the result
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(LOGTAG, "MQTT client is now subscribed to topic " + topic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(LOGTAG, "MQTT failed to subscribe to topic " + topic + " because: " +
                            exception.getLocalizedMessage());
                }
            });
        } catch (MqttException e) {
            Log.e(LOGTAG, "MQTT exception while subscribing to topic on MQTT broker, reason: " +
                    e.getReasonCode() + ", msg: " + e.getMessage() + ", cause: " + e.getCause());
            e.printStackTrace();
        }
    }

//    private void unsubscribeToTopic(MqttAndroidClient client, final String topic) {
//        try {
//            // Try to unsubscribe to the topic
//            IMqttToken token = client.unsubscribe(topic);
//            // Set up callbacks to handle the result
//            token.setActionCallback(new IMqttActionListener() {
//                @Override
//                public void onSuccess(IMqttToken asyncActionToken) {
//                    Log.d(LOGTAG, "MQTT client is now unsubscribed to topic " + topic);
//                }
//
//                @Override
//                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                    Log.e(LOGTAG, "MQTT client failed to unsubscribe to topic " + topic + " because: " +
//                            exception.getLocalizedMessage());
//                }
//            });
//        } catch (MqttException e) {
//            Log.e(LOGTAG, "MQTT exception while unsubscribing from topic on MQTT broker, reason: " +
//                    e.getReasonCode() + ", msg: " + e.getMessage() + ", cause: " + e.getCause());
//            e.printStackTrace();
//        }
//    }

    public void setMainScreen(View view){
    }
    public void setScoreScreen(View view){
        startActivity(new Intent(this, Scoreview.class));
    }
    public void setAchievementScreen(View view){
        startActivity(new Intent(this, Achievements.class));
    }


}