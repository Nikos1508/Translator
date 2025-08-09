// c++
const byte piezoPin = 6;

// piezo frequency
const unsigned int feq[5] = {0, 262, 330, 392, 523};

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
  pinMode(6, OUTPUT);

  Serial.begin(9600);
}

int currentTone = 0;

void loop() {
  int newTone = 0;

  if (digitalRead(8) == LOW) {
    digitalWrite(2, HIGH); // green led on
    newTone = 1;
  } else {
    digitalWrite(2, LOW);  // green led off
  }

  if (digitalRead(9) == LOW) {
    digitalWrite(3, HIGH); // yellow led on
    if (newTone == 0) newTone = 2;
  } else {
    digitalWrite(3, LOW);  // yellow led off
  }

  if (digitalRead(10) == LOW) {
    digitalWrite(4, HIGH); // red led on
    if (newTone == 0) newTone = 3;
  } else {
    digitalWrite(4, LOW);  // red led off
  }

  if (digitalRead(11) == LOW) {
    digitalWrite(5, HIGH); // blue led on
    if (newTone == 0) newTone = 4;
  } else {
    digitalWrite(5, LOW);  // blue led off
  }

  if (newTone != currentTone) {
    if (newTone == 0) {
      noTone(piezoPin);
    } else {
      tone(piezoPin, feq[newTone]);
    }
    currentTone = newTone;
  }
}
