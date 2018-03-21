package datamodel;

import client.FootballDataRestClient;
import config.ConfigService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Team {

    private String teamName;
    private Competition.CompetitionEnum league;
    private int currentRank;
    private FootballDataRestClient client;

    Logger logger = LogManager.getLogger(Team.class);

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
                + "\n"+ teamName +"\n" + league + "\n"+ioe);
        }
    }

    public Team(String teamName, Competition.CompetitionEnum league, int currentRank) {
        this.teamName = teamName;
        this.league = league;
        this.currentRank = currentRank;
    }

    public Team setTeamName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Cannot set null teamName field!");
        }
        this.teamName = name;
        return this;
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
    public boolean equals(Team teamToCompare) {
//        if (this.getLeague() != teamToCompare.getLeague()) {
//            return false;
//        }
//        if (!this.getTeamName().equals(teamToCompare.getTeamName())) {
//            return false;
//        }
//        return true;
        //TODO: Check both league and team name before returning true.. however, for now,
        //TODO: the league is being null when calculated in Driver, so just check the team name
        return this.getTeamName().equals(teamToCompare.getTeamName());
    }

}
