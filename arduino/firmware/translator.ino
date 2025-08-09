const byte piezoPin = 6;

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
  bool green = digitalRead(8);
  bool yellow = digitalRead(9);
  bool red = digitalRead(10);
  bool blue = digitalRead(11);

  if (green == LOW) {
    digitalWrite(2, HIGH);
    if (lastGreen == HIGH) {
      greenPressStart = millis();
      greenPressed = true;
      greenTune();
      delay(50);
    }

    unsigned long pressDuration = millis() - greenPressStart;

  } else {
    digitalWrite(2, LOW);
    greenPressed = false;
  }

  if (yellow == LOW) {
    digitalWrite(3, HIGH);
    if (lastYellow == HIGH) {
      yellowPressStart = millis();
      yellowPressed = true;
      yellowTune();
      delay(50);
    }

    unsigned long pressDuration = millis() - yellowPressStart;

  } else {
    digitalWrite(3, LOW);
    yellowPressed = false;
  }

  if (red == LOW) {
    digitalWrite(4, HIGH);
    if (lastRed == HIGH) {
      redPressStart = millis();
      redPressed = true;
      redTune();
      delay(50);
    }

    unsigned long pressDuration = millis() - redPressStart;

  } else {
    digitalWrite(4, LOW);
    redPressed = false;
  }

  if (blue == LOW) {
    digitalWrite(5, HIGH);
    if (lastBlue == HIGH) {
      bluePressStart = millis();
      bluePressed = true;
      blueTune();
      delay(50);
    }

    unsigned long pressDuration = millis() - bluePressStart;

  } else {
    digitalWrite(5, LOW);
    bluePressed = false;
  }

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
