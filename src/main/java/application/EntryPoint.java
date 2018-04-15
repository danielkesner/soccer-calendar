package application;

import cache.CacheService;
import client.FootballDataRestClient;
import model.Fixture;
import model.LeagueTable;
import model.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


public class EntryPoint {

    private List<Team> topTeams;

    private static final FootballDataRestClient client = new FootballDataRestClient();

    private static final Logger logger = LogManager.getLogger(EntryPoint.class);

    private void doV2() throws Exception {
        long start, stop;
        start = System.currentTimeMillis();
        CacheService cacheService = new CacheService();
        cacheService.onStart();
        List<Integer> ids = cacheService.getAllCompetitionsIds();

        for (Integer competitionId : ids) {
            LeagueTable leagueTable = new LeagueTable(client.getLeagueTableByCompetitionId(competitionId));
            topTeams = leagueTable.getTopTeamsInLeagueTable();
            logger.info("CompEnum is: " + leagueTable.getCompetitionOfLeagueTable().toString());

            for (Team eachTeam : topTeams) {
                List<Fixture> list = client.getUpcomingMatchesByTeam(eachTeam, eachTeam.getCompetitionEnum());
                for (Fixture f : list) {
                    if (topTeams.contains(f.getHomeTeam()) && topTeams.contains(f.getAwayTeam())) {
                        logger.info("\n***\nWe found a good game!\n" + f.getHomeTeam().getTeamName()
                                + "\t\t" + f.getAwayTeam().getTeamName() + " on " + f.getDate() + "\n****");
                    }
                }
            }
        }

        stop = System.currentTimeMillis();
        logger.info("Total elapsed time: " + (stop-start)/1000 + " seconds.");
    }

    public static void main(String... a) throws Exception {
        new EntryPoint().doV2();
    }


}
