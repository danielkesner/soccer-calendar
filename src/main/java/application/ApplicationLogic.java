package application;

import cache.CacheService;
import client.FootballDataRestClient;
import model.Fixture;
import model.LeagueTable;
import model.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

class ApplicationLogic {

    private static final FootballDataRestClient client = new FootballDataRestClient();
    private static final Logger logger = LogManager.getLogger(ApplicationLogic.class);

    static List<Fixture> doV2() throws Exception {
        long start, stop;
        start = System.currentTimeMillis();
        CacheService cacheService = new CacheService();
        cacheService.onStart();
        List<Integer> ids = cacheService.getAllCompetitionsIds();
        List<Team> topTeams;
        List<Fixture> gamesToLog = null;

        for (Integer competitionId : ids) {
            LeagueTable leagueTable = new LeagueTable(client.getLeagueTableByCompetitionId(competitionId));
            topTeams = leagueTable.getTopTeamsInLeagueTable();
            gamesToLog = new ArrayList<Fixture>();

            for (Team eachTeam : topTeams) {
                List<Fixture> upcomingFixtures = client.getUpcomingMatchesByTeam(eachTeam, eachTeam.getCompetitionEnum());
                for (Fixture currentFixture : upcomingFixtures) {
                    if (topTeams.contains(currentFixture.getHomeTeam()) && topTeams.contains(currentFixture.getAwayTeam())) {
                        logger.info("\n***\nWe found a good game!\n" + currentFixture.getHomeTeam().getTeamName()
                                + "\t\t" + currentFixture.getAwayTeam().getTeamName() + " on " + currentFixture.getDate() + "\n****");
                        gamesToLog.add(currentFixture);
                    }
                }
            }
        }

        stop = System.currentTimeMillis();
        logger.info("Total elapsed time: " + (stop - start) / 1000 + " seconds.");
        return gamesToLog;
    }

}
