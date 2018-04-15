package model;

public class Team {

    private String teamName;
    private CompetitionEnum league;
    private int currentRank;
    private int teamApiId;  // API ID for a given team inside a given competition

    public CompetitionEnum getCompetitionEnum() {
        return this.league;
    }

    public Team(String teamName) {
        this.teamName = teamName;
    }

    public Team(String teamName, CompetitionEnum league, int currentRank) {
        this.teamName = teamName;
        this.league = league;
        this.currentRank = currentRank;
    }

    public Team(String name, CompetitionEnum competitionEnum, int rank, int apiId) {
        this.teamName = name;
        this.league = competitionEnum;
        this.currentRank = rank;
        this.teamApiId = apiId;
    }

    public int getTeamApiId() {
        return teamApiId;
    }

    public String getTeamName() {
        return this.teamName;
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
        //TODO: Check both league and team name before returning true
        Team t = (Team) teamToCompare;
        return this.getTeamName().equals(t.getTeamName());
    }

    public static int getTeamApiId(String url) {
        return Integer.parseInt(url.substring(url.lastIndexOf('/') + 1));
    }

}
