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
import com.google.api.services.calendar.Calendar;
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
     * Allows write access to Calendar service objects
     * <p>
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/calendar-java-quickstart
     */
    private static final List<String> SCOPES =
            Arrays.asList(CalendarScopes.CALENDAR);

    /**
    *  Global calendar instance returned by getCalendarInstance() and
     *  statically initialized on load
    * */
    private static Calendar _calendarService;

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
            _calendarService = getCalendarService();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Returns a reference to the global calendar object used in this class
     * (Note: not thread-safe! Do not call this method in a multi-threaded context!)
     * */
    public static Calendar getCalendarInstance() {
        return _calendarService;
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

    public static boolean setEventsInCalendar(Event event, Calendar service) {
        try {
            service.events().insert("primary", event).execute();
        } catch (IOException ioe) {
            return false;
        }
        return true;
    }

    void v() {
        DateTime z = new DateTime("2017-10-08T13:00:30.767000Z");
    }

    public static Event createGoogleEventFromFixture(Fixture fixture) {
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

}
