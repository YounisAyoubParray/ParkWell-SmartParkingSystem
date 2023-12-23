#include <LiquidCrystal_I2C.h>
#include <ESP8266WiFi.h>
#include <FirebaseESP8266.h>
#define FIREBASE_HOST "*********************************************************"
#define FIREBASE_AUTH "**********************************************"

#define LCD_ADDRESS 0x27
#define LCD_COLUMNS 16
#define LCD_ROWS 2


const char *ssid = "projectsp";      
const char *password = "helloworld@25"; 
const int triggerPin = D0;
const int echoPin = D3;   
long duration;
int distance;                                
int slot2 = D4;
int slot3 = D6;
int slot4 = D7;


boolean s2,s3,s4;
boolean aa = false;
boolean bb = false;
boolean cc = false;
boolean dd = false;
boolean dt=false;
boolean rt=false;
LiquidCrystal_I2C lcd(LCD_ADDRESS, LCD_COLUMNS, LCD_ROWS);
FirebaseData firebaseData;

void setup() {
  pinMode(triggerPin, OUTPUT);
  pinMode(echoPin, INPUT);
  pinMode(slot2, INPUT);
  pinMode(slot3, INPUT);
  pinMode(slot4, INPUT);
  
  Wire.begin();
  


  Serial.begin(9600);
  delay(100);
  lcd.begin(LCD_COLUMNS, LCD_ROWS);  
  lcd.backlight();  
  lcd.setCursor(0, 0);
  lcd.print("Connecting to ");
  lcd.setCursor(0, 1);
  lcd.print(ssid);
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println();
  Serial.println("Wi-Fi connected");
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("Welcome, This is");
  lcd.setCursor(0, 1);
  lcd.print("Slot No. 1");
  
  
}
void loop(){

s2 = digitalRead(slot2);
s3 = digitalRead(slot3);
s4 = digitalRead(slot4);

  digitalWrite(triggerPin, LOW);
  delayMicroseconds(2);
  digitalWrite(triggerPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(triggerPin, LOW);
  duration = pulseIn(echoPin, HIGH);
  distance = duration * 0.034 / 2;
  
  if (distance > 11 && aa == false) {
    if(!Firebase.setBool(firebaseData,"/parkings/Srinagar/slot1",true)){
    Serial.println("printing to dashboard abt slot 1 availability failed");}
    else{
      Serial.println("printing to dashboard abt slot 1 availability success");
      aa=true;
    };
    lcd.clear();
    lcd.setCursor(0, 0);
    lcd.print("Welcome, This is");
    lcd.setCursor(0, 1);
    lcd.print("Slot No. 1");
  }

  if (distance >= 7 && dt == false && rt==true) {
    lcd.clear();
    lcd.setCursor(0, 0);
    lcd.print("Thank you");
    lcd.setCursor(0,1);
    lcd.print("Safe travels!");
    rt=false;
  }

  if(distance<10 && dt==true){
    lcd.setCursor(0, 0);
    lcd.print("Distance: ");
    lcd.print(distance);
    lcd.print(" cm");
    lcd.setCursor(0, 1);
    lcd.print("R.Distance: 3cm");
  }
  
  if(distance<=3 && dt==true){
    lcd.setCursor(0, 0);
    lcd.print("Distance: ");
    lcd.print(distance);
    lcd.print(" cm");
    lcd.setCursor(0, 1);
    lcd.print("Handbrake,please");
    dt=false;
    rt=true;
  }
  


if (s2 == 1 && bb == false){
  if(!Firebase.setBool(firebaseData,"/parkings/Srinagar/slot2",true)){
    Serial.println("printing to dashboard abt slot 2 availability failed");}
    else{Serial.println("printing to dashboard abt slot 2 availability success");
    bb = true;
    };
}

if (s3 == 1 && cc == false){
  if(!Firebase.setBool(firebaseData,"/parkings/Srinagar/slot3",true)){
    Serial.println("printing to dashboard abt slot 3 availability failed");}
    else{Serial.println("printing to dashboard abt slot 3 availability success");
    cc = true;
    };
}

if (s4 == 1 && dd == false){
  if(!Firebase.setBool(firebaseData,"/parkings/SrinagarR/R1",true)){
    Serial.println("printing to dashboard abt slot 4 availability failed");}
    else{Serial.println("printing to dashboard abt slot 4 availability success");
    dd = true;
    };
}


if (distance < 10 && aa == true) {
    if(!Firebase.setBool(firebaseData,"/parkings/Srinagar/slot1",false)){
    Serial.println("printing to dashboard abt slot 1 non-availability failed");}
    else{
      Serial.println("printing to dashboard abt slot 1 non-availability success");
      aa=false;
    };
    lcd.clear();
    lcd.setCursor(0, 0);
    lcd.print("Distance: ");
    lcd.print(distance);
    lcd.print(" cm");
    lcd.setCursor(0, 1);
    lcd.print("R.Distance: 3cm");
    dt=true;
  }

  

if (s2 == 0 && bb == true){
  if(!Firebase.setBool(firebaseData,"/parkings/Srinagar/slot2",false)){
    Serial.println("printing to dashboard abt slot 2 non-availability failed");
    }else{
      Serial.println("printing to dashboard abt slot 2 non-availability success");
      bb = false;
      };
}

if (s3 == 0 && cc == true){
  if(!Firebase.setBool(firebaseData,"/parkings/Srinagar/slot3",false)){
    Serial.println("printing to dashboard abt slot 3 non-availability failed");
    }else{
      Serial.println("printing to dashboard abt slot 3 non-availability success");
      cc = false;
      };
}

if (s4 == 0 && dd == true){
  if(!Firebase.setBool(firebaseData,"/parkings/SrinagarR/R1",false)){
    Serial.println("printing to dashboard abt slot 4 non-availability failed");
    }else{
      Serial.println("printing to dashboard abt slot 4 non-availability success");
      dd = false;
      };
}

delay(700);


  
}
