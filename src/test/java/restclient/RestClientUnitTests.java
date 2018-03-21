package restclient;

import client.FootballDataRestClient;
import com.fasterxml.jackson.databind.JsonNode;
import config.ConfigService;
import datamodel.Competition;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class RestClientUnitTests {

    private static final FootballDataRestClient client = new FootballDataRestClient();

    /*
     * Given PREMIER_LEAGUE enum, return 445
     * */
    @Test
    public void getCompetitionIdByLeaguePremierLeagueTest() throws IOException {
        Competition.CompetitionEnum competitionEnum = Competition.CompetitionEnum.PREMIER_LEAGUE;
        int ret = client.getCompetitionIdByLeague(competitionEnum);
        Assert.assertEquals(ret, 445, "Expected Premier League competition id getEnumByStringName 445, got: " + ret);
    }

    /*
     * Given LA_LIGA, return 455
     * */
    @Test
    public void getCompetitionIdByLeagueLaLigaTest() throws IOException {
        Competition.CompetitionEnum competitionEnum = Competition.CompetitionEnum.LA_LIGA;
        int ret = client.getCompetitionIdByLeague(competitionEnum);
        Assert.assertEquals(ret, 455, "Expected La Liga competition id getEnumByStringName 455, got: " + ret);
    }

    /*
     * Given BUNDESLIGA, return 452
     * */
    @Test
    public void getCompetitionIdByLeagueBundesliga() throws IOException {
        Competition.CompetitionEnum competitionEnum = Competition.CompetitionEnum.BUNDESLIGA;
        int ret = client.getCompetitionIdByLeague(competitionEnum);
        Assert.assertEquals(ret, 452, "Expected Bundesliga competition id getEnumByStringName 452, got: " + ret);
    }

    /*
     * Given LIGUE_1, return 450
     * */
    @Test
    public void getCompetitionIdByLeagueLigue1() throws IOException {
        Competition.CompetitionEnum competitionEnum = Competition.CompetitionEnum.LIGUE_1;
        int ret = client.getCompetitionIdByLeague(competitionEnum);
        Assert.assertEquals(ret, 450, "Expected Bundesliga competition id getEnumByStringName 450, got: " + ret);
    }

    /*
    * Given a JsonNode for Manchester City, return 65 (ManCity's internal ID from FootballData API)
    * */
    @Test
    public void getTeamIdByNameManchesterCity() throws Exception {
        Competition.CompetitionEnum competitionEnum = Competition.CompetitionEnum.PREMIER_LEAGUE;
        JsonNode node = client.getLeagueTableByCompetitionId(client.getCompetitionIdByLeague(competitionEnum)).get(0);
        final int expected = 65;
        Assert.assertEquals(client.getTeamIdFromTableSubNode(node), expected, "Expected Man City ID of 65, got " + client.getTeamIdFromTableSubNode(node));
    }

    @Test
    public void getTeamIdsForTopThresholdTeamsInPremierLeague() throws IOException {
        Competition.CompetitionEnum competitionEnum = Competition.CompetitionEnum.PREMIER_LEAGUE;
        Assert.assertEquals(client.getTeamIdsForTopThresholdTeams(competitionEnum).size(), ConfigService.getNumberOfCompetitions());
    }


    @Test
    void getTable() throws Exception {
        Competition.CompetitionEnum competitionEnum = Competition.CompetitionEnum.PREMIER_LEAGUE;
        JsonNode node = client.getLeagueTableByCompetitionId(client.getCompetitionIdByLeague(competitionEnum));

    }

}
