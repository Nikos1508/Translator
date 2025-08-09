const byte piezoPin = 6;

// store previous button states
bool lastGreen = HIGH;
bool lastYellow = HIGH;
bool lastRed = HIGH;
bool lastBlue = HIGH;

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

  startupSound();  // Play startup sound once at boot
}

void loop() {
  bool green = digitalRead(8);
  bool yellow = digitalRead(9);
  bool red = digitalRead(10);
  bool blue = digitalRead(11);

  if (green == LOW) {
    digitalWrite(2, HIGH);
    if (lastGreen == HIGH) {
      greenTune();
      delay(50);  // small debounce delay
    }
  } else {
    digitalWrite(2, LOW);
  }

  if (yellow == LOW) {
    digitalWrite(3, HIGH);
    if (lastYellow == HIGH) {
      yellowTune();
      delay(50);
    }
  } else {
    digitalWrite(3, LOW);
  }

  if (red == LOW) {
    digitalWrite(4, HIGH);
    if (lastRed == HIGH) {
      redTune();
      delay(50);
    }
  } else {
    digitalWrite(4, LOW);
  }

  if (blue == LOW) {
    digitalWrite(5, HIGH);
    if (lastBlue == HIGH) {
      blueTune();
      delay(50);
    }
  } else {
    digitalWrite(5, LOW);
  }

  lastGreen = green;
  lastYellow = yellow;
  lastRed = red;
  lastBlue = blue;
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
