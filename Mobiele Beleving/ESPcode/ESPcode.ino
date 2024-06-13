#include <Adafruit_NeoPixel.h>
#include <WiFi.h>
#include <PubSubClient.h>
#include <LiquidCrystal_I2C.h>


//analog reads
const int pinPressureSenor = 36;
const int pinSensitivity = 39;
//neopixels
const int numberOfPixels = 12;

const int heightPin = 16;
const int strikePin = 17;

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
const char* MQTT_CLIENT_ID = "ESPTJE";
//todo
const char* MQTT_BROKER_URL = "broker.hivemq.com";
const int   MQTT_PORT = 1883;
const char* MQTT_USERNAME = "MobieleBelevingA5";
const char* MQTT_PASSWORD = "liefsSybeA5";

const char* MQTT_TOPIC_TEST = "MobieleBelevingA5";
const char* MQTT_TOPIC_PAIR = "MobieleBelevingA5/pair";
const char* MQTT_TOPIC_CONNECT = "MobieleBelevingA5/connect";
const char* MQTT_TOPIC_SCORE = "MobieleBelevingA5/score";
const char* MQTT_TOPIC_THEME = "MobieleBelevingA5/thema";

//checker voor connection met android
const char* connectionCheck = "connect";

const int MQTT_QOS = 1;

String mqttScore = "0";

WiFiClient wifiClient;

PubSubClient mqttClient(wifiClient);

//leds
Adafruit_NeoPixel heightPixels(numberOfPixels, heightPin, NEO_GRB + NEO_KHZ800);
Adafruit_NeoPixel strikePixels(numberOfPixels, strikePin, NEO_GRB + NEO_KHZ800);

const int johanDraak[2][3] = {{255, 123, 184}, {255, 245, 0}};
const int cobra[2][3] = {{0, 255, 0}, {255, 0, 0}};

int currentThema = 1;

const char* thema1 = "thema1";
const char* thema2 = "thema2";

//variabelen
int strikeScore = 0;
int height = 0;
int score = 0;

boolean toPlay = false;

int sensitivityCorrection = 0;

void mqttCallback(char* topic, byte* payload, unsigned int length) {
  // Logging
  Serial.print("MQTT callback called for topic ");
  Serial.println(topic);
  Serial.print("Payload length ");
  Serial.println(length);
  
  char txt[16];
  for (int i = 0; i < 16; i++) { txt[i] = '\0'; }
  strncpy(txt, (const char *) payload, length > 16 ? 16 : length);
  Serial.println(txt);
  mqttScore = txt;

  if (strcmp(topic, MQTT_TOPIC_CONNECT) == 0) {
    if (strcmp(txt, connectionCheck) == 0) {
      toPlay = true;
    }
  } else if (strcmp(topic, MQTT_TOPIC_THEME) == 0) {
    if (strcmp(txt, thema1) == 0) {
      currentThema = 1;
    } else {
      currentThema = 2;
    }
  }
}


void setup() {
  Serial.begin(9600);
  //lcd
  lcd.init();
  lcd.backlight();
  //WiFi
  WiFi.mode(WIFI_STA);
  WiFi.disconnect();
  WiFi.begin(WLAN_SSID, WLAN_ACCESS_KEY);

  printLCD("Connecting...");

  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
  }
  printLCD("Connected!");
  delay(1000);
  lcd.clear();
  //mqtt
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
  
  subscribeToTopic(MQTT_TOPIC_TEST);
  subscribeToTopic(MQTT_TOPIC_PAIR);
  subscribeToTopic(MQTT_TOPIC_CONNECT);
  subscribeToTopic(MQTT_TOPIC_SCORE);
  subscribeToTopic(MQTT_TOPIC_THEME);

  mqttClient.publish(MQTT_TOPIC_THEME, "thema1");
}

void subscribeToTopic(const char* topic) {
  if (!mqttClient.subscribe(topic, MQTT_QOS)) {
    Serial.print("Failed to subscribe to topic ");
    Serial.println(topic);
    while (!mqttClient.subscribe(topic, MQTT_QOS)) {
      Serial.print("Failed to subscribe to topic ");
      Serial.println(topic);
      printLCD("Error, Restart");
      delay(1000);
    }
  } else {
    Serial.print("Subscribed to topic ");
    Serial.println(topic);
  } 
}

void loop() {
  reconnect();

  mqttClient.loop();

  turnOffAllPixels();

  char charPairing[5];
  int pairCode = createPairingCode();
  printPairingCode(pairCode);
  
  char* charCode = itoa(pairCode, charPairing, 10);

  while (!toPlay) {
    mqttClient.loop();
    reconnect();
    mqttClient.publish(MQTT_TOPIC_PAIR, charCode);

    for (int i = 0; i < 10; i++) {
      mqttClient.loop();
      reconnect();
      delay(100);
      if (toPlay) {
        break;
      }
    }
  }

  if (toPlay) {
    generateHeight();

    printLCD("Doe Je Best!");

    prepareSmash((height/100) -3);

    readMaxPressure();

    smash((strikeScore/100) -3);

    score = returnScore(strikeScore, height);
    printScore(score);

    char charInt[5];
    String temp = String(score);

    reconnect();

    mqttClient.publish(MQTT_TOPIC_SCORE, itoa(score, charInt, 10));

    mqttClient.loop();
  }

  toPlay = false;

  delay(5000);
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
  Serial.println(text);

  lcd.setCursor(0,0);
  lcd.print(text);
}

void printScore(int score) {
  lcd.clear();

  lcd.setCursor(0,0);
  lcd.print("Score: ");
  lcd.print(score);

  lcd.setCursor(0, 1);
  lcd.print(String(strikeScore) + "/" + String(height));
}

void printPairingCode(int code) {
  Serial.println(code + "");
  lcd.clear();

  lcd.setCursor(0,0);
  lcd.print("Koppelcode: ");
  lcd.print(String(code));

  lcd.setCursor(0,1);
  lcd.print("Laatste: ");
  lcd.print("   " + String(score));
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
  if (currentThema == 1) {
    for (int i = 0; i < numberOfPixels; i++) {
      heightPixels.setPixelColor(i, heightPixels.Color(cobra[0][0], cobra[0][1], cobra[0][2]));
    }
  } else if (currentThema == 2) {
    for (int i = 0; i < numberOfPixels; i++) {
      heightPixels.setPixelColor(i, heightPixels.Color(johanDraak[0][0], johanDraak[0][1], johanDraak[0][2]));
    }
  } else {
    for (int i = 0; i < numberOfPixels; i++) {
      heightPixels.setPixelColor(i, heightPixels.Color(cobra[0][0], cobra[0][1], cobra[0][2]));
    }
  }
  heightPixels.setBrightness(100);
  
  heightPixels.show();
}

void prepareSmash(int height){
  resetHeightPixels();

  if (currentThema == 1) {
    heightPixels.setPixelColor(height, heightPixels.Color(cobra[1][0], cobra[1][1], cobra[1][2]));
  } else if (currentThema == 2) {
    heightPixels.setPixelColor(height, heightPixels.Color(johanDraak[1][0], johanDraak[1][1], johanDraak[1][2]));
  } else {
    heightPixels.setPixelColor(height, heightPixels.Color(cobra[1][0], cobra[1][1], cobra[1][2]));
  }

  // heightPixels.setPixelColor(height, heightPixels.Color(0, 255, 0));
  heightPixels.setBrightness(100);
  heightPixels.show();
}

void smash(int led){
  if (led> numberOfPixels){
    led = numberOfPixels;
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
  while (!mqttClient.connected()) {
    mqttClient.connect(MQTT_CLIENT_ID, MQTT_USERNAME, MQTT_PASSWORD); 
    mqttClient.subscribe(MQTT_TOPIC_TEST, MQTT_QOS);
    mqttClient.subscribe(MQTT_TOPIC_PAIR, MQTT_QOS);
    Serial.println("reconnecting");
    delay(1);
  }
  mqttClient.loop();
}

int createPairingCode() {
  return random(1000, 9999);
}