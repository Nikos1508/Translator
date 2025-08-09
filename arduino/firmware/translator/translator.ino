const byte piezoPin = 6;

// System startup flag
bool systemStarted = false;

// store previous button states
bool lastGreen = HIGH;
bool lastYellow = HIGH;
bool lastRed = HIGH;
bool lastBlue = HIGH;

unsigned long greenPressStart = 0;
bool greenPressed = false;

unsigned long yellowPressStart = 0;
bool yellowPressed = false;

unsigned long redPressStart = 0;
bool redPressed = false;

unsigned long bluePressStart = 0;
bool bluePressed = false;

// Function to round press duration to 1, 2, 3, 4, or 5 seconds
int roundPressTime(unsigned long duration) {
  float seconds = duration / 1000.0;
  
  if (seconds < 1.5) {
    return 1;
  } else if (seconds < 2.5) {
    return 2;
  } else if (seconds < 3.5) {
    return 3;
  } else if (seconds < 4.5) {
    return 4;
  } else {
    return 5;
  }
}

void setup() {
  // leds
  pinMode(2, OUTPUT); // green
  pinMode(3, OUTPUT); // yellow
  pinMode(4, OUTPUT); // red
  pinMode(5, OUTPUT); // blue

  // push-buttons
  pinMode(8, INPUT_PULLUP); // green
  pinMode(9, INPUT_PULLUP); // yellow
  pinMode(10, INPUT_PULLUP); // red
  pinMode(11, INPUT_PULLUP); // blue

  // buzzer
  pinMode(piezoPin, OUTPUT);

  Serial.begin(9600);
}

void loop() {
  // Read buttons
  bool green = digitalRead(8);
  bool yellow = digitalRead(9);
  bool red = digitalRead(10);
  bool blue = digitalRead(11);
  
  // Check if system needs to be started
  if (!systemStarted) {
    if (green == LOW || yellow == LOW || red == LOW || blue == LOW) {
      startupSound();
      systemStarted = true;
      delay(500);
      return;
    }
    return;
  }

  // Green button
  static unsigned long greenPressDuration = 0;
  if (green == LOW) {
    digitalWrite(2, HIGH);
    if (lastGreen == HIGH) {
      greenPressStart = millis();
      greenPressed = true;
      greenTune();
      delay(50);
    }
    greenPressDuration = millis() - greenPressStart;
  } else {
    digitalWrite(2, LOW);
    if (greenPressed && lastGreen == LOW) {
      int roundedTime = roundPressTime(greenPressDuration);
      Serial.print("Green button: ");
      Serial.print(roundedTime);
      Serial.println(" second(s)");
      
      if (roundedTime == 5) {
        oppositeTune();
      }
    }
    greenPressed = false;
    greenPressDuration = 0;
  }

  // Yellow button
  static unsigned long yellowPressDuration = 0;
  if (yellow == LOW) {
    digitalWrite(3, HIGH);
    if (lastYellow == HIGH) {
      yellowPressStart = millis();
      yellowPressed = true;
      yellowTune();
      delay(50);
    }
    yellowPressDuration = millis() - yellowPressStart;
  } else {
    digitalWrite(3, LOW);
    if (yellowPressed && lastYellow == LOW) {
      int roundedTime = roundPressTime(yellowPressDuration);
      Serial.print("Yellow button: ");
      Serial.print(roundedTime);
      Serial.println(" second(s)");
      
      if (roundedTime == 5) {
        oppositeTune();
      }
    }
    yellowPressed = false;
    yellowPressDuration = 0;
  }

  // Red button
  static unsigned long redPressDuration = 0;
  if (red == LOW) {
    digitalWrite(4, HIGH);
    if (lastRed == HIGH) {
      redPressStart = millis();
      redPressed = true;
      redTune();
      delay(50);
    }
    redPressDuration = millis() - redPressStart;
  } else {
    digitalWrite(4, LOW);
    if (redPressed && lastRed == LOW) {
      int roundedTime = roundPressTime(redPressDuration);
      Serial.print("Red button: ");
      Serial.print(roundedTime);
      Serial.println(" second(s)");
      
      if (roundedTime == 5) {
        oppositeTune();
      }
    }
    redPressed = false;
    redPressDuration = 0;
  }

  // Blue button
  static unsigned long bluePressDuration = 0;
  if (blue == LOW) {
    digitalWrite(5, HIGH);
    if (lastBlue == HIGH) {
      bluePressStart = millis();
      bluePressed = true;
      blueTune();
      delay(50);
    }
    bluePressDuration = millis() - bluePressStart;
  } else {
    digitalWrite(5, LOW);
    if (bluePressed && lastBlue == LOW) {
      int roundedTime = roundPressTime(bluePressDuration);
      Serial.print("Blue button: ");
      Serial.print(roundedTime);
      Serial.println(" second(s)");
      
      if (roundedTime == 5) {
        oppositeTune();
      }
    }
    bluePressed = false;
    bluePressDuration = 0;
  }

  // Update last button states
  lastGreen = green;
  lastYellow = yellow;
  lastRed = red;
  lastBlue = blue;

  delay(30);
}

// two-tone tunes for each color

void greenTune() {
  tone(piezoPin, 523, 150);
  delay(150);
  tone(piezoPin, 659, 150);
  delay(150);
  noTone(piezoPin);
}

void yellowTune() {
  tone(piezoPin, 392, 150);
  delay(150);
  tone(piezoPin, 440, 150);
  delay(150);
  noTone(piezoPin);
}

void redTune() {
  tone(piezoPin, 330, 150);
  delay(150);
  tone(piezoPin, 294, 150);
  delay(150);
  noTone(piezoPin);
}

void blueTune() {
  tone(piezoPin, 262, 150);
  delay(150);
  tone(piezoPin, 311, 150);
  delay(150);
  noTone(piezoPin);
}

// Startup sound
void startupSound() {
  tone(piezoPin, 400, 150);
  delay(200);
  tone(piezoPin, 600, 150);
  delay(200);
  tone(piezoPin, 800, 300);
  delay(300);
  noTone(piezoPin);
}

void oppositeTune() {
  tone(piezoPin, 523, 300);  // C5
  delay(300);
  tone(piezoPin, 784, 300);  // G5
  delay(300);
  tone(piezoPin, 1046, 600); // C6 (octave up)
  delay(600);
  noTone(piezoPin);
}


