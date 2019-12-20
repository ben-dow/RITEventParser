# RITEventParser
An application for parsing emails from RIT's email event confirmation system and loading confirmed reservations into Google Calendar Events


## Download and Running:

` git clone https://github.com/ben-dow/RITEventParser.git `

To run this application you must have a `credentials.json` from the Google API Console that is setup for Gmail and Google Calendar. 

After adding `credentials.json` to `src/resources/`

`mvn exec:java`

## Building a Jar

`mvn assembly:assembly` will build the executable Jar in `target/`


## Using the Application

This application currently supports:
- On Campus Reservations
- Avoids Duplicating Events
- Calendar Selection for Events
- Event Selection
