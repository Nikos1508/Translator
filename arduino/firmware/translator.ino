// c++
const byte piezoPin = 6;

// piezo frequency
const unsigned int feq[5] = {0, 261, 349, 440, 523};

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
  if (digitalRead(8) == LOW) {
    digitalWrite(2, HIGH);
    tone(piezoPin, feq[1]);
    delay(100);
    noTone(piezoPin);
    digitalWrite(2, LOW);
  }

  if (digitalRead(9) == LOW) {
    digitalWrite(3, HIGH);
    tone(piezoPin, feq[2]);
    delay(100);
    noTone(piezoPin);
    digitalWrite(3, LOW);
  }

  if (digitalRead(10) == LOW) {
    digitalWrite(4, HIGH);
    tone(piezoPin, feq[3]);
    delay(100);
    noTone(piezoPin);
    digitalWrite(4, LOW);
  }

  if (digitalRead(11) == LOW) {
    digitalWrite(5, HIGH);
    tone(piezoPin, feq[4]);
    delay(100);
    noTone(piezoPin);
    digitalWrite(5, LOW);
  }
}
