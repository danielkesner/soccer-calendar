package model;

import client.FootballDataRestClient;
import config.ConfigService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Fixture {

    private Team homeTeam;
    private Team awayTeam;
    private String date;
    private Competition.CompetitionEnum competitionEnum;

    private static FootballDataRestClient client;

    static Logger logger = LogManager.getLogger(Fixture.class);

    static {
        client = new FootballDataRestClient();
    }

    public Fixture(Team homeTeam, Team awayTeam, String date) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.date = date;
    }

    public Fixture() { }

    public Competition.CompetitionEnum getCompetitionEnum() {
        return competitionEnum;
    }

    public void setCompetitionEnum(Competition.CompetitionEnum competitionEnum) {
        this.competitionEnum = competitionEnum;
    }

    public Team getHomeTeam() {
        return this.homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Team getAwayTeam() {
        return this.awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private static void setRankingsForEachTeam(Fixture fixture, Competition.CompetitionEnum competitionEnum) throws IOException {
        Team homeTeam = fixture.getHomeTeam();
        homeTeam.setCurrentRank(client.getRankingOfTeam(competitionEnum, homeTeam));

        Team awayTeam = fixture.getAwayTeam();
        awayTeam.setCurrentRank(client.getRankingOfTeam(competitionEnum, awayTeam));
    }

    /*
     * Returns a Team object representing the opponent of the
     * Team argument in the fixture passed in. Also sets the rankings for each team in the fixture.
     * */
//    public static Team getOpponent(Fixture fixture, Team team, Competition.CompetitionEnum competitionEnum) throws IOException {
//        setRankingsForEachTeam(fixture, competitionEnum);
//        if (ConfigService.debug()) {
//            logger.info("getOpponent() received the following arguments: \nFixture:\n" +
//                    "Home Team:\t" + fixture.getHomeTeam().getTeamName() + "\nAway team:\t" + fixture.getAwayTeam().getTeamName()
//                    + "\nHome rank:\t" + fixture.getHomeTeam().getCurrentRank() + "\nAway rank:\t" + fixture.getAwayTeam().getCurrentRank()
//                    + "\nTeam argument passed in: " + team.getTeamName() + "\t\tRank of team argument passed in:\t" + team.getCurrentRank());
//        }
//
//        // If the argument is the home team, the opponent is the away team
//        if (team.equals(fixture.getHomeTeam())) {
//            Team ret = fixture.getAwayTeam();
//            if (ConfigService.debug()) {
//                logger.info("getOpponent() returning the following Team: " + ret.getTeamName() + " with rank " + ret.getCurrentRank());
//            }
//            return ret;
//        }
//        else {
//            Team ret = fixture.getHomeTeam();
//            if (ConfigService.debug()) {
//                logger.info("getOpponent() returning the following Team: " + ret.getTeamName() + " with rank " + ret.getCurrentRank());
//            }
//            return ret;
//        }
//    }

//    public static Team getOpponent() {
//
//    }

    public String toString(Team team) {
        return team.getTeamName();
    }

}
