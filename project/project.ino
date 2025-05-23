const int addButton = 5;   // "ADD" button
const int clearButton = 6; // "CLEAR" button

bool addButtonState = HIGH;        
bool clearButtonState = HIGH;       
bool lastAddState = HIGH;  
bool lastClearState = HIGH;  

unsigned long lastAddDebounceTime = 0;  
unsigned long lastClearDebounceTime = 0;  
const unsigned long debounceDelay = 50;  // Debounce time in milliseconds

void setup() {
    Serial.begin(9600);
    pinMode(addButton, INPUT_PULLUP);
    pinMode(clearButton, INPUT_PULLUP);
}

void loop() {
    // Read the button states
    int readingAdd = digitalRead(addButton);
    int readingClear = digitalRead(clearButton);

    // ADD button debounce
    if (readingAdd != lastAddState) {
        lastAddDebounceTime = millis();
    }
    if ((millis() - lastAddDebounceTime) > debounceDelay) {
        if (readingAdd != addButtonState) {
            addButtonState = readingAdd;
            if (addButtonState == LOW) {
                Serial.println("ADD");
            }
        }
    }

    // CLEAR button debounce
    if (readingClear != lastClearState) {
        lastClearDebounceTime = millis();
    }
    if ((millis() - lastClearDebounceTime) > debounceDelay) {
        if (readingClear != clearButtonState) {
            clearButtonState = readingClear;
            if (clearButtonState == LOW) {
                Serial.println("CLEAR");
            }
        }
    }

    // Save the last button states
    lastAddState = readingAdd;
    lastClearState = readingClear;
}
