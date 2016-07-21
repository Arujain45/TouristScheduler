# Tourist Scheduler

This project combines different existing sources of data related to events and points of interest (PoIs) in the city of Stockholm. The system generates a schedule to explore the PoIs that the users select based on their visit times, budget constraints, and type of transport. The front-end thus consists of a mobile application that shows a map of Stockholm and allows the users to enter information about their respective trips. These parameters are later used to generate a schedule based on the opening times of the PoIs, the traffic situation and other criteria.

#Integration with CityPulse

The Request Handler acts as the backend of the system. It receives PoI data and routes from CityPulse's Geospatial Data Infrastructure (GDI) using its Java interface. The package is already integrated in the backend, and communication with the GDI is done through an SSH tunnel.

#System requirements

* Request Handler: 
	- Linux operating system (tested with Ubuntu 14.04 LTS)
	- Java JRE 1.8.0_91 or higher
* Android application:
	- Android SDK 6.0 or higher
	- Google Play Services 9.0.2 or higher

#Running the system

* Request Handler:
	- Establish an SSH tunnel from the terminal. The password can be found in the GDI Java interface, which is available in the CityPulse Development SVN repository.
	```bash
	ssh -N -L5438:localhost:5432 tunnel@131.227.92.55 -p8020
	```
	- Run the Request Handler (run project, or run its .jar)

* Android application: install the APK on the phone

#Contributors
The system was developed at [Ericsson Research](<insert link>) as part of the EU project [CityPulse](<insert link>).
