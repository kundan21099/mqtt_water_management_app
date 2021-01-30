### An Android App using MQTT, SQLite, Java Services and Chart


This app uses the hivemq public broker for MQTT (and paho mqtt library for java), SQLite to store the data locally on device and display the the history using charts on the app.

This App executes MQTT communication with ESP8266 Mqtt client, find the source code here [mqtt_water_management_implementation_on_esp8266](https://github.com/kundan21099/mqtt_water_management_implementation_on_esp8266)

***Stored Database using SQLite Example***


<img src="./img/Database.PNG" alt="Database" width="400"/>


***App Screenshots***

<img src="./img/textViewcrop.png" alt="Date in Text View" width="300"/>

<img src="./img/flowrate_screenshot.png" alt="Flow rate chart" width="300"/>

<img src="./img/volume.png" alt="Volume chart" width="300"/>

<img src="./img/alert.PNG" alt="Alert" width="300"/>



Note: As this app was developed in 2021 and Google ended Android Things support in 2022, this app might not work in newer version and with Android Things dependency. 
