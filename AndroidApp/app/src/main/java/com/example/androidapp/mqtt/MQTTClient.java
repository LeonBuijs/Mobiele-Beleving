package com.example.androidapp.mqtt;

import android.content.Context;
import android.util.Log;

import com.example.androidapp.main.MainActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
/*
* met behulp van Max Hager
* */

public class MQTTClient {
    private final String USERNAME = "MobieleBelevingA5";
    private final String PASSWORD = "liefsSybeA5";
    private String TOPIC = "MobieleBelevingA5/pair";
    private final String LOGTAG = MainActivity.class.getName();
    private final String LOG_TAG = "MQTT_A5";
    private final int QUALITY_OF_SERVICE = 2;//mag ook 0 ofzo zijn
    private static final String CLIENT_ID = "A5" + UUID.randomUUID().toString();
    private static final String BROKER_HOST_URL = "tcp://broker.hivemq.com:1883";
    private MqttAndroidClient mqttClient;

    public MQTTClient(Context context, MqttMR receiver){
        Log.i(LOG_TAG, "Using clientID '" + CLIENT_ID + "'.");
        mqttClient = new MqttAndroidClient(context,BROKER_HOST_URL,CLIENT_ID);
        setCallBackMQTT(receiver);
        connect();
    }
    public void setCallBackMQTT(MqttMR receiver){
        mqttClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                if (reconnect){
                    Log.i(LOG_TAG,"reconnected");
                } else Log.i(LOG_TAG, "Connection completed.");
            }

            @Override
            public void connectionLost(Throwable cause) {
                Log.w(LOG_TAG, "Connection lost.", cause);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.i(LOG_TAG, "Received message '" + message.toString() + "'.");
                receiver.messageReceived(topic,message);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.i(LOG_TAG, "Delivery completed.");
            }
        });
    }
    public void setReceiver(MqttMR receiver) {
        setCallBackMQTT(receiver);
    }
    public void connect(){
        if (mqttClient.isConnected()){
            Log.i(LOG_TAG, "Already connected.");
            return;
        }
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(false);
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());
        try {
            mqttClient.connect(options, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(LOG_TAG, "Successfully connected.");
                    subscribe(TOPIC);
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w(LOG_TAG, "Failed to connect.", exception);
                }
            });
        } catch (MqttException e){
            e.printStackTrace();
        }

    }

    public void subscribe(String topic){
        try {
            mqttClient.subscribe(topic, QUALITY_OF_SERVICE, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(LOG_TAG,"Subscribed to topic '" + topic + "'.");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w(LOG_TAG, "Failed to subscribe to topic '" + topic + "'.", exception);
                }
            });
        }catch (MqttException e){
            e.printStackTrace();
        }
    }
    public void unSubscribe(String topic){
        try {
            mqttClient.unsubscribe(topic);
            Log.i(LOG_TAG,"Unsubscribed to topic '" + topic + "'.");
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    public void publishMessage(String message){
        try {
            mqttClient.publish(TOPIC, message.getBytes(StandardCharsets.UTF_8), QUALITY_OF_SERVICE, false, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(LOG_TAG, "Published message '" + message + "'.");
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w(LOG_TAG, "Failed to publish message '" + message + "'.", exception);
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public String getTOPIC() {
        return TOPIC;
    }

    public void setTOPIC(String TOPIC) {
        this.TOPIC = TOPIC;
    }
}
