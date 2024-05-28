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

int sensitivityCorrection = 0;
void mqttCallback(char* topic, byte* payload, unsigned int length) {
  // Logging
  Serial.print("MQTT callback called for topic ");
  Serial.println(topic);
  Serial.print("Payload length ");
  Serial.println(length);
  
  // Kijk welk topic is ontvangen en handel daarnaar
  if (strcmp(topic, MQTT_TOPIC_TEST) == 0) {
    // De payload is een tekst voor op het LCD
    // Let op, geen null-terminated string, dus voeg zelf de \0 toe
    char txt[LINE_LENGTH + 1];
    for (int i = 0; i < LINE_LENGTH + 1; i++) { txt[i] = '\0'; }
    strncpy(txt, (const char *) payload, length > 16 ? 16 : length);
    // Laat de tekst zien in zowel log als op het LCD
    Serial.print("Text: ");
    Serial.println(txt);
    // lcd.clear();
    // lcd.setCursor(0, 0);
    // lcd.print(MQTT_TOPIC_LCD);
    // lcd.setCursor(0, 1);
    // lcd.print(txt);
  } 
  // else if (strcmp(topic, MQTT_TOPIC_BUTTON1) == 0) {
  //   Serial.println("Button 1 press received");
  //   for (int led = 0; led < NR_OF_LEDS; led++) {
  //     ledIntensities[led] = 0;
  //     setLedIntensity(led, ledIntensities[led]);
  //   }

  // else {
  //   Serial.println("Assuming LED topic received");
  //   char validPayload[16]; // Tijdelijke buffer van 16 chars voor de payload
  //   byte value; // Hierin komt de getalwaarde na conversie
  //   // Alleen de eerste 'length' bytes in de payload buffer zijn geldig
  //   // dus kopieer ze naar een tijdelijke char array en neem niet meer dan 16 chars mee
  //   strncpy(validPayload, (const char *) payload, length > 16 ? 16 : length);
  //   // Zet de tekst om in een byte waarde, veronderstel een unsigned int in de tekst
  //   sscanf((const char *) validPayload, "%u", &value);
  //   for (int led = 0; led < NR_OF_LEDS; led++) {
  //     if (strcmp(topic, MQTT_TOPIC_LEDS[led]) == 0) {
  //       ledIntensities[led] = value;
  //       setLedIntensity(led, ledIntensities[led]);
  //     }
  //   }
  // }
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

  mqttClient.loop();

  delay(1);
}

int readPressure() {
    return analogRead(pinPressureSenor);
}

void readSensitivity() {
    sensitivityCorrection = analogRead(pinSensitivity);
}

void printLCD(String text) {
  lcd.setCursor(0,0);
  lcd.print(text);
}

void printScore(int score) {
  lcd.setCursor(0,0);
  lcd.print("Score:");

  lcd.setCursor(0, 1);
  lcd.print(score);
}
