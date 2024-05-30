#include <Adafruit_NeoPixel.h>
#include <WiFi.h>
#include <PubSubClient.h>
#include <LiquidCrystal_I2C.h>


//analog reads
const int pinPressureSenor = 36;
const int pinSensitivity = 39;
//neopixels
const int numberOfPixels = 13;

const int heightPin = 16;//todo
const int strikePin = 17;//todo

//lcd
//Set the LCD address, number of columns and rows, and specify SCL and SDA pins
//SCL = D22
//SDA = D21
const int LINE_LENGTH = 16;
LiquidCrystal_I2C lcd(0x27, 16, 2);  // Change 0x27 to your LCD address if different

//wifi //todo
const char* WLAN_SSID = "iPhone van Jasper";
const char* WLAN_ACCESS_KEY = "Jahpertje";

//mqtt
const char* MQTT_CLIENT_ID = "ESP32";
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
  
  char txt[4];
  for (int i = 0; i < 4; i++) { txt[i] = '\0'; }
  strncpy(txt, (const char *) payload, length > 4 ? 4 : length);
  Serial.println(txt);
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
    while (!mqttClient.connect(MQTT_CLIENT_ID, MQTT_USERNAME, MQTT_PASSWORD)) {
      Serial.println("Failed to connect to MQTT broker");
      delay(1000);
    }
  } else {
    Serial.println("Connected to MQTT broker");
  }
  
  // mqttClient.publish("MQTT_TOPIC_TEST", "test");
  // mqttClient.subscribe("MQTT_TOPIC_TEST");
  if (!mqttClient.subscribe(MQTT_TOPIC_TEST, MQTT_QOS)) {
    Serial.print("Failed to subscribe to topic ");
    Serial.println(MQTT_TOPIC_TEST);
    while (!mqttClient.subscribe(MQTT_TOPIC_TEST, MQTT_QOS)) {
      Serial.print("Failed to subscribe to topic ");
      Serial.println(MQTT_TOPIC_TEST);
      delay(1000);
    }
  } else {
    Serial.print("Subscribed to topic ");
    Serial.println(MQTT_TOPIC_TEST);
  }

  mqttClient.publish(MQTT_TOPIC_TEST, "hallotjes");
  mqttClient.publish(MQTT_TOPIC_TEST, "test2");

  //leds
  // heightPixels.begin();
  // strikePixels.begin();

  // resetHeightPixels();
  turnOffAllPixels();
}

void loop() {
  // put your main code here, to run repeatedly:
  reconnect();

  mqttClient.loop();

  // if (!mqttClient.subscribe(MQTT_TOPIC_TEST, MQTT_QOS)) {
  //   Serial.print("Failed to subscribe to topic ");
  //   Serial.println(MQTT_TOPIC_TEST);
  // }

  

  // Serial.println(readMaxPressure());

  // int pres = readPressure();
  
  // if (pres > 200) {
  //   char charInt[5];
  //   String temp = String(pres);
  //   // char final = temp.toCharArray(), buf, 5);
  //   mqttClient.publish(MQTT_TOPIC_TEST, itoa(pres, charInt, 10));
  // }

  if (toPlay) {
    generateHeight();

    prepareSmash((height/100) -2);

    readMaxPressure();

    smash((strikeScore/100) -2);

    score = returnScore(strikeScore, height);
    printScore(score);

    char charInt[5];
    String temp = String(score);

    reconnect();

    mqttClient.publish(MQTT_TOPIC_TEST, itoa(score, charInt, 10));
    
    strikeScore = 0;
    height = 0;
    score = 0;
  }

  delay(100);
}
//todo bepalen welke waardes genegeerd moeten worden voor een threshold
void readMaxPressure() {
  boolean running = true;
  int maxPressure = 0;
  int millisBegin = 0;

  while (running) {
    reconnect();
    mqttClient.loop();
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
  lcd.clear();

  lcd.setCursor(0,0);
  lcd.print(text);
}

void printScore(int score) {
  lcd.clear();

  lcd.setCursor(0,0);
  lcd.print("Score: ");
  lcd.print(score);
  lcd.print("/");
  lcd.print(strikeScore);

  lcd.setCursor(0, 1);
  lcd.print("Height: ");
  lcd.print(height);
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

void resetHeightPixels() {
  for (int i = 0; i < numberOfPixels; i++) {
    heightPixels.setPixelColor(i, heightPixels.Color(255, 0, 0));
  }
  heightPixels.setBrightness(150);
  
  heightPixels.show();
}

void prepareSmash(int height){
  resetHeightPixels();

  heightPixels.setPixelColor(height, heightPixels.Color(0, 255, 0));
  heightPixels.setBrightness(150);
  heightPixels.show();
}

void smash(int led){
  if (led> numberOfPixels){
    return;
  }
  for (int i = 0; i < led; i++) {
    strikePixels.setPixelColor(i, strikePixels.Color(255, 0, 0));
    strikePixels.setBrightness(150);
    strikePixels.show();
    delay(30 * i);
  }
  delay(1000);
  for (int i = led; i >= 0; i--) {
    strikePixels.setPixelColor(i, strikePixels.Color(0, 0, 0));
    strikePixels.setBrightness(150);
    strikePixels.show();
    delay(20 * i);
  }
}

void turnOffAllPixels() {
  for (int i = 0; i < numberOfPixels; i++) {
    strikePixels.setPixelColor(i, strikePixels.Color(0, 0, 0));
    heightPixels.setPixelColor(i, heightPixels.Color(0, 0, 0));
  }
}

void reconnect() {
  if (!mqttClient.connected()) {
    mqttClient.connect(MQTT_CLIENT_ID, MQTT_USERNAME, MQTT_PASSWORD);  
    mqttClient.subscribe(MQTT_TOPIC_TEST, MQTT_QOS);
  }
}