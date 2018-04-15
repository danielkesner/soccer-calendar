package model;

import client.FootballDataRestClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Team {

    private String teamName;
    private Competition.CompetitionEnum league;
    private int currentRank;
    private FootballDataRestClient client;
    private int teamApiId;  // API ID for a given team inside a given competition

    Logger logger = LogManager.getLogger(Team.class);

    public Competition.CompetitionEnum getCompetitionEnum() {
        return this.league;
    }

    public Team(String teamName) {
        this.teamName = teamName;
    }

    public Team(String teamName, Competition.CompetitionEnum league) {
        this.teamName = teamName;
        this.league = league;
        client = new FootballDataRestClient();
        try {
            if (client.getRankingOfTeam(league, teamName) == -1) {
                throw new RuntimeException("Could not determine rank of " + teamName + " in " + league.toString() +
                        " when constructing new Team object!");
            }
            this.currentRank = client.getRankingOfTeam(league, teamName);
        } catch (IOException ioe) {
            logger.warn("Caught IOException when creating new Team object with params: "
                    + "\n" + teamName + "\n" + league + "\n" + ioe);
        }
    }

    public Team(String teamName, Competition.CompetitionEnum league, int currentRank) {
        this.teamName = teamName;
        this.league = league;
        this.currentRank = currentRank;
    }

    public Team(String name, Competition.CompetitionEnum competitionEnum, int rank, int apiId) {
        this.teamName = name;
        this.league = competitionEnum;
        this.currentRank = rank;
        this.teamApiId = apiId;
    }

    public Team setTeamName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Cannot set null teamName field!");
        }
        this.teamName = name;
        return this;
    }


    public int getTeamApiId() {
        return teamApiId;
    }

    public String getTeamName() {
        return this.teamName;
    }

    public Team setLeague(Competition.CompetitionEnum league) {
        this.league = league;
        return this;
    }

    public Competition.CompetitionEnum getLeague() {
        return this.league;
    }

    public Team setCurrentRank(int rank) {
        if (rank < 1 || rank > 20) {
            throw new IllegalArgumentException("Team's ranking must be between 1 and 20! Cannot be: " + rank);
        }
        this.currentRank = rank;
        return this;
    }

    public int getCurrentRank() {
        return this.currentRank;
    }

    /*
     * Team objects are equal if they're from the same league
     * and have the same team name
     *  */
    @Override
    public boolean equals(Object teamToCompare) {
//        if (this.getLeague() != teamToCompare.getLeague()) {
//            return false;
//        }
//        if (!this.getTeamName().equals(teamToCompare.getTeamName())) {
//            return false;
//        }
//        return true;
        //TODO: Check both league and team name before returning true.. however, for now,
        //TODO: the league is being null when calculated in Driver, so just check the team name
        Team t = (Team) teamToCompare;
        return this.getTeamName().equals(t.getTeamName());
    }

    //upcomingFixturesNode.get("fixtures").get(0).get("_links")
    // "homeTeam" -> "{"href":"http://api.football-data.org/v1/teams/67"}"
    //upcomingFixturesNode.get("fixtures").get(0).get("_links").get("homeTeam").get("href").asText()
    //upcomingFixturesNode.get("fixtures").get(0).get("_links").get("homeTeam").get("href").asText().substring(upcomingFixturesNode.get("fixtures").get(0).get("_links").get("homeTeam").get("href").asText().lastIndexOf('/')+1)
    public static int getTeamApiId(String url) {
        return Integer.parseInt(url.substring(url.lastIndexOf('/')+1));
    }

}
