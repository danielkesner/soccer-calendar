package driver;

import client.FootballDataRestClient;
import com.fasterxml.jackson.databind.JsonNode;
import config.ConfigService;
import datamodel.Competition;
import datamodel.Fixture;
import datamodel.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Driver {

    private static final FootballDataRestClient client = new FootballDataRestClient();

    private static final Logger logger = LogManager.getLogger(Driver.class);

    public static void main(String... a) throws Exception {
//        System.setProperty("logging", "debug");
        System.setProperty("logging", "zzaaaz");
        //TODO: Home team always has rank of 0
        //TODO: Once this works for hard-coded premier league, iterate over all CompetitionEnums
        //TODO: Add Champions League enum
        //TODO: Once done, create LeagueTable object, recreate the table as an object that you get back from getLeagueTableByCompetitionEnum(), so you only have to make that method call once
        JsonNode eplTable = client.getLeagueTableByCompetitionEnum(Competition.CompetitionEnum.PREMIER_LEAGUE);
        for (int i = 0; i < ConfigService.getStandingsThresholdValue(); i++) {

            JsonNode team = eplTable.get(i);
            Team currentTeam = new Team(team.get("teamName").asText().replaceAll("\"", ""), Competition.CompetitionEnum.PREMIER_LEAGUE);
            String teamName = currentTeam.getTeamName();

            List<Fixture> upcomingFixtures = client.getUpcomingMatchesPayloadByTeam(Competition.CompetitionEnum.PREMIER_LEAGUE, teamName);

            for (Fixture fixture : upcomingFixtures) {

                if (client.isTeamWithinThresholdRanking(Competition.CompetitionEnum.PREMIER_LEAGUE, Fixture.getOpponent(fixture, currentTeam, Competition.CompetitionEnum.PREMIER_LEAGUE))) {

                    logger.info("\n*************\nFound good upcoming match! \nHome Team: " + fixture.getHomeTeam().getTeamName() + "\t\tRanking: " + fixture.getHomeTeam().getCurrentRank() +
                            "\nAway Team: " + fixture.getAwayTeam().getTeamName() + "\t\tRanking: " + fixture.getAwayTeam().getCurrentRank() + "\nDate: " + fixture.getDate()
                            + "\nCompetition: " + fixture.getCompetitionEnum().toString() + "\n*************");
                }

            }
        }
    }


}
