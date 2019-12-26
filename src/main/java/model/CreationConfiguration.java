package model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

public class CreationConfiguration {
    private String selectedCalendar;
    private HashMap<String,String> calendars;
    private List<CalendarEvent> selectedEvents;
    private List<CalendarEvent> allEvents;
    private boolean isAuthenticated;

    private Integer totalMessageFound;
    private Integer messagesProcessed;

    private PropertyChangeSupport propChangeSupport;

    public CreationConfiguration(){
        super();
        selectedEvents = new ArrayList<>();
        allEvents = new ArrayList<>();
        propChangeSupport = new PropertyChangeSupport(this);
        calendars = new HashMap<>();
        isAuthenticated = false;
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        propChangeSupport.addPropertyChangeListener(pcl);
    }

    public void setAuthenticated(boolean isAuthenticated){
        propChangeSupport.firePropertyChange(new PropertyChangeEvent(this,"isAuthenticated", this.isAuthenticated, isAuthenticated));
        this.isAuthenticated = isAuthenticated;

    }

    public boolean isAuthenticated(){
        return isAuthenticated;
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        propChangeSupport.removePropertyChangeListener(pcl);
    }

    public void addEvent(CalendarEvent event){
        allEvents.add(event);
    }

    public synchronized void addEvents(List<CalendarEvent> events){
        events.addAll(this.allEvents);
        propChangeSupport.firePropertyChange(new PropertyChangeEvent(this, "allEvents", this.allEvents, events));

        this.allEvents = events;

    }

    public void addCalendar(String calendarName, String calendarId){

        HashMap<String, String> oldCalendars = new HashMap<>();
        oldCalendars.putAll(this.calendars);

        calendars.put(calendarName,calendarId);

        propChangeSupport.firePropertyChange(new PropertyChangeEvent(this, "calendars", oldCalendars, this.calendars));
    }

    public HashMap<String,String> getCalendars() {
        return calendars;
    }

    public void setSelectedCalendar(String name){
        propChangeSupport.firePropertyChange(new PropertyChangeEvent(this, "selectedCalendar", selectedCalendar, name));

        selectedCalendar = name;

    }

    public void selectEvent(String confirmationId){
        Optional<CalendarEvent> event = allEvents.stream().filter(obj -> obj.getConfirmationID().equals(confirmationId)).findFirst();
        if(event.isPresent()){
            selectedEvents.add(event.get());
            allEvents.remove(event.get());
        }
    }

    public void selectEvent(CalendarEvent event){
        List<CalendarEvent> oldAllEvents = new ArrayList<>();
        oldAllEvents.addAll(this.allEvents);

        List<CalendarEvent> oldSelectedEvents = new ArrayList<>();
        oldSelectedEvents.addAll(this.selectedEvents);

        selectedEvents.add(event);
        allEvents.remove(event);

        propChangeSupport.firePropertyChange(new PropertyChangeEvent(this, "allEvents", oldAllEvents, this.allEvents));
        propChangeSupport.firePropertyChange(new PropertyChangeEvent(this, "selectedEvents", oldSelectedEvents, this.selectedEvents));

    }

    public void deselectEvent(CalendarEvent event){

        List<CalendarEvent> oldAllEvents = new ArrayList<>();
        oldAllEvents.addAll(this.allEvents);

        List<CalendarEvent> oldSelectedEvents = new ArrayList<>();
        oldSelectedEvents.addAll(this.selectedEvents);

        allEvents.add(event);
        selectedEvents.remove(event);

        propChangeSupport.firePropertyChange(new PropertyChangeEvent(this, "allEvents", oldAllEvents, this.allEvents));
        propChangeSupport.firePropertyChange(new PropertyChangeEvent(this, "selectedEvents", oldSelectedEvents, this.selectedEvents));

    }

    public void selectAllEvents(){
        List<CalendarEvent> oldAllEvents = new ArrayList<>();
        oldAllEvents.addAll(this.allEvents);

        List<CalendarEvent> oldSelectedEvents = new ArrayList<>();
        oldSelectedEvents.addAll(this.selectedEvents);

        selectedEvents.addAll(allEvents);
        allEvents = new ArrayList<>();

        propChangeSupport.firePropertyChange(new PropertyChangeEvent(this, "allEvents", oldAllEvents, this.allEvents));
        propChangeSupport.firePropertyChange(new PropertyChangeEvent(this, "selectedEvents", oldSelectedEvents, this.selectedEvents));
    }

    public void clearEvents(){
        List<CalendarEvent> oldAllEvents = new ArrayList<>();
        oldAllEvents.addAll(this.allEvents);

        List<CalendarEvent> oldSelectedEvents = new ArrayList<>();
        oldSelectedEvents.addAll(this.selectedEvents);

        selectedEvents = new ArrayList<>();
        allEvents = new ArrayList<>();

        propChangeSupport.firePropertyChange(new PropertyChangeEvent(this, "allEvents", oldAllEvents, this.allEvents));
        propChangeSupport.firePropertyChange(new PropertyChangeEvent(this, "selectedEvents", oldSelectedEvents, this.selectedEvents));

    }

    public String getSelectedCalendar() {
        return selectedCalendar;
    }

    public List<CalendarEvent> getSelectedEvents() {
        return selectedEvents;
    }

    public List<CalendarEvent> getAllEvents() {
        return allEvents;
    }

    public Integer getTotalMessageFound() {
        return totalMessageFound;
    }

    public void setTotalMessageFound(Integer totalMessageFound) {
        propChangeSupport.firePropertyChange(new PropertyChangeEvent(this, "totalMessageFound", this.totalMessageFound,totalMessageFound));
        this.totalMessageFound = totalMessageFound;
    }

    public Integer getMessagesProcessed() {
        return messagesProcessed;
    }

    public void incrementMessagesProcessed() {
        propChangeSupport.firePropertyChange(new PropertyChangeEvent(this, "messagesProcessed", this.messagesProcessed,this.messagesProcessed++));
        this.messagesProcessed++;
    }
    public void resetMessagesProcessed(){
        propChangeSupport.firePropertyChange(new PropertyChangeEvent(this, "messagesProcessed", this.messagesProcessed,0));
        this.messagesProcessed = 0;
    }
}
