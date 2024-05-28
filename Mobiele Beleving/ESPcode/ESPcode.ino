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
LiquidCrystal_I2C lcd(0x27, 16, 2);  // Change 0x27 to your LCD address if different

//wifi
const char* WLAN_SSID = "SSID_van_je_eigen_WLAN";
const char* WLAN_ACCESS_KEY = "password_van_je_eigen_WLAN";

//mqtt
const char* MQTT_CLIENT_ID = "MQTTExampleTryout_maak_hier_een_unieke_code_van";
//todo
const char* MQTT_BROKER_URL = "sendlab.nl";
const int   MQTT_PORT = 11884;
const char* MQTT_USERNAME = "ti";
const char* MQTT_PASSWORD = "tiavans";

const int MQTT_QOS = 0;

WiFiClient wifiClient;

PubSubClient mqttClient(wifiClient);

//leds
Adafruit_NeoPixel heightPixels(numberOfPixels, heightPin, NEO_GRB + NEO_KHZ800);
Adafruit_NeoPixel strikePixels(numberOfPixels, strikePin, NEO_GRB + NEO_KHZ800);

int sensitivityCorrection = 0;

void setup() {
  Serial.begin(9600);
  //lcd
  lcd.init();
  lcd.backlight();
  //WiFi
    WiFi.mode(WIFI_STA);
    WiFi.begin(WLAN_SSID, WLAN_ACCESS_KEY);
}

void loop() {
  // put your main code here, to run repeatedly:
  // readSensitivity();

  // Serial.println(sensitivityCorrection);
  printScore(69);

  delay(100);
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
