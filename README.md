1 Installation
Click the ‘Sensor Data Display.apk’ to install the application on the smartphone.
 
2 Permission
After successful installation, open in turn: ‘Settings’ -> ‘Apps’ -> ‘Sensor Data Display’ -> ‘Permissions’. Allow the ‘Location’ and ‘Storage’ permissions.
     
3 Assemble the hardware module
Install the Bluetooth unit in the position correctly.
On the circuit board, RXD and TXD correspond to two blue lines. GND and VCC correspond to green line and yellow line respectively.
Connect the Arduino board with the laptop or other power. 
When the hardware module is in working state, the light on the sensor will light up and the ‘ON’ light on the Arduino processor will also light up.
The light on the Bluetooth unit will twinkle, the Bluetooth unit can be discovered by other devices.
When the ‘L’ and ‘TX’ lights on the Arduino processor twinkle, it respects that the Arduino processor is executing the code written and processing data.
 
4 Log in
New user enters new username and password and click ‘Register’ to register and sign in. User name must be in the form of email.
If user already has an account, the user enters username and password and click ‘Sign in’ to log in to the application.
Click Google ‘Sign in’ to use Google account to log in.
 
5 Offline simulation
In the login interface, click ‘Offline’ to start the offline mode.
Select the txt file saved in the smartphone.
3D model will move automatically based on the data saved in the selected file.
  
6 Scan Bluetooth devices
After successful login, in the Bluetooth device scanning interface, click ‘SCAN’ to discover the surrounding Bluetooth devices.
The name of Bluetooth unit of hardware module is ‘DSD TECH’.
Click ‘DSD TECH’ to connect to the hardware module.
 
7 Sensor data display
After connection, the application will show the ‘Text’ page and ‘connected’ prompt.
Click ‘Text’, ‘Curve’ or ‘3D Model’ to change the page for displaying data in number, curves and 3D Model.
The Text page show Bluetooth address, connection state and the data of temperature and rotation angles in numbers.
The Curve page show a chart with curves of the X, Y, Z axis rotation angles. The data will be added to the chart dynamically.
Users can swipe the chart to view the previous data.
Y axis respects rotation angle and its range is from -180 to 180. X axis is time stamp.
Blue line means X axis rotation angle. Red line means Y axis rotation angle. Black line means Z axis rotation angle. 
The rotation angle numbers are shown above curves.
In 3D Model page, click on the cube to select which cube to simulate the sensor movement in real time.
The selected cube will have blue boundary. Click on the area other than cubes, the model will not simulate with the sensor.
If the Bluetooth connection fails, click ‘CONNECT’ to connect to the ‘DSD TECH’.
   
8 Log out
Go back to the Bluetooth devices scanning interface. Click ‘SIGN OUT’ to log out. Then return to the login interface.
