package controller;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import model.CalendarEvent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GoogleController {
    private ApplicationController applicationController;

    private static final String APPLICATION_NAME = "Gmail API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final String GoogleScopes[] = new String[] {GmailScopes.GMAIL_READONLY, CalendarScopes.CALENDAR};
    private static final List<String> SCOPES = new ArrayList<>(Arrays.asList(GoogleScopes));
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";


    /** Services **/
    private Gmail gmailService;
    private Calendar calendarService;

    /** Logging **/
    private Logger logger;

    public GoogleController(ApplicationController applicationController) {
        this.applicationController = applicationController;

        logger = Logger.getLogger("EventLog");
    }

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        logger.info("Loading Client Secrets");
        InputStream in = GoogleController.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            logger.warning("Client Secrets not Found");
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public void authenticateUser() throws GeneralSecurityException, IOException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        logger.info("Creating Gmail Service");
        gmailService= new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        logger.info("Creating Calendar Service");
        calendarService = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        applicationController.advanceWindows();
    }

    public void fetchGmailMessages() throws IOException {

        // Print the labels in the user's account.
        String user = "me";
        logger.info("\"Reservation\" \"for RIT Players\"");
        List<Message> messages = gmailService.users().messages().list(user).setQ("\"Reservation\" \"for RIT Players\" ").execute().getMessages();

        if (messages.isEmpty()) {
        } else {
            logger.info( messages.size()+" Found");
            applicationController.getCreationConfiguration().clearEvents();
            for (Message message : messages) {
                logger.info( "Processing message" + message.getId());
                logger.info("Fetching Additional Information");
                Message msg = gmailService.users().messages().get(user,message.getId()).execute();

                byte[] bodyBytes = Base64.decodeBase64(msg.getPayload().getBody().getData());
                String text = "";
                if(bodyBytes != null){
                    text += new String(bodyBytes, StandardCharsets.UTF_8).replaceAll("<\\s*[^>]*>","");
                }
                StringBuilder builder = new StringBuilder();
                getPlainTextFromMessageParts(msg.getPayload().getParts(),builder);
                text += builder.toString().replaceAll("<\\s*[^>]*>","");


                String resNumberRegex = "Reservation (\\d*)";
                Pattern resNumberPattern = Pattern.compile(resNumberRegex);
                Matcher resNumberMatcher = resNumberPattern.matcher(text);
                String resNumber = "";
                while (resNumberMatcher.find()) {
                    resNumber = resNumberMatcher.group(1);
                }

                logger.info("Found Res Number: " + resNumber);

                String eventInfoRegex = "(Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday), (January|Feburary|March|April|May|June|July|August|September|October|November|December) (\\d?\\d), (20\\d\\d)\\r\\n\\r?\\n?(\\d?\\d:\\d\\d (PM|AM)) - (\\d?\\d:\\d\\d (PM|AM)) (.*) \\(Student Org (Space|Event) (Confirmed|Request|Approved)\\) (.*)";
                Pattern eventInfoPattern = Pattern.compile(eventInfoRegex);
                Matcher eventInfoMatcher = eventInfoPattern.matcher(text);
                ArrayList<CalendarEvent> events = new ArrayList<>();

                while (eventInfoMatcher.find()) {
                    logger.info("Found Regex Match for message: " + msg.getId());
                    String eventName = eventInfoMatcher.group(9);
                    String location = eventInfoMatcher.group(12);
                    StringBuilder startDatetimeStr = new StringBuilder();
                    StringBuilder endDatetimeStr = new StringBuilder();
                    DateTime startDatetime;
                    DateTime endDatetime;

                    startDatetimeStr.append(eventInfoMatcher.group(2));
                    startDatetimeStr.append(" ");
                    startDatetimeStr.append(eventInfoMatcher.group(3));
                    startDatetimeStr.append(" ");
                    startDatetimeStr.append(eventInfoMatcher.group(4));
                    startDatetimeStr.append(" ");
                    startDatetimeStr.append(eventInfoMatcher.group(5));

                    endDatetimeStr.append(eventInfoMatcher.group(2));
                    endDatetimeStr.append(" ");
                    endDatetimeStr.append(eventInfoMatcher.group(3));
                    endDatetimeStr.append(" ");
                    endDatetimeStr.append(eventInfoMatcher.group(4));
                    endDatetimeStr.append(" ");
                    endDatetimeStr.append(eventInfoMatcher.group(7));

                    try {
                        String datePattern = "MMMMM dd yyyy hh:mm a";
                        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);

                        Date startDate = dateFormat.parse(startDatetimeStr.toString());
                        startDatetime = new DateTime(startDate);

                        Date endDate = dateFormat.parse(endDatetimeStr.toString());
                        endDatetime = new DateTime(endDate);


                        logger.info("Attempting to Create New Event");
                        events.add(new CalendarEvent(eventName, startDatetime, endDatetime, resNumber, location));

                    } catch (ParseException e) {
                        logger.warning(Arrays.toString(e.getStackTrace()));
                        e.printStackTrace();
                    }

                }
                logger.info("Adding Events and Going to Next Message");
                logger.info(events.size() + " Found in this Message");
                applicationController.getCreationConfiguration().addEvents(events);
            }
        }
    }

    private void getPlainTextFromMessageParts(List<MessagePart> messageParts, StringBuilder stringBuilder) {
        if (messageParts != null) {
            for (MessagePart messagePart : messageParts) {
                if (messagePart.getBody() != null && messagePart.getBody().getData() != null) {
                    stringBuilder.append(new String( Base64.decodeBase64(messagePart.getBody().getData())));
                }
                if (messagePart.getParts() != null) {
                    getPlainTextFromMessageParts(messagePart.getParts(), stringBuilder);
                }
            }
        }
    }

    public void fetchCalendars() throws IOException {
        String user = "me";
       List<CalendarListEntry> calendarListEntries = calendarService.calendarList().list().execute().getItems();

       for(CalendarListEntry e : calendarListEntries){

           if(e.getAccessRole().equals("owner") || e.getAccessRole().equals("writer")){
               applicationController.getCreationConfiguration().addCalendar(e.getSummary(),e.getId());
           }
       }

    }

    public void createCalendarEvents() {
        String calName = applicationController.getCreationConfiguration().getSelectedCalendar();
        String calendarId = applicationController.getCreationConfiguration().getCalendars().get(calName);

        for(CalendarEvent event : applicationController.getCreationConfiguration().getSelectedEvents()) {

            List<Event> potentialCollisions = getEventsBetweenTimes(event.getStartTime(), event.getEndTime(), calendarId);

            Event googleEvent = new Event()
                    .setSummary(event.getEventName())
                    .setLocation(event.getLocation())
                    .setDescription("Confirmation ID: " + event.getConfirmationID());

            EventDateTime start = new EventDateTime()
                    .setDateTime(event.getStartTime())
                    .setTimeZone("America/New_York");
            googleEvent.setStart(start);

            EventDateTime end = new EventDateTime()
                    .setDateTime(event.getEndTime())
                    .setTimeZone("America/New_York");
            googleEvent.setEnd(end);


            boolean collides = false;
            for(Event googEvent: potentialCollisions){
                if(googEvent.getDescription().equals(googleEvent.getDescription())){
                    collides = true;
                }
            }


            try {
                if(!collides) {
                    calendarService.events().insert(calendarId, googleEvent).execute();
                }
                else{
                    System.out.println("Collision!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        applicationController.advanceWindows();
    }

    public List<Event> getEventsBetweenTimes(DateTime start, DateTime end, String calendarId){
        try {
            return calendarService.events().list(calendarId).setTimeMin(start).setTimeMax(end).execute().getItems();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}

