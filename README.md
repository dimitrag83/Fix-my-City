# Fix-my-City

Report problems around the city using mobile devices through an Android application.


Citizens complete a simple form to report the problems providing the following information
•	Type
•	Address
•	Description
•	Date
•	Photo

Then this data are sent via HTTP POST in on-line database at url http://localhost/smartcity\senddata.php 
Saved report at http://localhost/smartcity\viewdata.php 

In the smart city senario every objects is identifying with a unique QR code.
The application uses the mobile device's camera to rea the QR code and identify the object. Then, it searches the object in the online database and presents its description  to the user.

After successfully retrieves the data, the app retrieves user’s current position automatically and show it on the map. User completes the rest of info and sends the data to report the problem.
