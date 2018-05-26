# Fix-my-City

Report problems around the city using an Android application for mobile devices.

Citizens can report a problem or see the current reports around the city.

In the smart city senario every objects is identifying with a unique QR code.
The application uses the mobile device's camera to rea the QR code and identify the object. Then, it searches the object in the online database and presents its description  to the user.

After successfully identifying the object, the application retrieves user’s current position automatically and show it on the map. 

User completes the rest of information on a simple form (Type, Address, Description,	Date,	Photo)  and sends the data to report the problem.

The data are sent and retrieve via HTTP POST in on-line database ( url http://localhost/smartcity\senddata.php and  http://localhost/smartcity\viewdata.php )




