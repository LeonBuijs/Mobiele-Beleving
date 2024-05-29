#include <Adafruit_NeoPixel.h>
#include <WiFi.h>
#include <PubSubClient.h>
#include <LiquidCrystal_I2C.h>


//analog reads
const int pinPressureSenor = 36;
const int pinSensitivity = 39;
//neopixels
const int numberOfPixels = 38;

const int heightPin = -1;//todo
const int strikePin = -2;//todo

//lcd
// Set the LCD address, number of columns and rows, and specify SCL and SDA pins
//SCL = D22
//SDA = D21
const int LINE_LENGTH = 16;
LiquidCrystal_I2C lcd(0x27, 16, 2);  // Change 0x27 to your LCD address if different

//wifi //todo
const char* WLAN_SSID = "iPhone van Jasper";
const char* WLAN_ACCESS_KEY = "Jahpertje";

//mqtt
const char* MQTT_CLIENT_ID = "broker.hivemq.com";
//todo
const char* MQTT_BROKER_URL = "broker.hivemq.com";
const int   MQTT_PORT = 1883;
const char* MQTT_USERNAME = "MobieleBelevingA5";
const char* MQTT_PASSWORD = "liefsSybeA5";

const char* MQTT_TOPIC_TEST = "MobieleBelevingA5";

const int MQTT_QOS = 0;

WiFiClient wifiClient;

PubSubClient mqttClient(wifiClient);

//leds
Adafruit_NeoPixel heightPixels(numberOfPixels, heightPin, NEO_GRB + NEO_KHZ800);
Adafruit_NeoPixel strikePixels(numberOfPixels, strikePin, NEO_GRB + NEO_KHZ800);

void mqttCallback(char* topic, byte* payload, unsigned int length) {
  // Logging
  Serial.print("MQTT callback called for topic ");
  Serial.println(topic);
  Serial.print("Payload length ");
  Serial.println(length);
}

//variabelen
int strikeScore = 0;
int height = 0;
int score = 0;
boolean toPlay = true;

int sensitivityCorrection = 0;


void setup() {
  Serial.begin(9600);
  //lcd
  lcd.init();
  lcd.backlight();
  //WiFi
  WiFi.mode(WIFI_STA);
  WiFi.disconnect();
  WiFi.begin(WLAN_SSID, WLAN_ACCESS_KEY);

  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(1000);
  }
  //mqtt
  // mqttClient.connect(MQTT_CLIENT_ID, MQTT_USERNAME, MQTT_PASSWORD);
  mqttClient.setServer(MQTT_BROKER_URL, MQTT_PORT);
  mqttClient.setCallback(mqttCallback);

  if (!mqttClient.connect(MQTT_CLIENT_ID, MQTT_USERNAME, MQTT_PASSWORD)) {
    Serial.println("Failed to connect to MQTT broker");
  } else {
    Serial.println("Connected to MQTT broker");
  }
  
  mqttClient.publish(MQTT_TOPIC_TEST, "hallotjes");

  // mqttClient.publish("MQTT_TOPIC_TEST", "test");
  // mqttClient.subscribe("MQTT_TOPIC_TEST");
  if (!mqttClient.subscribe(MQTT_TOPIC_TEST, MQTT_QOS)) {
    Serial.print("Failed to subscribe to topic ");
    Serial.println(MQTT_TOPIC_TEST);
  } else {
    Serial.print("Subscribed to topic ");
    Serial.println(MQTT_TOPIC_TEST);
  }
}

void loop() {
  // put your main code here, to run repeatedly:
  // readSensitivity();

  // Serial.println(sensitivityCorrection);
  // printScore(69);

  // mqttClient.loop();
  // Serial.println(readMaxPressure());
  if (toPlay) {
    generateHeight();
    readMaxPressure();
    score = returnScore(strikeScore, height);
    printScore(score);
    Serial.println(score);
    strikeScore = 0;
    height = 0;
    score = 0;
  }

  delay(100);
}
//todo bepalen welke waardes genegeerd moeten worden voor een threshold
void readMaxPressure() {
  boolean running = true;
  while (running) {
    readSensitivity();
    int pressure = readPressure();
    if  (pressure > 300) {
      strikeScore = pressure;
      running = false;
      break;
    }
  }
}

int readPressure() {
  return analogRead(pinPressureSenor) - sensitivityCorrection;
}

void readSensitivity() {
  int sensitivity = analogRead(pinSensitivity);
  sensitivityCorrection = ((sensitivity/100) * 2);
}

void printLCD(String text) {
  lcd.setCursor(0,0);
  lcd.print(text);
}

void printScore(int score) {
  lcd.clear();

  lcd.setCursor(0,0);
  lcd.print("Score:");

  lcd.setCursor(0, 1);
  lcd.print(score);
}

int returnScore(int score, int height) {
  int difference = height - score;

  if (difference > 1000 || difference < -1000) {
    return 0;
  }

  if (difference < 0) {
    return 1000 + difference;
  } else {
    return 1000 - difference;
  }
}

void generateHeight() {
  height = random(500, 1500);
}