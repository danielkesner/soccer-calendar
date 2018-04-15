package model;

import client.FootballDataRestClient;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class FixtureUnitTests {

    private static FootballDataRestClient client;

    static {
        client = new FootballDataRestClient();
        System.setProperty("logging", "debug");
    }


//    void getOpponent_and_assert_Swansea_not_in_threshold() throws IOException {
//        Team homeTeam = new Team("Manchester City FC", Competition.CompetitionEnum.PREMIER_LEAGUE, 1);
//        Team awayTeam = new Team("Swansea City FC", Competition.CompetitionEnum.PREMIER_LEAGUE, 14);
//        Fixture fixture = new Fixture();
//        fixture.setHomeTeam(homeTeam);
//        fixture.setAwayTeam(awayTeam);
//
//        Team expectedOpponent = new Team("Swansea City FC", Competition.CompetitionEnum.PREMIER_LEAGUE, 14);
//
//        Team _teamArg = new Team("Manchester City FC", Competition.CompetitionEnum.PREMIER_LEAGUE, 1);
//        Assert.assertTrue(Fixture.getOpponent(fixture, _teamArg, Competition.CompetitionEnum.PREMIER_LEAGUE).equals(expectedOpponent));
//        Assert.assertFalse(client.isTeamWithinThresholdRanking(Competition.CompetitionEnum.PREMIER_LEAGUE, expectedOpponent));
//    }

}
