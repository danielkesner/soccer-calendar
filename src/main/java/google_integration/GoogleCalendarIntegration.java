package google_integration;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import model.Fixture;
import util.DateTimeUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class GoogleCalendarIntegration {

    private static final String APPLICATION_NAME = "Soccer Notification App";

    /**
     * Directory to store user credentials for this application.
     */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/calendar-java-quickstart");

    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;

    /**
     * Global instance of the scopes required by this quickstart.
     * <p>
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/calendar-java-quickstart
     */
    private static final List<String> SCOPES =
            Arrays.asList(CalendarScopes.CALENDAR);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
                GoogleCalendarIntegration.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Calendar client service.
     */
    public static com.google.api.services.calendar.Calendar
    getCalendarService() throws IOException {
        Credential credential = authorize();
        return new com.google.api.services.calendar.Calendar.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private void mapFixturesToEvents() {

    }

    private static boolean setEventsInCalendar() {


        return false;
    }

    private Event createGoogleEventFromFixture(Fixture fixture) {
        if (fixture.getAwayTeam() == null || fixture.getHomeTeam() == null || fixture.getDate() == null) {
            throw new RuntimeException("Received invalid Fixture object in createGoogleEventFromFixture()!");
        }

        String homeTeam = fixture.getHomeTeam().getTeamName();
        String awayTeam = fixture.getAwayTeam().getTeamName();
        String date = fixture.getDate();

        Event event = new Event()
                .setDescription(homeTeam + " vs. " + awayTeam)
                .setSummary(homeTeam + " vs. " + awayTeam);

        EventDateTime startTime = new EventDateTime()
                .setDateTime(new DateTime(date))
                .setTimeZone("America/Los_Angeles");
        event.setStart(startTime);

        String endDateTime = DateTimeUtil.incrementTimeByTwoHours(date);
        EventDateTime endTime = new EventDateTime()
                .setDateTime(new DateTime(endDateTime))
                .setTimeZone("America/Los_Angeles");
        event.setEnd(endTime);

        return event;
    }


    public static void main(String[] args) throws IOException {

        com.google.api.services.calendar.Calendar service =
                getCalendarService();

        // Create Event for 5/12
        Event event = new Event()
                .setSummary("Test NUMBER TWO BITCH")
                .setLocation("Nowhere")
                .setDescription("Test EVENT TWO BITCH");

        DateTime startDateTime = new DateTime("2018-05-12T13:30:00Z");
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("America/Los_Angeles");
        event.setStart(start);

        DateTime endTime = new DateTime("2018-05-12T15:00:00Z");
        EventDateTime end = new EventDateTime()
                .setDateTime(endTime)
                .setTimeZone("America/Los_Angeles");
        event.setEnd(end);

        event = service.events().insert("primary", event).execute();
        System.out.printf("Event created: %s\n", event.getHtmlLink());

    }
}
