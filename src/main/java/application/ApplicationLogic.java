package application;

import cache.CacheService;
import client.FootballDataRestClient;
import com.google.api.services.calendar.model.Event;
import google_integration.GoogleCalendarIntegration;
import model.Fixture;
import model.LeagueTable;
import model.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

class ApplicationLogic {

    private static final FootballDataRestClient client = new FootballDataRestClient();
    private static final Logger logger = LogManager.getLogger(ApplicationLogic.class);

    static void doV2() throws Exception {
        long start, stop;
        start = System.currentTimeMillis();
        CacheService cacheService = new CacheService();
        cacheService.onStart();
        List<Integer> ids = cacheService.getAllCompetitionsIds();
        List<Team> topTeams;

        // For each competition
        for (Integer competitionId : ids) {
            LeagueTable leagueTable = new LeagueTable(client.getLeagueTableByCompetitionId(competitionId));
            topTeams = leagueTable.getTopTeamsInLeagueTable();

            // For each of the top teams
            for (Team eachTeam : topTeams) {
                List<Fixture> upcomingFixtures = client.getUpcomingMatchesByTeam(eachTeam, eachTeam.getCompetitionEnum());

                // For each fixture for each of the top teams
                for (Fixture currentFixture : upcomingFixtures) {
                    if (topTeams.contains(currentFixture.getHomeTeam()) && topTeams.contains(currentFixture.getAwayTeam())) {
                        Event event = GoogleCalendarIntegration.createGoogleEventFromFixture(currentFixture);
                        if (GoogleCalendarIntegration.setEventsInCalendar(event, GoogleCalendarIntegration.getCalendarInstance())) {
                            logger.info("\n***\nWe found a good game!\n" + currentFixture.getHomeTeam().getTeamName()
                                    + "\t\t" + currentFixture.getAwayTeam().getTeamName() + " on " + currentFixture.getDate() + "\n****");
                        } else {
                            logger.error("Failed to set event in calendar! "+ currentFixture.getHomeTeam().getTeamName() + " vs. " + currentFixture.getAwayTeam().getTeamName());
                        }
                    }
                }
            }
        }
        stop = System.currentTimeMillis();
        logger.info("Total elapsed time: " + (stop - start) / 1000 + " seconds.");
    }

}
