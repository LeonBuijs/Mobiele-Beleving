package com.example.androidapp.Mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public interface MqttMR {
    /*
    * met behulp van Max Hager
    * */
    void messageReceived(String topic, MqttMessage payload);
}
