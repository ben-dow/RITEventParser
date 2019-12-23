package model;

import com.google.api.client.util.DateTime;

import java.util.logging.Logger;


public class CalendarEvent {
    private String EventName;
    private DateTime startTime;
    private DateTime endTime;
    private String confirmationID;
    private String location;

    Logger logger;

    public CalendarEvent(String eventName, DateTime startTime, DateTime endTime, String confirmationID, String location) {
        logger = Logger.getLogger("EventLog");
        EventName = eventName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.confirmationID = confirmationID;
        this.location = location;

        logger.info("Created New Event: " + toString().replaceAll("\n", ""));
    }

    public String getEventName() {
        return EventName;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public String getConfirmationID() {
        return confirmationID;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return EventName + " " + getConfirmationID();
    }


    public String toDetailedString() {
        return "Name: " + EventName +
                "\nStart: " + startTime +
                "\nEnd: " + endTime +
                "\nConfirmation ID: " + confirmationID +
                "\nLocation: " + location;

    }
}
