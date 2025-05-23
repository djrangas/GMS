#include <SPI.h>
#include <MFRC522.h>
#include <LiquidCrystal.h>

#define SS_PIN 10
#define RST_PIN 9

const int addButton = A1;
const int clearButton = A2;
const int greenledPin = 18;
const int redledPin = 19;
const int buzzerPin = A0;

bool addButtonState = HIGH;
bool clearButtonState = HIGH;
bool lastAddState = HIGH;
bool lastClearState = HIGH;

unsigned long lastAddDebounceTime = 0;
unsigned long lastClearDebounceTime = 0;
const unsigned long debounceDelay = 50;

MFRC522 mfrc522(SS_PIN, RST_PIN);
LiquidCrystal lcd(7, 6, 5, 4, 3, 2);  // RS, EN, D4, D5, D6, D7

void setup() {
  Serial.begin(9600);
  SPI.begin();
  mfrc522.PCD_Init();

  pinMode(addButton, INPUT_PULLUP);
  pinMode(clearButton, INPUT_PULLUP);
  pinMode(greenledPin, OUTPUT);
  pinMode(redledPin, OUTPUT);
  pinMode(buzzerPin, OUTPUT);

  lcd.begin(16, 2);
  lcd.print("Place your card");
  Serial.println("System Initialized");
  Serial.println("Waiting for card...");
}

void loop() {
  // Button handling with debounce
  int readingAdd = digitalRead(addButton);
  int readingClear = digitalRead(clearButton);

  if (readingAdd != lastAddState) lastAddDebounceTime = millis();
  if ((millis() - lastAddDebounceTime) > debounceDelay) {
    if (readingAdd != addButtonState) {
      addButtonState = readingAdd;
      if (addButtonState == LOW) {
        Serial.println("ADD");
        lcd.clear();
        lcd.print("Add Pressed");
      }
    }
  }

  if (readingClear != lastClearState) lastClearDebounceTime = millis();
  if ((millis() - lastClearDebounceTime) > debounceDelay) {
    if (readingClear != clearButtonState) {
      clearButtonState = readingClear;
      if (clearButtonState == LOW) {
        Serial.println("CLEAR");
        lcd.clear();
        lcd.print("Clear Pressed");
      }
    }
  }

  // Serial commands from Java
  if (Serial.available() > 0) {
    char command = Serial.read();
    if (command == '1') {
      digitalWrite(greenledPin, HIGH);
      lcd.clear();
      lcd.print("Access Granted");

      tone(buzzerPin, 1000);  // Beep
      delay(500);
      noTone(buzzerPin);

      delay(2500);  // LED still on
      digitalWrite(greenledPin, LOW);
    } else if (command == '0') {
      for (int i = 0; i < 2; i++) {
        digitalWrite(redledPin, HIGH);
        tone(buzzerPin, 2000);
        delay(100);
        digitalWrite(redledPin, LOW);
        noTone(buzzerPin);
        delay(100);
      }
      lcd.clear();
      lcd.print("Access Denied");
    }
  }

  lastAddState = readingAdd;
  lastClearState = readingClear;

  // RFID card read
  if (!mfrc522.PICC_IsNewCardPresent() || !mfrc522.PICC_ReadCardSerial()) return;

  lcd.clear();
  lcd.print("UID: ");
  // Serial.print("UID tag: ");
  String uidString = "";

  for (byte i = 0; i < mfrc522.uid.size; i++) {
    if (mfrc522.uid.uidByte[i] < 0x10) uidString += "0";
    uidString += String(mfrc522.uid.uidByte[i], HEX);
  }

  uidString.toUpperCase();
  Serial.println(uidString);
  lcd.print(uidString);

  mfrc522.PICC_HaltA();
  mfrc522.PCD_StopCrypto1();
  delay(1500);
}
